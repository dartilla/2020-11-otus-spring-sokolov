package ru.dartilla.bookkeeper.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.dartilla.bookkeeper.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с авторами должно в БД")
@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {

    @Autowired
    AuthorDaoJdbc dao;

    @DisplayName("сохранять автора")
    @Test
    void shouldInsertAuthor() {
        Author expected = new Author(null, "Ф. Достоевский");
        long newId = dao.insert(expected);
        assertThat(newId).isGreaterThan(0);
        assertThat(dao.getById(newId)).isEqualToComparingOnlyGivenFields(expected, "name");
    }

    @DisplayName("искать автора по идентификатору")
    @Test
    void shouldGetById() {
        assertThat(dao.getById(1L).getName()).isEqualTo("Кастанеда К.");
    }

    @DisplayName("искать автора по имени ")
    @Test
    void shouldFindByName() {
        Author expected = new Author(null, "Кастанеда К.");
        assertThat(dao.findByName(expected.getName()).get())
                .isEqualToComparingOnlyGivenFields(expected, "name");
    }

    @DisplayName("вернуть без ошибок Optional для автора не найденого по имени")
    @Test
    void shouldFindEmptyOptionalByNotExistName() {
        Author expected = new Author(null, "Кастанеда К.22") ;
        assertThat(dao.findByName(expected.getName()).isEmpty()).isTrue();
    }

}