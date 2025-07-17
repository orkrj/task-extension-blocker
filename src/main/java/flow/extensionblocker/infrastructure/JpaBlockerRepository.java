package flow.extensionblocker.infrastructure;

import flow.extensionblocker.domain.Blocker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBlockerRepository extends JpaRepository<Blocker, Long> {}
