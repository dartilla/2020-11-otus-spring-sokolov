package ru.dartilla.bookkeeper.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.bookkeeper.book.vo.BookInsertVo;
import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.book.vo.BookSearchVo;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Book;
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

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Сервис работы с книгами должен")
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private GenreService genreService;

    private BookServiceImpl bookService;

    @BeforeEach
    private void setUp() {
        bookService = new BookServiceImpl(bookRepository, authorService, genreService);
    }

    @DisplayName("добавлять книгу")
    @Test
    public void shouldAddBook() {
        BookInsertVo expected = new BookInsertVo("Книга 1", "Тютчев А.", Set.of("Фантастика"));
        Author author = new Author(22L, expected.getAuthorName());
        when(authorService.acquireAuthor(any())).thenReturn(author);
        when(genreService.findGenreByNames(any())).thenReturn(Arrays.asList(new Genre(22L, "Фантастика")));
        bookService.addBook(expected);
        verify(bookRepository, times(1)).save(any());
    }

    @DisplayName("возвращает статистику по книгам")
    @Test
    public void shouldGetBooksOverview() {
        when(bookRepository.getAll()).thenReturn(Arrays.asList(
                new Book(1L, "Новый Мир", true, new Author(1L, "Неизвестных О."), Set.of(new Genre(1L, "Роман"))),
                new Book(2L, "Новый Мир", false, new Author(1L, "Неизвестных О."), Set.of(new Genre(1L, "Роман"))),
                new Book(3L, "Новый Мир", true, new Author(1L, "Неизвестных О."), Set.of(new Genre(1L, "Роман"))),
                new Book(4L, "Старый Мир", true, new Author(1L, "Ренуар А."), Set.of(new Genre(2L, "Проза")))
        ));
        Collection<BookOverviewVo> booksOverviews = bookService.getBooksOverview();
        verify(bookRepository, times(1)).getAll();
        assertThat(booksOverviews).containsExactlyInAnyOrder(new BookOverviewVo("Новый Мир", "Неизвестных О.", 2, Set.of("Роман")),
                new BookOverviewVo("Старый Мир", "Ренуар А.", 1, Set.of("Проза")));

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
        Book book = new Book(2L, "новая книга", false, new Author(1L, null), Set.of(new Genre(1L, null)));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        bookService.returnBook(book.getId());
        assertThat(book.isInStorage()).isTrue();
    }

    @DisplayName("кидает исключение при ненайденом авторе при попытке взять книгу")
    @Test
    public void shouldThrowAuthorExceptionBorrowBook() {
        BookSearchVo bookToSearch = new BookSearchVo("Новая", "Автор");
        assertThatThrownBy(() -> bookService.borrowBook(bookToSearch)).isInstanceOf(AuthorNotFoundException.class);
    }

    @DisplayName("кидает исключение при ненайденом книге при попытке взять книгу")
    @Test
    public void shouldThrowBookNotFoundExceptionBorrowBook() {
        BookSearchVo bookToSearch = new BookSearchVo("Новая", "Автор");
        Author author = new Author(22L, bookToSearch.getAuthorName());
        when(authorService.findAuthor(bookToSearch.getAuthorName())).thenReturn(Optional.of(author));
        assertThatThrownBy(() -> bookService.borrowBook(bookToSearch)).isInstanceOf(AvailableBookIsNotFound.class);
    }

    @DisplayName("позволяет взять книгу")
    @Test
    public void shouldBorrowBook() {
        BookSearchVo bookToSearch = new BookSearchVo("Новая", "Автор");
        Author author = new Author(22L, bookToSearch.getAuthorName());
        when(authorService.findAuthor(bookToSearch.getAuthorName())).thenReturn(Optional.of(author));
        Book book = new Book(11L, bookToSearch.getTitle(), true, author, Set.of(new Genre(22L, null)));
        when(bookRepository.findByAuthorIdAndTitleAndStorage(author.getId(), bookToSearch.getTitle(), true))
                .thenReturn(singletonList(book));
        bookService.borrowBook(bookToSearch);
        assertThat(book.isInStorage()).isFalse();
    }
}
