package depromeet.onepiece.feedback.command.infrastructure;

import com.azure.ai.openai.assistants.AssistantsClient;
import com.azure.ai.openai.assistants.AssistantsClientBuilder;
import com.azure.ai.openai.assistants.models.*;
import com.azure.core.credential.KeyCredential;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AzureTest {

  private final AssistantsClient client;
  private final String assistantId;

  public AzureTest(@Value("${chatgpt.api-key}") String apiKey) {
    this.client = new AssistantsClientBuilder().credential(new KeyCredential(apiKey)).buildClient();
    this.assistantId = "asst_Wa3wjFoRU4eUiY2nv0Zew6nZ"; // OpenAI Assistant ID
  }

  /** ✅ 로컬 파일을 직접 읽어 Assistant에게 전달하는 메서드 */
  //    public Map<String, Object> processLocalPDF(String filePath) {
  //        Map<String, Object> response = new HashMap<>();
  //
  //        try {
  //            // 1️⃣ **로컬 파일 읽기**
  //            Path path = Paths.get(filePath);
  //            String fileContent = new String(Files.readAllBytes(path));
  //
  //            log.info("Read file successfully: {} (Size: {} characters)", filePath,
  // fileContent.length());
  //
  //            // 2️⃣ **Assistant 스레드 생성**
  //            AssistantThread thread = client.createThread(new AssistantThreadCreationOptions());
  //            String threadId = thread.getId();
  //            log.info("Created Thread ID: {}", threadId);
  //
  //            // 3️⃣ **파일 내용을 256,000자 단위로 나누어 여러 개의 메시지로 전송**
  //            int chunkSize = 256000;
  //            int totalChunks = (int) Math.ceil((double) fileContent.length() / chunkSize);
  //
  //            for (int i = 0; i < totalChunks; i++) {
  //                int start = i * chunkSize;
  //                int end = Math.min(start + chunkSize, fileContent.length());
  //                String chunk = fileContent.substring(start, end);
  //
  //                log.info("Sending chunk {}/{} (Size: {} characters)", i + 1, totalChunks,
  // chunk.length());
  //
  //                client.createMessage(threadId, new ThreadMessageOptions(MessageRole.USER,
  //                    "Here is part " + (i + 1) + " of a PDF file:\n\n" + chunk));
  //            }
  //
  //            // 4️⃣ **Assistant 실행**
  //            ThreadRun run = client.createRun(threadId, new CreateRunOptions(assistantId));
  //            log.info("Run ID: {}", run.getId());
  //
  //            // 5️⃣ **Assistant 실행 완료될 때까지 대기**
  //            while (true) {
  //                run = client.getRun(threadId, run.getId());
  //                log.info("Run status: {}", run.getStatus());
  //
  //                if (run.getStatus() == RunStatus.COMPLETED) {
  //                    log.info("Assistant response is ready!");
  //                    break;
  //                } else if (run.getStatus() == RunStatus.FAILED) {
  //                    log.error("Assistant execution failed!");
  //                    response.put("status", "failed");
  //                    return response;
  //                }
  //
  //                Thread.sleep(2000);
  //            }
  //
  //            // 6️⃣ **Assistant 응답 가져오기**
  //            List<ThreadMessage> messages = client.listMessages(threadId).getData();
  //            StringBuilder assistantResponse = new StringBuilder();
  //
  //            if (messages.isEmpty()) {
  //                log.warn("No response from Assistant.");
  //                response.put("status", "completed");
  //                response.put("message", "No response from Assistant.");
  //                return response;
  //            }
  //
  //            for (ThreadMessage message : messages) {
  //                List<MessageContent> contents = message.getContent();
  //
  //                List<String> textContents = contents.stream()
  //                    .filter(MessageTextContent.class::isInstance)
  //                    .map(MessageTextContent.class::cast)
  //                    .map(this::extractText)
  //                    .collect(Collectors.toList());
  //
  //                String textResponse = String.join("\n", textContents);
  //                assistantResponse.append(textResponse).append("\n");
  //            }
  //
  //            response.put("status", "completed");
  //            response.put("assistant_response", assistantResponse.toString().trim());
  //
  //        } catch (IOException e) {
  //            log.error("Error reading local PDF", e);
  //            response.put("status", "error");
  //            response.put("message", "Failed to read and process local PDF file.");
  //        } catch (InterruptedException e) {
  //            Thread.currentThread().interrupt();
  //            response.put("status", "error");
  //            response.put("message", "Assistant execution interrupted.");
  //        }
  //
  //        return response;
  //    }
  //
  //    private String extractText(MessageTextContent content) {
  //        if (content == null) {
  //            return "";
  //        }
  //        return content.getText().toString();
  //    }

  //    public Map<String, Object> uploadAndAnalyzePDF(String filePath) {
  //        Map<String, Object> response = new HashMap<>();
  //
  //        try {
  //            // 1️⃣ **로컬 PDF 파일을 OpenAI에 업로드**
  //            Path path = Paths.get(filePath);
  //            byte[] fileBytes = Files.readAllBytes(path);
  //
  //            FileDetails fileDetails = new FileDetails(BinaryData.fromBytes(fileBytes),
  // path.getFileName().toString());
  //
  //            OpenAIFile uploadedFile = client.uploadFile(fileDetails, FilePurpose.ASSISTANTS);
  //            String fileId = uploadedFile.getId();
  //            log.info("Uploaded PDF File ID: {}", fileId);
  //
  //            // 2️⃣ **업로드된 파일을 Assistant에게 전달**
  //            AssistantThread thread = client.createThread(new AssistantThreadCreationOptions());
  //            String threadId = thread.getId();
  //            log.info("Created Thread ID: {}", threadId);
  //
  //            client.createMessage(threadId, new ThreadMessageOptions(MessageRole.USER,
  //                "Please analyze the content of the uploaded PDF file: " + fileId));
  //
  //            // 3️⃣ **Assistant 실행**
  //            ThreadRun run = client.createRun(threadId, new CreateRunOptions(assistantId));
  //            log.info("Run ID: {}", run.getId());
  //
  //            // 4️⃣ **Assistant 실행 완료될 때까지 대기**
  //            while (true) {
  //                run = client.getRun(threadId, run.getId());
  //                log.info("Run status: {}", run.getStatus());
  //
  //                if (run.getStatus() == RunStatus.COMPLETED) {
  //                    log.info("Assistant response is ready!");
  //                    break;
  //                } else if (run.getStatus() == RunStatus.FAILED) {
  //                    log.error("Assistant execution failed!");
  //                    response.put("status", "failed");
  //                    return response;
  //                }
  //
  //                Thread.sleep(2000);
  //            }
  //
  //            // 5️⃣ **Assistant 응답 가져오기**
  //            List<ThreadMessage> messages = client.listMessages(threadId).getData();
  //            StringBuilder assistantResponse = new StringBuilder();
  //
  //            if (messages.isEmpty()) {
  //                log.warn("No response from Assistant.");
  //                response.put("status", "completed");
  //                response.put("message", "No response from Assistant.");
  //                return response;
  //            }
  //
  //            for (ThreadMessage message : messages) {
  //                List<MessageContent> contents = message.getContent();
  //
  //                // ✅ `MessageTextContent`에서 `MessageTextDetails` 가져오기
  //                List<String> textContents = contents.stream()
  //                    .filter(MessageTextContent.class::isInstance)
  //                    .map(MessageTextContent.class::cast)
  //                    .map(textContent -> textContent.getText()) // ✅ MessageTextDetails 가져오기
  //                    .map(MessageTextDetails::getValue) // ✅ 실제 텍스트 값 가져오기
  //                    .collect(Collectors.toList());
  //
  //                String textResponse = String.join("\n", textContents);
  //                assistantResponse.append(textResponse).append("\n");
  //            }
  //
  //            response.put("status", "completed");
  //            response.put("assistant_response", assistantResponse.toString().trim());
  //
  //        } catch (IOException | InterruptedException e) {
  //            log.error("Error uploading and processing PDF", e);
  //            response.put("status", "error");
  //            response.put("message", "Failed to upload and process PDF file.");
  //        }
  //
  //        return response;
  //    }

  //    public Map<String, Object> uploadAndAnalyzePDF(String filePath) {
  //        Map<String, Object> response = new HashMap<>();
  //
  //        try {
  //            // 1️⃣ **로컬 PDF 파일을 OpenAI에 업로드**
  //            Path path = Paths.get(filePath);
  //            byte[] fileBytes = Files.readAllBytes(path);
  //
  //            FileDetails fileDetails = new FileDetails(BinaryData.fromBytes(fileBytes),
  // path.getFileName().toString());
  //
  //            OpenAIFile uploadedFile = client.uploadFile(fileDetails, FilePurpose.ASSISTANTS);
  //            String fileId = uploadedFile.getId();
  //            log.info("Uploaded PDF File ID: {}", fileId);
  //
  //            // 2️⃣ **Assistant 스레드 생성**
  //            AssistantThread thread = client.createThread(new AssistantThreadCreationOptions());
  //            String threadId = thread.getId();
  //            log.info("Created Thread ID: {}", threadId);
  //
  //            // 3️⃣ **파일을 참조하여 분석하도록 Assistant에게 요청**
  //            client.createMessage(threadId,
  //                new ThreadMessageOptions(MessageRole.USER, "Please analyze the uploaded PDF
  // file.")
  //                    .setAttachments(Collections.singletonList(
  //                        new MessageAttachment(fileId,
  // Collections.singletonList(BinaryData.fromString(""))) // 올바른 형식으로 수정
  //                    ))
  //            );
  //
  //            // 4️⃣ **Assistant 실행**
  //            ThreadRun run = client.createRun(threadId, new CreateRunOptions(assistantId));
  //            log.info("Run ID: {}", run.getId());
  //
  //            // 5️⃣ **Assistant 실행 완료될 때까지 대기**
  //            while (true) {
  //                run = client.getRun(threadId, run.getId());
  //                log.info("Run status: {}", run.getStatus());
  //
  //                if (run.getStatus() == RunStatus.COMPLETED) {
  //                    log.info("Assistant response is ready!");
  //                    break;
  //                } else if (run.getStatus() == RunStatus.FAILED) {
  //                    log.error("Assistant execution failed!");
  //                    response.put("status", "failed");
  //                    return response;
  //                }
  //
  //                Thread.sleep(2000);
  //            }
  //
  //            // 6️⃣ **Assistant 응답 가져오기**
  //            List<ThreadMessage> messages = client.listMessages(threadId).getData();
  //            StringBuilder assistantResponse = new StringBuilder();
  //
  //            if (messages.isEmpty()) {
  //                log.warn("No response from Assistant.");
  //                response.put("status", "completed");
  //                response.put("message", "No response from Assistant.");
  //                return response;
  //            }
  //
  //            for (ThreadMessage message : messages) {
  //                List<MessageContent> contents = message.getContent();
  //
  //                List<String> textContents = contents.stream()
  //                    .filter(MessageTextContent.class::isInstance)
  //                    .map(MessageTextContent.class::cast)
  //                    .map(textContent -> textContent.getText()) // ✅ MessageTextDetails 가져오기
  //                    .map(MessageTextDetails::getValue) // ✅ 실제 텍스트 값 가져오기
  //                    .collect(Collectors.toList());
  //
  //                String textResponse = String.join("\n", textContents);
  //                assistantResponse.append(textResponse).append("\n");
  //            }
  //
  //            response.put("status", "completed");
  //            response.put("assistant_response", assistantResponse.toString().trim());
  //
  //        } catch (IOException | InterruptedException e) {
  //            log.error("Error uploading and processing PDF", e);
  //            response.put("status", "error");
  //            response.put("message", "Failed to upload and process PDF file.");
  //        }
  //
  //        return response;
  //    }

  public Map<String, Object> runAssistant() {

    //        String openAiApiKey = "your-openai-api-key";
    //        OpenAIClient client = new OpenAIClient(new KeyCredential(openAiApiKey));
    //
    //        // OpenAI Assistant ID
    //        String assistantId = "your-assistant-id";
    //
    //        // 메시지 생성
    //        ThreadMessage message = new ThreadMessage(MessageRole.USER, "Solve 3x + 11 = 14");
    //
    //        // 스레드 생성 옵션
    //        AssistantThreadCreationOptions threadOptions = new AssistantThreadCreationOptions()
    //            .setMessages(Arrays.asList(message));
    //
    //        // 스레드 실행 옵션
    //        CreateAndRunThreadOptions createAndRunThreadOptions = new
    // CreateAndRunThreadOptions(assistantId)
    //            .setThread(threadOptions);
    //
    //        // 스레드 실행
    //        Run run = client.createThreadAndRun(createAndRunThreadOptions);

    Map<String, Object> response = new HashMap<>();

    // 1️⃣ **새로운 스레드 생성**
    AssistantThread thread = client.createThread(new AssistantThreadCreationOptions());
    String threadId = thread.getId();
    log.info("Thread ID: {}", threadId);

    // 2️⃣ **사용자 메시지 전송**
    String userMessage = "I need to solve the equation `3x + 11 = 14`. Can you help me?";
    client.createMessage(threadId, new ThreadMessageOptions(MessageRole.USER, userMessage));

    // 3️⃣ **Assistant 실행 (Run 생성)**
    ThreadRun run = client.createRun(threadId, new CreateRunOptions(assistantId));
    log.info("Run ID: {}", run.getId());

    // 4️⃣ **Assistant 실행 완료될 때까지 대기**
    while (true) {
      run = client.getRun(threadId, run.getId()); // 실행 상태 업데이트
      log.info("Run status: {}", run.getStatus());

      if (run.getStatus() == RunStatus.COMPLETED) {
        log.info("Assistant response is ready!");
        break;
      } else if (run.getStatus() == RunStatus.FAILED) {
        log.error("Assistant execution failed!");
        response.put("status", "failed");
        return response;
      }

      try {
        Thread.sleep(2000); // 2초 대기 후 다시 상태 확인
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    // 5️⃣ **응답 메시지 가져오기**
    List<ThreadMessage> messages = client.listMessages(threadId).getData();
    StringBuilder assistantResponse = new StringBuilder();

    if (messages.isEmpty()) {
      log.warn("No response from Assistant.");
      response.put("status", "completed");
      response.put("message", "No response from Assistant.");
      return response;
    }

    // ✅ `MessageTextContent`에서 실제 텍스트 추출
    for (ThreadMessage message : messages) {
      List<MessageContent> contents = message.getContent();

      // ✅ `MessageTextContent`만 필터링하여 변환
      List<String> textContents =
          contents.stream()
              .filter(MessageTextContent.class::isInstance) // 정확한 타입 필터링
              .map(MessageTextContent.class::cast) // 안전한 캐스팅
              .map(this::extractText) // ✅ `extractText()` 사용하여 변환
              .collect(Collectors.toList());

      // ✅ 문자열을 `\n` 기준으로 합치기
      String textResponse = String.join("\n", textContents);

      assistantResponse.append(textResponse).append("\n");
    }

    response.put("status", "completed");
    response.put("assistant_response", assistantResponse.toString().trim());
    return response;
  }

  /** ✅ `MessageTextContent`에서 텍스트를 추출하는 메서드 */
  private String extractText(MessageTextContent content) {
    if (content == null) {
      return "";
    }

    // ✅ `MessageTextContent.getText()`의 반환 타입이 `MessageTextDetails`인지 확인
    if (content.getText() instanceof MessageTextDetails) {
      return ((MessageTextDetails) content.getText()).getValue(); // ✅ 문자열 값 가져오기
    }

    return "";
  }
}
