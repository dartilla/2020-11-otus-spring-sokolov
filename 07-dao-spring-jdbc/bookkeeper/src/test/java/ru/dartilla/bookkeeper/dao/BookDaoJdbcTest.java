package ru.dartilla.bookkeeper.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.domain.Book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Dao для работы с книгами должно в БД")
@JdbcTest
@Import(BookDaoJdbc.class)
class BookDaoJdbcTest {

    @Autowired
    BookDaoJdbc dao;

    @DisplayName("сохранять книгу с null идентификатором")
    @Test
    void shouldInsertBookWithNullId() {
        Book expected = new Book(null, "Знак четырех", 2L, 2L, true);
        dao.insert(expected);
        assertThat(dao.getAll().stream().filter(vo -> vo.getTitle().equals(expected.getTitle())).count()).isEqualTo(1);
    }

    @DisplayName("сохранять книгу с заданым идентификатором")
    @Test
    void shouldInsertBook() {
        Book expected = new Book(79L, "Знак четырех", 2L, 2L, true);
        dao.insert(expected);
        assertThat(dao.getById(79L)).isEqualToComparingFieldByField(expected);
    }

    @DisplayName("не сохранять книгу с существующим id")
    @Test
    void shouldNotInsertDuplicatedIdBook() {
        Book expected = new Book(1L, "Знак четырех", 2L, 2L, true);
        assertThatThrownBy(() -> dao.insert(expected)).isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("обновлять книгу")
    @Test
    void shouldUpdateBook() {
        Book expected = new Book(1L, "Знак четырех", 2L, 2L, true);
        assertThat(dao.getById(1L).getTitle()).isNotEqualTo("Знак четырех");
        dao.update(expected);
        assertThat(dao.getById(1L)).isEqualToComparingFieldByField(expected);
    }

    @DisplayName("не обновлять несуществующую книгу")
    @Test
    void shouldNotUpdateNotExistingBook() {
        dao.delete(1L);
        Book expected = new Book(1L, "Знак четырех", 2L, 2L, true);
        dao.update(expected);
        assertThatThrownBy(() -> dao.getById(1L)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("удалять существующую книгу")
    @Test
    void shouldDeleteExistingBook() {
        Book book = dao.getById(1L);
        dao.delete(book.getId());
        assertThatThrownBy(() -> dao.getById(book.getId())).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("не падать при удалении несуществующей книги")
    @Test
    void shouldNotFailDeletingNotExistingBook() {
        dao.delete(-1L);
    }

    @DisplayName("получать книгу по идентификатору")
    @Test
    void shouldGetById() {
        assertThat(dao.getById(1L)).hasFieldOrPropertyWithValue("id", 1L);
    }

    @DisplayName("находить книгу по идентификатору")
    @Test
    void shouldFindById() {
        assertThat(dao.findById(1L).get()).hasFieldOrPropertyWithValue("id", 1L);
    }

    @DisplayName("не находить книгу по несуществующему идентификатору")
    @Test
    void shouldNotFindByWrongId() {
        assertThat(dao.findById(-1L)).isEmpty();
    }

    @DisplayName("получать все книги")
    @Test
    void getAll() {
        assertThat(dao.getAll()).isNotEmpty();
    }

    @DisplayName("находить информацию о доступности книг")
    @Test
    void shouldFindBookOverview() {
        BookOverviewVo one = new BookOverviewVo("Учение дона Хуана", "К. Кастанеда", 1);
        BookOverviewVo two = new BookOverviewVo("Отдельная реальность", "К. Кастанеда", 2);
        assertThat(dao.getBooksOverview()).contains(one, two);
    }
}