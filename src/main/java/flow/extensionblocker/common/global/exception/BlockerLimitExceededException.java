package flow.extensionblocker.common.global.exception;

import lombok.Getter;

@Getter
public class BlockerLimitExceededException extends RuntimeException {

  private final int code;

  public BlockerLimitExceededException() {
    super(ErrorCode.BLOCKER_EXCEEDS_LIMIT.getMessage());
    this.code = ErrorCode.BLOCKER_EXCEEDS_LIMIT.getCode();
  }
}
