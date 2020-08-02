package ru.dartilla.bookkeeper.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.bookkeeper.book.vo.BookInsertVo;
import ru.dartilla.bookkeeper.book.vo.BookSearchVo;
import ru.dartilla.bookkeeper.dao.BookDao;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Book;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.exception.AuthorNotFoundException;
import ru.dartilla.bookkeeper.exception.AvailableBookIsNotFound;
import ru.dartilla.bookkeeper.exception.BookNotFoundException;
import ru.dartilla.bookkeeper.service.AuthorService;
import ru.dartilla.bookkeeper.service.GenreService;

import java.util.Collections;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Сервис работы с книгами должен")
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookDao bookDao;

    @Mock
    private AuthorService authorService;

    @Mock
    private GenreService genreService;

    private BookServiceImpl bookService;

    @BeforeEach
    private void setUp() {
        bookService = new BookServiceImpl(bookDao, authorService, genreService);
    }

    @DisplayName("добавлять книгу")
    @Test
    public void shouldAddBook() {
        BookInsertVo expected = new BookInsertVo("Книга 1", "Тютчев А.", "Фантастика");
        Author author = new Author(22L, expected.getAuthorName());
        when(authorService.acquireAuthor(any())).thenReturn(author);
        when(genreService.findGenreByName(any())).thenReturn(Optional.of(new Genre(22L, "Роман")));
        bookService.addBook(expected);
        verify(bookDao, times(1)).insert(any());
    }

    @DisplayName("возвращает статистику по книгам")
    @Test
    public void shouldGetBooksOverview() {
        bookService.getBooksOverview();
        verify(bookDao, times(1)).getBooksOverview();
    }

    @DisplayName("кидает исключение, если не нашлась книга во время возврата")
    @Test
    public void shouldThrowWhenNotFoundReturnBook() {
        Long id = 22L;
        when(bookDao.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.returnBook(22L)).isInstanceOf(BookNotFoundException.class);
    }

    @DisplayName("позволяет вернуть книгу")
    @Test
    public void shouldReturnBook() {
        Book book = new Book(2L, "новая книга", 1L, 1L, false);
        when(bookDao.findById(book.getId())).thenReturn(Optional.of(book));
        bookService.returnBook(book.getId());
        assertThat(book.isInStorage()).isTrue();
        verify(bookDao, times(1)).update(book);
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
        Book book = new Book(11L, bookToSearch.getTitle(), author.getId(), 22L, true);
        when(bookDao.findByAuthorIdAndTitleAndStorage(author.getId(), bookToSearch.getTitle(), true))
                .thenReturn(singletonList(book));
        bookService.borrowBook(bookToSearch);
        assertThat(book.isInStorage()).isFalse();
        verify(bookDao, times(1)).update(book);
    }
}