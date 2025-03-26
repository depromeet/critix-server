import os
import json
import time
import requests
import re
from concurrent.futures import ThreadPoolExecutor, as_completed
import boto3

# S3 기본 설정
endpoint_url = "https://kr.object.ncloudstorage.com"
bucket_name = os.getenv("BUCKET_NAME")
access_key = os.getenv("ACCESS_KEY")
secret_key = os.getenv("SECRET_KEY")

# OCR API 설정
ocr_api_url = os.getenv("OCR_API_URL")
ocr_secret = os.getenv("OCR_SECRET")
headers = {
    "Content-Type": "application/json",
    "X-OCR-SECRET": ocr_secret
}

# S3 클라이언트 생성
s3 = boto3.client(
    's3',
    aws_access_key_id=access_key,
    aws_secret_access_key=secret_key,
    endpoint_url=endpoint_url
)

def get_presigned_url(key):
    return s3.generate_presigned_url(
        ClientMethod='get_object',
        Params={'Bucket': bucket_name, 'Key': key },
        ExpiresIn=3600
    )

def get_presigned_url_for_put(key):
    return s3.generate_presigned_url(
        ClientMethod='put_object',
        Params={'Bucket': bucket_name, 'Key': key, 'ContentType': 'application/json'},
        ExpiresIn=3600
    )

def process_image(idx, key, url, retry_count=1):
    filename = os.path.basename(key)

    payload = {
        "images": [
            {
                "format": "png",
                "name": f"image_{idx}",
                "url": url
            }
        ],
        "lang": "ko",
        "requestId": f"req_{idx}",
        "resultType": "string",
        "timestamp": int(time.time() * 1000),
        "version": "V1"
    }

    for attempt in range(1, retry_count + 2):
        try:
            res = requests.post(ocr_api_url, headers=headers, json=payload)
            if res.status_code == 200:
                fields = res.json()['images'][0].get('fields', [])
                texts = [f['inferText'] for f in fields]
                return filename, texts
            else:
                print(f"⚠️ OCR 요청 실패 ({filename}) - 시도 {attempt}, 상태코드: {res.status_code}")
        except Exception as e:
            print(f"⚠️ OCR 예외 발생 ({filename}) - 시도 {attempt}, 오류: {str(e)}")

        time.sleep(1)

    # 모든 시도 실패 시
    return filename, [f"OCR failed after {retry_count + 1} attempts"]

def process_s3_upload(event, context):
    start_time = time.time()
    print("🚀 이벤트 수신:", json.dumps(event, indent=2, ensure_ascii=False))

    try:
        key = event.get('object_name')
        if not key:
            return {"result": "error", "message": "object_name not provided in event"}

        match = re.match(r"([^/]+)/processed/completed\.txt", key)
        if not match:
            print("❌ userId 경로 파싱 실패")
            return {"result": "invalid path"}

        user_id = match.group(1)
        prefix = f"{user_id}/processed/"

        print(f"📁 S3 목록 조회 중: {prefix}")
        try:
            response = s3.list_objects_v2(Bucket=bucket_name, Prefix=prefix)
        except Exception as e:
            return {"result": "s3_list_error", "message": str(e)}

        contents = response.get("Contents", [])
        image_keys = [obj["Key"] for obj in contents if obj["Key"].endswith(".png")]

        if not image_keys:
            print("⚠️ 이미지 없음")
            return {"result": "no images"}

        print(f"✅ 이미지 {len(image_keys)}장 OCR 시작")

        ocr_results = {}
        with ThreadPoolExecutor(max_workers=4) as executor:
            futures = [
                executor.submit(process_image, idx, key, get_presigned_url(key))
                for idx, key in enumerate(image_keys, start=1)
            ]
            for future in as_completed(futures):
                try:
                    filename, texts = future.result()
                    ocr_results[filename] = " ".join(texts)
                    print(f"✅ OCR 완료: {filename}")
                except Exception as e:
                    print(f"❌ OCR 처리 실패 (Thread 내부): {str(e)}")
                    ocr_results[f"error_{time.time()}"] = f"Exception: {str(e)}"



        result_key = f"{user_id}/ocr/result.json"
        print("📝 OCR 결과:", result_key, ocr_results)

        # presigned URL로 결과 업로드
        try:
            upload_url = get_presigned_url_for_put(result_key)
        except Exception as e:
            return {"result": "presigned_url_error", "message": str(e)}

        try:
            res = requests.put(
                upload_url,
                data=json.dumps(ocr_results, ensure_ascii=False, indent=2).encode("utf-8"),
                headers={'Content-Type': 'application/json'}
            )

            if res.status_code == 200:
                print(f"✅ OCR 결과 presigned URL로 업로드 완료 → {result_key}")
            else:
                print(f"❌ 업로드 실패: {res.status_code}")
                return {"result": "upload error", "status_code": res.status_code}

        except Exception as e:
            return {"result": "upload_exception", "message": str(e)}

        elapsed = round(time.time() - start_time, 3)
        print(f"✅ OCR 전체 완료, 처리 시간: {elapsed}s")

        return {
            "result": "success",
            "image_count": len(image_keys),
            "processing_time_sec": elapsed
        }

    except Exception as e:
        print(f"❌ 최상위 오류 발생: {str(e)}")
        return {"result": "error", "message": str(e)}

def main(args):
    try:
        result = process_s3_upload(args, None)
        return result if isinstance(result, dict) else {"result": "invalid_return", "raw": str(result)}
    except Exception as e:
        print(f"❌ main() 예외 발생: {str(e)}")
        return {"result": "error", "message": str(e)}
