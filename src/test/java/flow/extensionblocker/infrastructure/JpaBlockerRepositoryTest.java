package flow.extensionblocker.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import flow.extensionblocker.domain.Blocker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaBlockerRepositoryTest {

  @Autowired
  private JpaBlockerRepository sut;

  @Nested
  @DisplayName("차단기 조회 테스트")
  class FindBlocker {

    @Test
    @DisplayName("확장자에 해당하는 차단기를 조회한다")
    void test1() {
      // Given
      String extension = "exe";
      sut.save(Blocker.of(extension));

      // When
      var blocker = sut.findBlockerByExtension(extension);

      // Then
      assertTrue(blocker.isPresent());
      assertEquals(extension, blocker.get().getExtension());
    }

    @Test
    @DisplayName("존재하지 않는 확장자에 대한 차단기는 조회되지 않는다")
    void test2() {
      // Given
      String extension = "nonexistent";

      // When
      var blocker = sut.findBlockerByExtension(extension);

      // Then
      assertFalse(blocker.isPresent());
    }
  }
}