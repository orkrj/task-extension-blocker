package flow.extensionblocker.presentation.controller;

import flow.extensionblocker.application.BlockerService;
import flow.extensionblocker.application.dto.BlockerResponse;
import flow.extensionblocker.application.dto.CreateBlockerRequest;
import flow.extensionblocker.application.dto.CreateBlockerResponse;
import flow.extensionblocker.application.dto.CustomBlockerCountResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blockers")
public class BlockerController {

  private final BlockerService blockerService;

  @PostMapping
  public ResponseEntity<CreateBlockerResponse> createBlocker(
      @Valid @RequestBody CreateBlockerRequest request
  ) {
    return ResponseEntity.ok(blockerService.createBlocker(request));
  }

  @GetMapping
  public ResponseEntity<List<BlockerResponse>> getBlockers() {
    return ResponseEntity.ok(blockerService.getBlockers());
  }

  @GetMapping("/fixed")
  public ResponseEntity<List<BlockerResponse>> getAllFixedBlockers() {
    return ResponseEntity.ok(blockerService.getAllFixedBlockers());
  }

  @GetMapping("/custom/count")
  public ResponseEntity<CustomBlockerCountResponse> getCustomBlockerCount() {
    return ResponseEntity.ok(blockerService.getCustomBlockerCount());
  }

  @PatchMapping("/{extension}")
  public ResponseEntity<Void> deleteBlocker(@PathVariable String extension) {
    blockerService.deleteBlocker(extension);
    return ResponseEntity.noContent().build();
  }
}
