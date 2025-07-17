package flow.extensionblocker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

  @NotBlank(message = "확장자는 필수 입력값입니다.")
  @Pattern(
      regexp = "^[a-zA-Z0-9]{1,10}$",
      message = "확장자는 1자 이상 10자 이하의 영문 대소문자와 숫자로만 구성되어야 합니다."
  )
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
