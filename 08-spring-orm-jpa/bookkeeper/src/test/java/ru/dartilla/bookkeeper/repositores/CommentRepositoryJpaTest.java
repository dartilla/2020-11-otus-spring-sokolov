package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.dartilla.bookkeeper.config.DataSourceBeanConfig;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.domain.Comment;

import java.util.Comparator;
import java.util.List;

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
        Comment expected = new Comment(2L, new Script(1L, null, null, null), new Comment(1L, null, null, null),
                "Читай дальше, там написано");
        Comment actual = commentRepository.findById(2L).get();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getScript().getId()).isEqualTo(expected.getScript().getId());
        assertThat(actual.getParent().getId()).isEqualTo(expected.getParent().getId());
    }

    @DisplayName("находить комментарии для книги")
    @Test
    void shouldFindByBook() {
        List<Comment> comments = commentRepository.findByScript(1L);
        assertThat(comments.size()).isEqualTo(3);
        Comment first = new Comment(1L, null, null, "Что курил автор?");
        Comment firstChild = new Comment(2L, null, first, "Читай дальше, там написано");
        Comment second = new Comment(3L, null, null, "Какая гадость эта ваша заливная рыба");
        assertThat(comments).usingElementComparator(Comparator.comparing(Comment::getId).thenComparing(Comment::getMessage))
                .containsOnly(first, second, firstChild);
    }
}