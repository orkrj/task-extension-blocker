package flow.extensionblocker.domain;

import java.util.List;
import java.util.Optional;

public interface BlockerRepository {

  Blocker createBlocker(Blocker blocker);

  List<Blocker> findBlockers();

  Optional<Blocker> findBlocker(String extension);

  Optional<Blocker> findBlockerNotDeleted(String extension);
}
