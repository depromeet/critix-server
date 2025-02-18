package depromeet.onepiece.file.command.exception;

import static depromeet.onepiece.file.command.exception.FileCommandExceptionCode.FILE_UPLOAD_FAILED;

import depromeet.onepiece.common.error.GlobalException;

public class FileUploadFailedException extends GlobalException {
  public FileUploadFailedException() {
    super(FILE_UPLOAD_FAILED.getMessage(), FILE_UPLOAD_FAILED);
  }
}
