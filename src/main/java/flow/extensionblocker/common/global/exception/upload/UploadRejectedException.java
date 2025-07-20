package flow.extensionblocker.common.global.exception.upload;

import flow.extensionblocker.common.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UploadRejectedException extends RuntimeException {

  private final int code;

  public UploadRejectedException() {
    super(ErrorCode.UPLOAD_REQUEST_REJECTED.getMessage());
    this.code = ErrorCode.UPLOAD_REQUEST_REJECTED.getCode();
  }
}
