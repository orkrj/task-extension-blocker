package flow.extensionblocker.application.dto;

import flow.extensionblocker.domain.Blocker;

public record BlockerResponse(
    String extension
) {
  public static BlockerResponse from(Blocker blocker) {
    return new BlockerResponse(blocker.getExtension());
  }
}
