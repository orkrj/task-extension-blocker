package flow.extensionblocker.infrastructure;

import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.BlockerRepository;
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
}
