package flow.extensionblocker.infrastructure;

import flow.extensionblocker.domain.Blocker;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBlockerRepository extends JpaRepository<Blocker, Long> {
  Optional<Blocker> findBlockerByExtension(String extension);
}
