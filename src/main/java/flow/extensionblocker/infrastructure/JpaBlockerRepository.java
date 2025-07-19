package flow.extensionblocker.infrastructure;

import flow.extensionblocker.domain.Blocker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaBlockerRepository extends JpaRepository<Blocker, Long> {

  Optional<Blocker> findBlockerByExtension(String extension);

  Optional<Blocker> findBlockerByExtensionAndDeletedAtIsNull(String extension);

  @Query("SELECT b FROM Blocker b WHERE b.deletedAt IS NULL")
  List<Blocker> findBlockers();
}
