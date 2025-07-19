package flow.extensionblocker.application;

import flow.extensionblocker.application.dto.BlockerResponse;
import flow.extensionblocker.application.dto.CreateBlockerRequest;
import flow.extensionblocker.application.dto.CreateBlockerResponse;
import flow.extensionblocker.common.exception.BlockerLimitExceededException;
import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.BlockerRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockerService {

  private final BlockerRepository blockerRepository;

  @Transactional
  public CreateBlockerResponse createBlocker(CreateBlockerRequest request) {
    if (this.isOverCustomBlockerLimit()) {throw new BlockerLimitExceededException();}

    Optional<Blocker> checkedBlocker = isBlockerExists(request.extension());
    if (checkedBlocker.isPresent()) {
      return CreateBlockerResponse.from(checkedBlocker.get().restore());
    }

    Blocker blocker = blockerRepository.createBlocker(CreateBlockerRequest.toBlocker(request));
    return CreateBlockerResponse.from(blocker);
  }

  public List<BlockerResponse> getBlockers() {
    return blockerRepository.findBlockers().stream()
        .map(BlockerResponse::from)
        .toList();
  }

  @Transactional
  public void deleteBlocker(String extension) {
    Blocker blocker = this.findBlocker(extension);
    if (!blocker.isEnabled() && blocker.getDeletedAt() != null) {
      throw new IllegalArgumentException(extension + " 는 이미 삭제됨");
    }

    blocker.delete();
  }

  private Optional<Blocker> isBlockerExists(String extension) {
    return blockerRepository.findBlocker(extension);
  }

  private Blocker findBlocker(String extension) {
    return blockerRepository.findBlocker(extension)
        .orElseThrow(() -> new IllegalArgumentException(extension + " 가 없음"));
  }

  public boolean isBlocked(String extension) {
    return blockerRepository.findBlockerNotDeleted(extension).isPresent();
  }

  public boolean isOverCustomBlockerLimit() {
    return 200 - blockerRepository.countCustomBlockers() <= 0;
  }
}
