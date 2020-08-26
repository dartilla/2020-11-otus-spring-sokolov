package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.dartilla.bookkeeper.config.DataSourceBeanConfig;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.domain.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA репозиторий для работы с комментариями должен")
@DataJpaTest
@Import({CommentRepositoryJpa.class, DataSourceBeanConfig.class})
class CommentRepositoryJpaTest {

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("находить комментарий по id")
    @Test
    void shouldFindById() {
        Script script = new Script(1L, null, null, null, null);
        Comment expected = new Comment(2L, script, new Comment(1L, script, null, null),
                "Читай дальше, там написано");
        Comment actual = commentRepository.findById(2L).get();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getScript().getId()).isEqualTo(expected.getScript().getId());
        assertThat(actual.getParent().getId()).isEqualTo(expected.getParent().getId());
    }
}