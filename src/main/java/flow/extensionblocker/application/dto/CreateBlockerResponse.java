package flow.extensionblocker.application.dto;

import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.Type;

public record CreateBlockerResponse(
    String extension,
    Type type,
    boolean enabled
) {
  public static CreateBlockerResponse from(Blocker blocker) {
    return new CreateBlockerResponse(blocker.getExtension(), blocker.getType(), blocker.isEnabled());
  }
}
