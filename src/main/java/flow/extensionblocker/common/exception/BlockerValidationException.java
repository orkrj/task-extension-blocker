package flow.extensionblocker.common.exception;

import lombok.Getter;

@Getter
public class BlockerValidationException extends RuntimeException {

  private final int code;

  public BlockerValidationException(int code, String message) {
    super(ErrorCode.BLOCKER_VALIDATION_ERROR.getMessage());
    this.code = ErrorCode.BLOCKER_VALIDATION_ERROR.getCode();
  }
}
