package flow.extensionblocker.common.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  BLOCKER_NOT_FOUND(1001, "해당 차단기가 존재하지 않습니다."),
  BLOCKER_ALREADY_EXISTS(1002, "이미 존재하는 차단기입니다."),
  BLOCKER_ALREADY_DELETED(1003, "이미 삭제된 차단기입니다."),

  BLOCKER_VALIDATION_ERROR(2001, "유효성 검사에 실패했습니다."),
  BLOCKER_EXCEEDS_LIMIT(2002, "차단기 개수가 제한을 초과했습니다."),

  UPLOAD_REQUEST_REJECTED(9001, "차단기에 의해 업로드 요청이 거부되었습니다.");

  private final int code;
  private final String message;

  ErrorCode(final int code, final String message) {
    this.code = code;
    this.message = message;
  }
}
