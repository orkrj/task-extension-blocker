package flow.extensionblocker.common.global.exception;

import lombok.Getter;

@Getter
public class BlockerValidationException extends RuntimeException {

  private final int code;

  public BlockerValidationException() {
    super(ErrorCode.BLOCKER_VALIDATION_ERROR.getMessage());
    this.code = ErrorCode.BLOCKER_VALIDATION_ERROR.getCode();
  }
}
