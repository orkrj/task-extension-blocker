package flow.extensionblocker.common.exception;

import lombok.Getter;

@Getter
public class BlockerAlreadyExistsException extends RuntimeException {

  private final int code;

  public BlockerAlreadyExistsException() {
    super(ErrorCode.BLOCKER_ALREADY_EXISTS.getMessage());
    this.code = ErrorCode.BLOCKER_ALREADY_EXISTS.getCode();
  }
}
