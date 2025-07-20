package flow.extensionblocker.application.dto;

import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.Type;

public record BlockerResponse(
    String extension,
    Type type,
    boolean enabled
) {
  public static BlockerResponse from(Blocker blocker) {
    return new BlockerResponse(blocker.getExtension(), blocker.getType(), blocker.isEnabled());
  }
}
