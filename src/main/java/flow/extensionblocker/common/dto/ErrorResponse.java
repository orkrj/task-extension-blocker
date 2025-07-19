package flow.extensionblocker.common.dto;

public record ErrorResponse(int serverCode, String message, String details) {}