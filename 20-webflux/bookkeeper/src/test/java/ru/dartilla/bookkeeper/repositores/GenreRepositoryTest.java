package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;
import ru.dartilla.bookkeeper.domain.Genre;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("репозиторий для работы с жанрами должен")
@DataMongoTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("получить жанр по идентификатору")
    @Test
    void shouldGetById() {
        Genre expected = new Genre("1", "Мистика");
        assertThat(genreRepository.findById(expected.getId()).block())
                .isEqualToComparingFieldByField(expected);
    }

    @DisplayName("возвращать empty при попытке получить жанр по несуществующему идентификатору")
    @Test
    void shouldFindEmptyByNotExistentId() {
        Genre expected = new Genre("199", "Мистика");
        assertThat(genreRepository.findById(expected.getId()).block()).isNull();
    }

    @DisplayName("возвращать все жанры")
    @Test
    void shouldGetAll() {
        StepVerifier.create(genreRepository.findAll())
                .expectNextCount(3)
                .verifyComplete();
    }

    @DisplayName("находить жанр по имени")
    @Test
    void shouldFindByName() {
        Genre expected = new Genre("1", "Мистика");
        assertThat(genreRepository.findByName(expected.getName()).block()).isEqualToComparingOnlyGivenFields(expected, "id", "name");
    }

    @DisplayName("находить находить пусто по несуществующему имени")
    @Test
    void shouldFindEmptyByNotExistentName() {
        Genre expected = new Genre("1", "Мистика222");
        assertThat(genreRepository.findByName(expected.getName()).block()).isNull();
    }

    @DisplayName("находить по списку имен")
    @Test
    public void shouldFindByNameIn() {
        StepVerifier.create(genreRepository.findByNameIn(Arrays.asList("Мистика", "Детектив")))
                .expectNextCount(2)
                .verifyComplete();
    }
}