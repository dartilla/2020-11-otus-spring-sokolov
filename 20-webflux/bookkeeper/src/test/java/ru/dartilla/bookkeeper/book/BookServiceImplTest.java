package ru.dartilla.bookkeeper.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.bookkeeper.script.vo.ScriptDataVo;
import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.script.ScriptService;
import ru.dartilla.bookkeeper.script.vo.ScriptSearchVo;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Book;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.exception.AuthorNotFoundException;
import ru.dartilla.bookkeeper.exception.AvailableBookIsNotFound;
import ru.dartilla.bookkeeper.exception.BookNotFoundException;
import ru.dartilla.bookkeeper.repositores.BookRepository;
import ru.dartilla.bookkeeper.service.AuthorService;
import ru.dartilla.bookkeeper.service.GenreService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Сервис работы с книгами должен")
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private ScriptService scriptService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private GenreService genreService;

    private BookServiceImpl bookService;

    @BeforeEach
    private void setUp() {
        bookService = new BookServiceImpl(bookRepository, authorService, scriptService);
    }

    @DisplayName("добавлять книгу")
    @Test
    public void shouldAddBook() {
        ScriptDataVo expected = new ScriptDataVo(null, "Книга 1", "Тютчев А.", Set.of("Фантастика"));
        Author author = new Author(22L, expected.getAuthorName());
        when(scriptService.acquireScript(any())).thenReturn(new Script(99L, expected.getTitle(), author,
                Set.of(new Genre(22L, "Фантастика"))));
        bookService.addBook(expected);
        verify(bookRepository, times(1)).save(any());
    }

    @DisplayName("возвращает статистику по книгам")
    @Test
    public void shouldGetBooksOverview() {
        Script newWorld = new Script(1L, "Новый Мир", new Author(1L, "Неизвестных О."), Set.of(new Genre(1L, "Роман")));
        Script oldWorld = new Script(2L, "Старый Мир", new Author(1L, "Ренуар А."), Set.of(new Genre(2L, "Проза")));
        when(bookRepository.findAll()).thenReturn(Arrays.asList(
                new Book(1L, true, newWorld),
                new Book(2L, false, newWorld),
                new Book(3L, true, newWorld),
                new Book(4L, true, oldWorld)));
        Collection<BookOverviewVo> booksOverviews = bookService.getBooksOverview();
        verify(bookRepository, times(1)).findAll();
        assertThat(booksOverviews).containsExactlyInAnyOrder(
                new BookOverviewVo(newWorld.getId(), newWorld.getTitle(), newWorld.getAuthor().getName(), 2,
                        newWorld.getGenres().stream().map(Genre::getName).collect(toSet())),
                new BookOverviewVo(oldWorld.getId(), oldWorld.getTitle(), oldWorld.getAuthor().getName(), 1,
                        oldWorld.getGenres().stream().map(Genre::getName).collect(toSet())));
    }

    @DisplayName("кидает исключение, если не нашлась книга во время возврата")
    @Test
    public void shouldThrowWhenNotFoundReturnBook() {
        Long id = 22L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.returnBook(22L)).isInstanceOf(BookNotFoundException.class);
    }

    @DisplayName("позволяет вернуть книгу")
    @Test
    public void shouldReturnBook() {
        Script script = new Script(2L, "новая книга", new Author(1L, null), Set.of(new Genre(1L, null)));
        Book book = new Book(1L, false, script);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        bookService.returnBook(book.getId());
        assertThat(book.isInStorage()).isTrue();
    }

    @DisplayName("кидает исключение при ненайденом авторе при попытке взять книгу")
    @Test
    public void shouldThrowAuthorExceptionBorrowBook() {
        ScriptSearchVo bookToSearch = new ScriptSearchVo("Новая", "Автор");
        assertThatThrownBy(() -> bookService.borrowBook(bookToSearch)).isInstanceOf(AuthorNotFoundException.class);
    }

    @DisplayName("кидает исключение при ненайденом книге при попытке взять книгу")
    @Test
    public void shouldThrowBookNotFoundExceptionBorrowBook() {
        ScriptSearchVo bookToSearch = new ScriptSearchVo("Новая", "Автор");
        Author author = new Author(22L, bookToSearch.getAuthorName());
        when(authorService.findAuthor(bookToSearch.getAuthorName())).thenReturn(Optional.of(author));
        when(scriptService.findByAuthorIdAndTitle(any(), any())).thenReturn(Optional.of(new Script(null, null, null, null)));
        assertThatThrownBy(() -> bookService.borrowBook(bookToSearch)).isInstanceOf(AvailableBookIsNotFound.class);
    }

    @DisplayName("позволяет взять книгу")
    @Test
    public void shouldBorrowBook() {
        ScriptSearchVo bookToSearch = new ScriptSearchVo("Новая", "Автор");
        Author author = new Author(22L, bookToSearch.getAuthorName());
        when(authorService.findAuthor(bookToSearch.getAuthorName())).thenReturn(Optional.of(author));
        Script script = new Script(11L, bookToSearch.getTitle(), author, Set.of(new Genre(22L, null)));
        Book book = new Book(1L, true, script);
        when(scriptService.findByAuthorIdAndTitle(author.getId(), bookToSearch.getTitle()))
                .thenReturn(Optional.of(script));
        when(bookRepository.findInStorageByScript(script.getId())).thenReturn(Optional.of(book));
        bookService.borrowBook(bookToSearch);
        assertThat(book.isInStorage()).isFalse();
    }
}
