package flow.extensionblocker.common.global.exception.blocker;

import flow.extensionblocker.common.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BlockerValidationException extends RuntimeException {

  private final int code;

  public BlockerValidationException() {
    super(ErrorCode.BLOCKER_VALIDATION_ERROR.getMessage());
    this.code = ErrorCode.BLOCKER_VALIDATION_ERROR.getCode();
  }
}
