package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.dartilla.bookkeeper.config.DataSourceBeanConfig;
import ru.dartilla.bookkeeper.domain.Author;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("репозиторий для работы с авторами должен")
@DataJpaTest
@Import({DataSourceBeanConfig.class})
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepo;

    @DisplayName("сохранять автора")
    @Test
    public void shouldInsertAuthor() {
        Author expected = new Author(null, "Флеминг Я.");
        authorRepo.save(expected);
        assertThat(expected.getId()).isNotNull();
        Author actual = authorRepo.findByName(expected.getName()).get();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual).isEqualToComparingOnlyGivenFields(expected, "name");
    }

    @DisplayName("получать автора по идентификатору")
    @Test
    public void shouldGetById() {
        Author expected = new Author(1L, "Кастанеда К.");
        assertThat(authorRepo.findById(expected.getId())).get().isEqualToComparingFieldByField(expected);
    }

    @DisplayName("получать empty вместо автора по несущеcтвующему идентификатору")
    @Test
    public void shouldFindEmptyByWrongId() {
        Author expected = new Author(99L, "Кастанеда К.");
        assertThat(authorRepo.findById(expected.getId())).isEmpty();
    }

    @DisplayName("находить автора по имени")
    @Test
    public void shouldFindByName() {
        Author expected = new Author(1L, "Кастанеда К.");
        assertThat(authorRepo.findByName(expected.getName()).get()).isEqualToComparingFieldByField(expected);
    }

    @DisplayName("находить пустого автора по несуществующему имени")
    @Test
    public void shouldFindEmptyByWrongName() {
        Author expected = new Author(1L, "Флеминг Я.");
        assertThat(authorRepo.findByName(expected.getName())).isEmpty();
    }
}