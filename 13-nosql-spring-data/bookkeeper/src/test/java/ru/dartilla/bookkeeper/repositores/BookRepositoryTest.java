package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Book;
import ru.dartilla.bookkeeper.domain.Script;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("репозиторий для работы с экземплярами книги должен")
@DataMongoTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("возвращать экземпляры книги")
    @Test
    public void shouldFindAll() {
        List<Book> books = bookRepository.findAll();
        assertThat(books).isNotEmpty();
    }

    @DisplayName("находить в экземпляр книги по рукописи")
    @Test
    public void shouldFindInStorageByScript() {
        Script script = new Script("1", "Учение дона Хуана", null, null);
        Book expected = bookRepository.findFirstByScriptAndInStorageTrue(script.getId()).get();
        assertThat(expected.isInStorage()).isTrue();
        assertThat(expected.getScript()).isEqualToComparingOnlyGivenFields(script, "id", "title");
    }

    @DisplayName("находить экземпляр книги по id")
    @Test
    public void shouldFindById() {
        Book book = bookRepository.findById("1").get();
        assertThat(book).isNotNull();
        assertThat(book.isInStorage()).isFalse();
    }

    @DisplayName("сохраняет экзэмпляр книги")
    @Test
    public void shouldSave() {
        Script script = new Script("99", "Эхо в Горах", new Author("99", "Неизвестных О"), null);
        Book book = new Book(null, true, script);
        bookRepository.save(book);
        mongoTemplate.save(book.getScript());
        Book actual = bookRepository.findById(book.getId()).get();
        assertThat(actual.isInStorage()).isEqualTo(book.isInStorage());
        assertThat(actual.getScript().getTitle()).isEqualTo(script.getTitle());
    }
}