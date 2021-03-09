package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;
import ru.dartilla.bookkeeper.domain.Author;


import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@DisplayName("репозиторий для работы с авторами должен")
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepo;

    @DisplayName("сохранять автора")
    @Test
    public void shouldInsertAuthor() {
        Author expected = new Author(null, "Флеминг Я.");

        assertThat(authorRepo.save(expected).block()
                .getId()).isNotNull();
        assertThat(authorRepo.findByName(expected.getName()).block())
                .isEqualToComparingOnlyGivenFields(expected, "name");
    }

    @DisplayName("получать автора по идентификатору")
    @Test
    public void shouldGetById() {
        Author expected = new Author("1", "Кастанеда К.");
        assertThat(authorRepo.findById(expected.getId()).block())
                .isEqualToComparingFieldByField(expected);
    }

    @DisplayName("получать empty вместо автора по несущеcтвующему идентификатору")
    @Test
    public void shouldFindEmptyByWrongId() {
        Author expected = new Author("99", "Кастанеда К.");
        StepVerifier.create(authorRepo.findById(expected.getId())).verifyComplete();
    }

    @DisplayName("находить автора по имени")
    @Test
    public void shouldFindByName() {
        Author expected = new Author("1", "Кастанеда К.");
        StepVerifier.create(authorRepo.findByName(expected.getName()))
                .assertNext(x -> assertThat(x).isEqualToComparingFieldByField(expected))
                .verifyComplete();
    }

    @DisplayName("находить пустого автора по несуществующему имени")
    @Test
    public void shouldFindEmptyByWrongName() {
        Author expected = new Author("1", "Флеминг Я.");
        StepVerifier.create(authorRepo.findByName(expected.getName()))
                .verifyComplete();
    }
}
