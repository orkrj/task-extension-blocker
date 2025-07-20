package flow.extensionblocker.application.dto;

import flow.extensionblocker.common.annotation.ValidExtension;
import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.Type;
import jakarta.validation.constraints.NotBlank;

public record CreateBlockerRequest(
    @NotBlank @ValidExtension String extension
) {
  public static Blocker toBlocker(CreateBlockerRequest request) {
    return Blocker.of(request.extension());
  }
}
