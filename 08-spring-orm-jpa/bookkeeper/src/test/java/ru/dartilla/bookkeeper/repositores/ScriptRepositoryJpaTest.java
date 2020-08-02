package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.dartilla.bookkeeper.config.DataSourceBeanConfig;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.repositores.exception.MergeEntityNotExistingId;


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
        Script expected = new Script(null, "Новая книга", new Author(1L, "Кастанеда К."), Set.of(new Genre(1L, "Мистика")));
        scriptRepository.save(expected);
        Script actual = scriptRepository.findByAuthorIdAndTitle(
                expected.getAuthor().getId(), expected.getTitle()).stream().findFirst().get();
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "id");
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("обновлять существующую рукопись")
    @Test
    public void shouldUpdateExisting() {
        Script expected = new Script(1L, "Новый заголовок", new Author(2L, null), Set.of(new Genre(2L, null)));
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
        Script expected = new Script(99L, "Новый заголовок", new Author(2L, null), Set.of(new Genre(2L, null)));
        assertThatThrownBy(() -> scriptRepository.save(expected)).isInstanceOf(MergeEntityNotExistingId.class);
    }

    @DisplayName("находить рукопись по идентификатору")
    @Test
    public void shouldFindById() {
        Script expected = new Script(1L, "Учение дона Хуана", new Author(1L, "Кастанеда К."), Set.of(new Genre(1L, "Мистика")));
        assertThat(scriptRepository.findById(expected.getId()).get()).isEqualToComparingFieldByField(expected);
    }

    @DisplayName("находить пустую рукопись по несуществующему идентификатору")
    @Test
    public void shouldFindEmptyByWrongId() {
        Script expected = new Script(-999L, "Учение дона Хуана", new Author(1L, "Кастанеда К."), Set.of(new Genre(1L, "Мистика")));
        assertThat(scriptRepository.findById(expected.getId())).isEmpty();
    }

    @DisplayName("находить рукопись по автору и заголовку")
    @Test
    public void shouldFindByAuthorAndTitle() {
        Script expected = new Script(2L, "Отдельная реальность", new Author(1L, "Кастанеда К."), Set.of(new Genre(1L, "Мистика")));
        assertThat(scriptRepository.findByAuthorIdAndTitle(expected.getAuthor().getId(), expected.getTitle()
                ).get()).isEqualTo(expected);
    }

    @DisplayName("находить все книги")
    @Test
    public void shouldFindAll() {
        assertThat(scriptRepository.getAll()).isNotEmpty();
    }
}