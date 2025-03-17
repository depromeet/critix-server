package depromeet.onepiece.file.command.exception;

import static depromeet.onepiece.file.command.exception.FileCommandExceptionCode.FILE_NOT_FOUND;

import depromeet.onepiece.common.error.GlobalException;

public class FileNotFoundException extends GlobalException {
  public FileNotFoundException() {
    super(FILE_NOT_FOUND.getMessage(), FILE_NOT_FOUND);
  }
}
