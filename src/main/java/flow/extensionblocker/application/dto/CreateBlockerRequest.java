package flow.extensionblocker.application.dto;

import flow.extensionblocker.common.annotation.ValidExtension;
import flow.extensionblocker.domain.Blocker;

public record CreateBlockerRequest(
    @ValidExtension String extension
) {
  public static Blocker toBlocker(CreateBlockerRequest request) {
    return Blocker.of(request.extension());
  }
}
