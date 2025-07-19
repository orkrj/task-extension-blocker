package flow.extensionblocker.application.dto;

import flow.extensionblocker.common.annotation.ValidExtension;
import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.Type;

public record CreateBlockerRequest(
    @ValidExtension String extension,
    Type type
) {
  public static Blocker toBlocker(CreateBlockerRequest request) {
    return Blocker.of(request.extension(), request.type());
  }
}
