package ru.dartilla.bookkeeper.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dao для работы с жанрами должно")
@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {

    @Autowired
    private GenreDao dao;

    @DisplayName("получать жанр по идентификатору")
    @Test
    void shouldGetById() {
        assertThat(dao.getById(1L)).hasFieldOrPropertyWithValue("id", 1L);
    }

    @DisplayName("находить жанр по наименованию")
    @Test
    void shouldFindByName() {
        assertThat(dao.findByName("Детектив").get()).hasFieldOrPropertyWithValue("name", "Детектив");
    }

    @DisplayName("получать жанры книги")
    @Test
    void getAll() {
        assertThat(dao.getAll()).isNotEmpty();
    }
}