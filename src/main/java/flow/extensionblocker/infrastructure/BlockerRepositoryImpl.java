package flow.extensionblocker.infrastructure;

import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.BlockerRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlockerRepositoryImpl implements BlockerRepository {

  private final JpaBlockerRepository jpaBlockerRepository;

  @Override
  public Blocker createBlocker(Blocker blocker) {
    return jpaBlockerRepository.save(blocker);
  }

  @Override
  public List<Blocker> findBlockers() {
    return jpaBlockerRepository.findBlockers();
  }

  @Override
  public Optional<Blocker> findBlocker(String extension) {
    return jpaBlockerRepository.findBlockerByExtension(extension);
  }

  @Override
  public Optional<Blocker> findBlockerNotDeleted(String extension) {
    return jpaBlockerRepository.findBlockerByExtensionAndEnabledIsTrue(extension);
  }
}
