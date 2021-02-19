package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.domain.Comment;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("репозиторий для работы с комментариями должен")
@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("находить комментарий по id")
    @Test
    void shouldFindById() {
        Script script = new Script("1", null, null, null);
        Comment expected = new Comment("2", script, new Comment("1", script, null, null),
                "Читай дальше, там написано");
        Comment actual = commentRepository.findById("2").get();
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getScript().getId()).isEqualTo(expected.getScript().getId());
        assertThat(actual.getParent().getId()).isEqualTo(expected.getParent().getId());
    }

    @DisplayName("находить комментраии для книги")
    @Test
    public void shouldContainComments() {
        List<Comment> comments = commentRepository.findByScriptId("1");
        assertThat(comments.size()).isEqualTo(3);
        assertThat(comments).usingElementComparator(Comparator.comparing(Comment::getMessage))
                .containsExactlyInAnyOrder(new Comment(null, null, null, "Что курил автор?"),
                        new Comment(null, null, null, "Читай дальше, там написано"),
                        new Comment(null, null, null, "Какая гадость эта ваша заливная рыба")
                );
    }

}