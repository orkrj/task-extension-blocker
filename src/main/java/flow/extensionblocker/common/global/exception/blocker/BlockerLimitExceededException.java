package flow.extensionblocker.common.global.exception.blocker;

import flow.extensionblocker.common.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BlockerLimitExceededException extends RuntimeException {

  private final int code;

  public BlockerLimitExceededException() {
    super(ErrorCode.BLOCKER_EXCEEDS_LIMIT.getMessage());
    this.code = ErrorCode.BLOCKER_EXCEEDS_LIMIT.getCode();
  }
}
