package flow.extensionblocker.domain;

import java.util.Optional;

public interface BlockerRepository {

  Blocker createBlocker(Blocker blocker);

  Optional<Blocker> findBlocker(String extension);
}
