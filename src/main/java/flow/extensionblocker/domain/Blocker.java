package flow.extensionblocker.domain;

import flow.extensionblocker.common.annotation.ValidExtension;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter @Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "blockers",
    uniqueConstraints = @UniqueConstraint(columnNames ={"extension"})
)
public class Blocker {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ValidExtension
  @Column(nullable = false, updatable = false, length = 10, unique = true)
  private String extension;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime blockedTime;

  public static Blocker of(String extension) {
    return Blocker.builder()
        .extension(extension.toLowerCase())
        .build();
  }
}
