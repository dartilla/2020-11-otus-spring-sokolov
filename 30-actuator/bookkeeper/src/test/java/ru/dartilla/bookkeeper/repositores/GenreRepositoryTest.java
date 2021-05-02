package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.dartilla.bookkeeper.config.DataSourceBeanConfig;
import ru.dartilla.bookkeeper.domain.Genre;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("репозиторий для работы с жанрами должен")
@DataJpaTest
@Import({DataSourceBeanConfig.class})
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("получить жанр по идентификатору")
    @Test
    void shouldGetById() {
        Genre expected = new Genre(1L, "Мистика");
        assertThat(genreRepository.findById(expected.getId())).get().isEqualToComparingFieldByField(expected);
    }

    @DisplayName("возвращать empty при попытке получить жанр по несуществующему идентификатору")
    @Test
    void shouldFindEmptyByNotExistentId() {
        Genre expected = new Genre(199L, "Мистика");
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
        Genre expected = new Genre(1L, "Мистика");
        assertThat(genreRepository.findByName(expected.getName()).get()).isEqualToComparingOnlyGivenFields(expected, "id", "name");
    }

    @DisplayName("находить находить пусто по несуществующему имени")
    @Test
    void shouldFindEmptyByNotExistentName() {
        Genre expected = new Genre(1L, "Мистика222");
        assertThat(genreRepository.findByName(expected.getName())).isEmpty();
    }

    @DisplayName("находить по списку имен")
    @Test
    public void shouldFindByNameIn() {
        assertThat(genreRepository.findByNameIn(Arrays.asList("Мистика", "Детектив")).size()).isEqualTo(2);
    }
}