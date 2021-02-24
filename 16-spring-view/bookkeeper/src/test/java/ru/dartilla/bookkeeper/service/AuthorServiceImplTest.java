package ru.dartilla.bookkeeper.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.repositores.AuthorRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Service работы с авторами должен")
@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    private AuthorServiceImpl authorService;

    @BeforeEach
    private void setUp() {
        authorService = new AuthorServiceImpl(authorRepository);
    }

    @DisplayName("возвращать созданного автора с новым идентификатором")
    @Test
    void shouldReturnInsertedAuthorWithNewId() {
        long newId = 77L;
        when(authorRepository.save(any())).thenAnswer(ctx -> {
            Author argument = ctx.getArgument(0);
            argument.setId(newId);
            return argument;
        });
        Author expected = new Author(null, "Пупкин В. В.");
        Author actual = authorService.insertAuthor(expected);
        assertThat(actual.getId()).isEqualTo(newId);
        assertThat(actual).isEqualToComparingOnlyGivenFields(expected, "name");
    }

    @DisplayName("возвращать существующего автора по ФИО")
    @Test
    void shouldGetExistingAuthorByFIO() {
        Author expected = new Author(77L, "Пупкин В. В.");
        when(authorRepository.findByName(expected.getName())).thenReturn(Optional.of(expected));
        Author actual = authorService.acquireAuthor(expected.getName());
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("обнаруживать автора (несуществующего создавать)")
    @Test
    void shouldAcquireNotExistingAuthor() {
        Author expected = new Author(null, "Пупкин В. В.");
        long newId = 77L;
        when(authorRepository.save(any())).thenAnswer(ctx -> {
            Author argument = ctx.getArgument(0);
            argument.setId(newId);
            return argument;
        });
        Author actual = authorService.acquireAuthor(expected.getName());
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "id");
    }

    @DisplayName("обнаруживть автора (существующего находить)")
    @Test
    void shouldAcquireExistingAuthor() {
        Author expected = new Author(77L, "Пупкин В. В.");
        when(authorRepository.findByName(expected.getName())).thenReturn(Optional.of(expected));
        Author actual = authorService.acquireAuthor(expected.getName());
        assertThat(actual).isEqualToComparingFieldByField(expected);
        verify(authorRepository, times(0)).save(any());
    }
}