package flow.extensionblocker.application;

import flow.extensionblocker.application.dto.BlockerResponse;
import flow.extensionblocker.application.dto.CreateBlockerRequest;
import flow.extensionblocker.application.dto.CreateBlockerResponse;
import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.BlockerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockerService {

  private final BlockerRepository blockerRepository;

  public CreateBlockerResponse createBlocker(CreateBlockerRequest request) {
    Blocker blocker = blockerRepository.createBlocker(CreateBlockerRequest.toBlocker(request));
    return CreateBlockerResponse.from(blocker);
  }

  public List<BlockerResponse> getBlockers() {
    return blockerRepository.findBlockers().stream()
        .map(BlockerResponse::from)
        .toList();
  }

  private Blocker findBlocker(String extension) {
    return blockerRepository.findBlocker(extension)
        .orElseThrow(() -> new IllegalArgumentException("임시용 예외: " + extension + " 는 없음"));
  }
}
