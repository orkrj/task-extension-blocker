package flow.extensionblocker.common.global.exception;

import lombok.Getter;

@Getter
public class BlockerNotFoundException extends RuntimeException {

  private final int code;

  public BlockerNotFoundException() {
    super(ErrorCode.BLOCKER_NOT_FOUND.getMessage());
    this.code = ErrorCode.BLOCKER_NOT_FOUND.getCode();
  }
}
