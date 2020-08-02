package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.dartilla.bookkeeper.config.DataSourceBeanConfig;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Book;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.repositores.exception.MergeEntityNotExistingId;


import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import({BookRepositoryJpa.class, DataSourceBeanConfig.class})
class BookRepositoryJpaTest {

    @Autowired
    private BookRepositoryJpa bookRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("сохранять новую книгу")
    @Test
    public void shouldSaveNewBook() {
        Book expected = new Book(null, "Новая книга", true, new Author(1L, "Кастанеда К."), Set.of(new Genre(1L, "Мистика")));
        bookRepository.save(expected);
        Book actual = bookRepository.findByAuthorIdAndTitleAndStorage(
                expected.getAuthor().getId(), expected.getTitle(), expected.isInStorage()).stream().findFirst().get();
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "id");
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("обновлять существующую книгу")
    @Test
    public void shouldUpdateExisting() {
        Book expected = new Book(1L, "Новый заголовок", true, new Author(2L, null), Set.of(new Genre(2L, null)));
        bookRepository.save(expected);
        em.flush();
        Book actual = bookRepository.findById(expected.getId()).get();
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "author", "genres");
        assertThat(actual.getAuthor().getId()).isEqualTo(expected.getAuthor().getId());
        assertThat(actual.getGenres().iterator().next().getId()).isEqualTo(expected.getGenres().iterator().next().getId());
    }

    @DisplayName("кидать исключения про попытке обновить не существующую книгу")
    @Test
    public void shouldNotUpdateNotExistingBook() {
        Book expected = new Book(99L, "Новый заголовок", true, new Author(2L, null), Set.of(new Genre(2L, null)));
        assertThatThrownBy(() -> bookRepository.save(expected)).isInstanceOf(MergeEntityNotExistingId.class);
    }

    @DisplayName("находить книгу по идентификатору")
    @Test
    public void shouldFindById() {
        Book expected = new Book(1L, "Учение дона Хуана", true, new Author(1L, "Кастанеда К."), Set.of(new Genre(1L, "Мистика")));
        assertThat(bookRepository.findById(expected.getId()).get()).isEqualToComparingFieldByField(expected);
    }

    @DisplayName("находить пустую книгу по несуществующему идентификатору")
    @Test
    public void shouldFindEmptyByWrongId() {
        Book expected = new Book(-999L, "Учение дона Хуана", true, new Author(1L, "Кастанеда К."), Set.of(new Genre(1L, "Мистика")));
        assertThat(bookRepository.findById(expected.getId())).isEmpty();
    }

    @DisplayName("находить книгу по автору и заголовку и наличию в библиотеке")
    @Test
    public void shouldFindByAuthorAndTitleAndInStorage() {
        Book expected = new Book(2L, "Отдельная реальность", false, new Author(1L, "Кастанеда К."), Set.of(new Genre(1L, "Мистика")));
        assertThat(bookRepository.findByAuthorIdAndTitleAndStorage(expected.getAuthor().getId(), expected.getTitle(),
                false)).containsOnly(expected);
        assertThat(bookRepository.findByAuthorIdAndTitleAndStorage(expected.getAuthor().getId(), expected.getTitle(),
                true).stream().map(Book::getId).collect(toList())).containsExactly(3L, 4L);
    }

    @DisplayName("находить все книги")
    @Test
    public void shouldFindAll() {
        assertThat(bookRepository.getAll()).isNotEmpty();
    }
}