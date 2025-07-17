package flow.extensionblocker.application.dto;

import flow.extensionblocker.domain.Blocker;

public record CreateBlockerResponse(
    String extension
) {
  public static CreateBlockerResponse from(Blocker blocker) {
    return new CreateBlockerResponse(blocker.getExtension());
  }
}
