package flow.extensionblocker.application.dto;

public record CustomBlockerCountResponse(
    int count,
    int limit
) {
  public static CustomBlockerCountResponse of(int count, int limit) {
    return new CustomBlockerCountResponse(count, limit);
  }
}
