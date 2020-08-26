package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.dartilla.bookkeeper.config.DataSourceBeanConfig;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Book;
import ru.dartilla.bookkeeper.domain.Script;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA репозиторий для работы с экземплярами книги должен")
@DataJpaTest
@Import({BookRepositoryJpa.class, DataSourceBeanConfig.class})
class BookRepositoryJpaTest {

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("возвращать экземпляры книги")
    @Test
    public void shouldGetAll() {
        List<Book> books = bookRepository.getAll();
        assertThat(books).isNotEmpty();
    }

    @DisplayName("находить в экземпляр книги в по рукописи")
    @Test
    public void shouldFindInStorageByScript() {
        Script script = new Script(1L, "Учение дона Хуана", null, null, null);
        Book expected = bookRepository.findInStorageByScript(script.getId()).get();
        assertThat(expected.isInStorage()).isTrue();
        assertThat(expected.getScript()).isEqualToComparingOnlyGivenFields(script, "id", "title");
    }

    @DisplayName("находить экземпляр книги по id")
    @Test
    public void shouldFindById() {
        Book book = bookRepository.findById(2L).get();
        assertThat(book).isNotNull();
        assertThat(book.isInStorage()).isFalse();
    }

    @DisplayName("сохраняет экзэмпляр книги")
    @Test
    public void shouldSave() {
        Script script = new Script(99L, "Эхо в Горах", new Author(99L, "Неизвестных О"), null, null);
        Book book = new Book(null, true, script);
        bookRepository.save(book);
        Book actual = bookRepository.findById(book.getId()).get();
        assertThat(actual.isInStorage()).isEqualTo(book.isInStorage());
        assertThat(actual.getScript().getTitle()).isEqualTo(script.getTitle());
    }
}