package flow.extensionblocker.common.global.exception;

import lombok.Getter;

@Getter
public class BlockerAlreadyDeletedException extends RuntimeException {

  private final int code;

  public BlockerAlreadyDeletedException() {
    super(ErrorCode.BLOCKER_ALREADY_DELETED.getMessage());
    this.code = ErrorCode.BLOCKER_ALREADY_DELETED.getCode();
  }
}
