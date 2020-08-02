package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.dartilla.bookkeeper.config.DataSourceBeanConfig;
import ru.dartilla.bookkeeper.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA репозиторий для работы с жанрами должен")
@DataJpaTest
@Import({GenreRepositoryJpa.class, DataSourceBeanConfig.class})
class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepositoryJpa genreRepository;

    @DisplayName("получить жанр по идентификатору")
    @Test
    void shouldGetById() {
        Genre expected = new Genre(1L, "Мистика");
        assertThat(genreRepository.getById(expected.getId())).isEqualToComparingFieldByField(expected);
    }

    @DisplayName("возвращать null при попытке получить жанр по несуществующему идентификатору")
    @Test
    void shouldGetNullByNotExistentId() {
        Genre expected = new Genre(199L, "Мистика");
        assertThat(genreRepository.getById(expected.getId())).isNull();
    }

    @DisplayName("возвращать все жанры")
    @Test
    void shouldGetAll() {
        assertThat(genreRepository.getAll()).isNotEmpty();
    }

    @DisplayName("находить жанр по имени")
    @Test
    void shouldFindByName() {
        Genre expected = new Genre(1L, "Мистика");
        assertThat(genreRepository.findByName(expected.getName()).get()).isEqualToComparingFieldByField(expected);
    }

    @DisplayName("находить находить пусто по несуществующему имени")
    @Test
    void shouldFindEmptyByNotExistentName() {
        Genre expected = new Genre(1L, "Мистика222");
        assertThat(genreRepository.findByName(expected.getName())).isEmpty();
    }
}