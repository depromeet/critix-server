package depromeet.onepiece.file.command.exception;

import static depromeet.onepiece.file.command.exception.FileCommandExceptionCode.FILE_FILTERING;

import depromeet.onepiece.common.error.GlobalException;

public class FileFilteringException extends GlobalException {
  public FileFilteringException() {
    super(FILE_FILTERING.getMessage(), FILE_FILTERING);
  }
}
