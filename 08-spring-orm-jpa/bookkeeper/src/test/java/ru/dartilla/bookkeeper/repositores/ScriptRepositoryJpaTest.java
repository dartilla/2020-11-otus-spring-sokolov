package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.dartilla.bookkeeper.config.DataSourceBeanConfig;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Comment;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.repositores.exception.MergeEntityNotExistingId;


import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JPA репозиторий для работы с рукописями")
@DataJpaTest
@Import({ScriptRepositoryJpa.class, DataSourceBeanConfig.class})
class ScriptRepositoryJpaTest {

    @Autowired
    private ScriptRepositoryJpa scriptRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("сохранять новую рукопись")
    @Test
    public void shouldSaveNew() {
        Script expected = new Script(null, "Новая книга", new Author(1L, "Кастанеда К."), Set.of(new Genre(1L, "Мистика")), null);
        scriptRepository.save(expected);
        Script actual = scriptRepository.findByAuthorIdAndTitle(
                expected.getAuthor().getId(), expected.getTitle()).stream().findFirst().get();
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "id");
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("обновлять существующую рукопись")
    @Test
    public void shouldUpdateExisting() {
        Script expected = new Script(1L, "Новый заголовок", new Author(2L, null), Set.of(new Genre(2L, null)), null);
        scriptRepository.save(expected);
        em.flush();
        Script actual = scriptRepository.findById(expected.getId()).get();
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "author", "genres");
        assertThat(actual.getAuthor().getId()).isEqualTo(expected.getAuthor().getId());
        assertThat(actual.getGenres().iterator().next().getId()).isEqualTo(expected.getGenres().iterator().next().getId());
    }

    @DisplayName("кидать исключения про попытке обновить не существующую рукопись")
    @Test
    public void shouldNotUpdateNotExistingBook() {
        Script expected = new Script(99L, "Новый заголовок", new Author(2L, null), Set.of(new Genre(2L, null)), null);
        assertThatThrownBy(() -> scriptRepository.save(expected)).isInstanceOf(MergeEntityNotExistingId.class);
    }

    @DisplayName("находить рукопись по идентификатору")
    @Test
    public void shouldFindById() {
        Script expected = new Script(1L, "Учение дона Хуана", new Author(1L, "Кастанеда К."), Set.of(new Genre(1L, "Мистика")), null);
        assertThat(scriptRepository.findById(expected.getId()).get()).isEqualToComparingOnlyGivenFields(expected,
                "id", "title", "author", "genres");
    }

    @DisplayName("находить пустую рукопись по несуществующему идентификатору")
    @Test
    public void shouldFindEmptyByWrongId() {
        Script expected = new Script(-999L, "Учение дона Хуана", new Author(1L, "Кастанеда К."), Set.of(new Genre(1L, "Мистика")), null);
        assertThat(scriptRepository.findById(expected.getId())).isEmpty();
    }

    @DisplayName("находить рукопись по автору и заголовку")
    @Test
    public void shouldFindByAuthorAndTitle() {
        Script expected = new Script(2L, "Отдельная реальность", new Author(1L, "Кастанеда К."), Set.of(new Genre(1L, "Мистика")), null);
        assertThat(scriptRepository.findByAuthorIdAndTitle(expected.getAuthor().getId(), expected.getTitle()
                ).get()).isEqualToComparingOnlyGivenFields(expected, "id", "title", "author", "genres");
    }

    @DisplayName("находить все книги")
    @Test
    public void shouldFindAll() {
        assertThat(scriptRepository.getAll()).isNotEmpty();
    }

    @DisplayName("находить комментраии для книги")
    @Test
    public void shouldContainComments() {
        Set<Comment> comments = scriptRepository.findById(1L).get().getComments();
        assertThat(comments.size()).isEqualTo(3);
        assertThat(comments).usingElementComparator(Comparator.comparing(Comment::getMessage))
                .containsExactlyInAnyOrder(new Comment(null, null, null, "Что курил автор?"),
                        new Comment(null, null, null, "Читай дальше, там написано"),
                        new Comment(null, null, null, "Какая гадость эта ваша заливная рыба")
                        );
    }
}