package flow.extensionblocker.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import flow.extensionblocker.domain.Blocker;
import flow.extensionblocker.domain.Type;
import flow.extensionblocker.infrastructure.persistence.JpaBlockerRepository;
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

    @Test
    @DisplayName("고정 확장자 차단기를 전부 조회한다.")
    void test3() {
      // Given
      sut.save(Blocker.of("exe", Type.FIXED, true));
      sut.save(Blocker.of("bat", Type.FIXED, false));
      sut.save(Blocker.of("txt", Type.CUSTOM, true));

      // When
      var fixedBlockers = sut.getAllFixedBlockers();

      // Then
      assertEquals(2, fixedBlockers.size());
      assertTrue(fixedBlockers.stream().anyMatch(b -> b.getExtension().equals("exe")));
      assertTrue(fixedBlockers.stream().anyMatch(b -> b.getExtension().equals("bat")));
    }

    @Test
    @DisplayName("활성화된 커스텀 확장자 차단기 등록 개수를 조회한다.")
    void test4() {
      // Given
      sut.save(Blocker.of("abc", Type.CUSTOM, true));
      sut.save(Blocker.of("def", Type.CUSTOM, true));
      sut.save(Blocker.of("ghi", Type.CUSTOM, false));
      sut.save(Blocker.of("jkl", Type.CUSTOM, true));
      sut.save(Blocker.of("mno", Type.FIXED, true));

      // When
      int customCount = sut.countCustomBlockers();

      // Then
      assertEquals(3, customCount);
    }
  }

  @Nested
  @DisplayName("차단기 전체 조회 테스트")
  class FindBlockers {

    @Test
    @DisplayName("차단기를 모두 조회한다")
    void test1() {
      // Given
      sut.save(Blocker.of("exe"));
      sut.save(Blocker.of("bat"));

      // When
      var blockers = sut.findBlockers();

      // Then
      assertEquals(2, blockers.size());
      assertTrue(blockers.stream().anyMatch(b -> b.getExtension().equals("exe")));
      assertTrue(blockers.stream().anyMatch(b -> b.getExtension().equals("bat")));
    }

    @Test
    @DisplayName("삭제된 차단기는 조회되지 않는다")
    void test2() {
      // Given
      var blocker = sut.save(Blocker.of("exe"));
      blocker.delete();

      // When
      var blockers = sut.findBlockers();

      // Then
      assertFalse(blockers.stream().anyMatch(b -> b.getExtension().equals("exe")));
    }

    @Test
    @DisplayName("차단기가 없는 경우 빈 리스트를 반환한다")
    void test3() {
      // When
      var blockers = sut.findBlockers();

      // Then
      assertTrue(blockers.isEmpty());
    }
  }

}