package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.dartilla.bookkeeper.domain.Genre;

import java.util.Arrays;

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
        assertThat(genreRepository.findById(expected.getId())).get().isEqualToComparingFieldByField(expected);
    }

    @DisplayName("возвращать empty при попытке получить жанр по несуществующему идентификатору")
    @Test
    void shouldFindEmptyByNotExistentId() {
        Genre expected = new Genre("199", "Мистика");
        assertThat(genreRepository.findById(expected.getId())).isEmpty();
    }

    @DisplayName("возвращать все жанры")
    @Test
    void shouldGetAll() {
        assertThat(genreRepository.findAll()).isNotEmpty();
    }

    @DisplayName("находить жанр по имени")
    @Test
    void shouldFindByName() {
        Genre expected = new Genre("1", "Мистика");
        assertThat(genreRepository.findByName(expected.getName()).get()).isEqualToComparingOnlyGivenFields(expected, "id", "name");
    }

    @DisplayName("находить находить пусто по несуществующему имени")
    @Test
    void shouldFindEmptyByNotExistentName() {
        Genre expected = new Genre("1", "Мистика222");
        assertThat(genreRepository.findByName(expected.getName())).isEmpty();
    }

    @DisplayName("находить по списку имен")
    @Test
    public void shouldFindByNameIn() {
        assertThat(genreRepository.findByNameIn(Arrays.asList("Мистика", "Детектив")).size()).isEqualTo(2);
    }
}