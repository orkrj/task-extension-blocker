package flow.extensionblocker.application;

import flow.extensionblocker.application.dto.BlockerResponse;
import flow.extensionblocker.application.dto.CreateBlockerRequest;
import flow.extensionblocker.application.dto.CreateBlockerResponse;
import flow.extensionblocker.application.dto.CustomBlockerCountResponse;
import flow.extensionblocker.common.global.exception.blocker.BlockerAlreadyDeletedException;
import flow.extensionblocker.common.global.exception.blocker.BlockerAlreadyExistsException;
import flow.extensionblocker.common.global.exception.blocker.BlockerLimitExceededException;
import flow.extensionblocker.common.global.exception.blocker.BlockerNotFoundException;
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

  private final static int CUSTOM_BLOCKER_LIMIT = 200;

  @Transactional
  public CreateBlockerResponse createBlocker(CreateBlockerRequest request) {
    if (this.isOverCustomBlockerLimit()) {throw new BlockerLimitExceededException();}

    Optional<Blocker> blocker = this.getBlocker(request.extension());
    if (blocker.isPresent()) {
        return CreateBlockerResponse.from(this.checkBlockerDuplication(blocker.get()));
    };

    Blocker savedBlocker = blockerRepository.createBlocker(CreateBlockerRequest.toBlocker(request));
    return CreateBlockerResponse.from(savedBlocker);
  }

  public List<BlockerResponse> getBlockers() {
    return blockerRepository.findBlockers().stream()
        .map(BlockerResponse::from)
        .toList();
  }

  public List<BlockerResponse> getAllFixedBlockers() {
    return blockerRepository.getAllFixedBlockers().stream()
        .map(BlockerResponse::from)
        .toList();
  }

  public CustomBlockerCountResponse getCustomBlockerCount() {
    return CustomBlockerCountResponse.of(blockerRepository.countCustomBlockers(), CUSTOM_BLOCKER_LIMIT);
  }

  @Transactional
  public void deleteBlocker(String extension) {
    Blocker blocker = this.findBlocker(extension);
    if (!blocker.isEnabled() && blocker.getDeletedAt() != null) {
      throw new BlockerAlreadyDeletedException();
    }

    blocker.delete();
  }

  private Optional<Blocker> getBlocker(String extension) {
    return blockerRepository.findBlocker(extension);
  }

  private Blocker checkBlockerDuplication(Blocker blocker) {
    if (blocker.isEnabled()) {throw new BlockerAlreadyExistsException();}

    blocker.restore();
    return blocker;
  }

  private Blocker findBlocker(String extension) {
    return blockerRepository.findBlocker(extension).orElseThrow(BlockerNotFoundException::new);
  }

  public boolean isBlocked(String extension) {
    return blockerRepository.findBlockerNotDeleted(extension).isPresent();
  }

  public boolean isOverCustomBlockerLimit() {
    return CUSTOM_BLOCKER_LIMIT - blockerRepository.countCustomBlockers() <= 0;
  }
}
