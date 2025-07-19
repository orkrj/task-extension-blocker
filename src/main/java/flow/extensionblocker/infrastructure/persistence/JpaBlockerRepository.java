package flow.extensionblocker.infrastructure.persistence;

import flow.extensionblocker.domain.Blocker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaBlockerRepository extends JpaRepository<Blocker, Long> {

  Optional<Blocker> findBlockerByExtension(String extension);

  Optional<Blocker> findBlockerByExtensionAndEnabledIsTrue(String extension);

  @Query("SELECT b FROM Blocker b WHERE b.enabled = true")
  List<Blocker> findBlockers();

  @Query("SELECT COUNT(b) FROM Blocker b WHERE b.type = 'CUSTOM' AND b.enabled = true")
  int countCustomBlockers();
}
