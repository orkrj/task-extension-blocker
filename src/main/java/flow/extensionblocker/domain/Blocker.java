package flow.extensionblocker.domain;

import flow.extensionblocker.common.annotation.ValidExtension;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.springframework.data.annotation.LastModifiedDate;
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
  @Column(nullable = false, updatable = false, length = 20, unique = true)
  private String extension;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Type type;

  @Column(nullable = false)
  private boolean enabled;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private  LocalDateTime updatedAt;

  @Column
  private LocalDateTime deletedAt;

  public static Blocker of(String extension, Type type) {
    return Blocker.builder()
        .extension(extension.toLowerCase())
        .type(type)
        .enabled(true)
        .build();
  }

  public static Blocker of(String extension, Type type, boolean enabled) {
    return Blocker.builder()
        .extension(extension.toLowerCase())
        .type(type)
        .enabled(enabled)
        .build();
  }

  public void delete() {
    this.enabled = false;
    this.deletedAt = LocalDateTime.now();
  }

  public Blocker restore() {
    this.enabled = true;
    return this;
  }
}
