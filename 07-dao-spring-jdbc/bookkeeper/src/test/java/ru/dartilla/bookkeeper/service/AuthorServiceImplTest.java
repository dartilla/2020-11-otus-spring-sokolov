package ru.dartilla.bookkeeper.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.bookkeeper.dao.AuthorDao;
import ru.dartilla.bookkeeper.domain.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Service работы с авторами должен")
@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorDao authorDao;

    private AuthorServiceImpl authorService;

    @BeforeEach
    private void setUp() {
        authorService = new AuthorServiceImpl(authorDao);
    }

    @DisplayName("возвращать созданного автора с новым идентификатором")
    @Test
    void shouldReturnInsertedAuthorWithNewId() {
        long newId = 77L;
        when(authorDao.insert(any())).thenReturn(newId);
        Author expected = new Author(null, "Пупкин В. В.");
        Author actual = authorService.insertAuthor(expected);
        assertThat(actual.getId()).isEqualTo(newId);
        assertThat(actual).isEqualToComparingOnlyGivenFields(expected, "name");
    }

    @DisplayName("возвращать существующего автора по ФИО")
    @Test
    void shouldGetExistingAuthorByFIO() {
        Author expected = new Author(77L, "Пупкин В. В.");
        when(authorDao.findByName(expected.getName())).thenReturn(Optional.of(expected));
        Author actual = authorService.acquireAuthor(expected.getName());
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("создавать (несуществующего) и возвращать автора по ФИО")
    @Test
    void shouldCreateAndGetExistingAuthorByFIO() {
        Author expected = new Author(77L, "Пупкин В. В.");
        when(authorDao.insert(any())).thenReturn(expected.getId());
        Author actual = authorService.acquireAuthor(expected.getName());
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }
}