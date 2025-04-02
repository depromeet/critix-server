package depromeet.onepiece.file.command.exception;

import static depromeet.onepiece.file.command.exception.FileCommandExceptionCode.FILE_CONVERT_ERROR;

import depromeet.onepiece.common.error.GlobalException;

public class FileConvertErrorException extends GlobalException {
  public FileConvertErrorException() {
    super(FILE_CONVERT_ERROR.getMessage(), FILE_CONVERT_ERROR);
  }
}
