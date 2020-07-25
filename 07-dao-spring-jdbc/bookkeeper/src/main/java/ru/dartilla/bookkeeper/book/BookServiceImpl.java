package ru.dartilla.bookkeeper.book;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dartilla.bookkeeper.book.vo.BookInsertVo;
import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.book.vo.BookSearchVo;
import ru.dartilla.bookkeeper.dao.BookDao;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Book;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.exception.*;
import ru.dartilla.bookkeeper.service.AuthorService;
import ru.dartilla.bookkeeper.service.GenreService;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    public void addBook(BookInsertVo bookInsertVo) throws BookkeeperException {
        Author author = authorService.acquireAuthor(bookInsertVo.getAuthorName());
        Genre genre = genreService.findGenreByName(bookInsertVo.getGenreName())
                .orElseThrow(() -> new GenreNotFoundException());
        bookDao.insert(new Book(null, bookInsertVo.getTitle(), author.getId(), genre.getId(), true));
    }

    @Override
    public List<BookOverviewVo> getBooksOverview() {
        return bookDao.getBooksOverview();
    }

    @Override
    public Long borrowBook(BookSearchVo bookToSearch) throws BookkeeperException {
        Author author = authorService.findAuthor(bookToSearch.getAuthorName())
                .orElseThrow(() -> new AuthorNotFoundException());
        Book book = bookDao.findByAuthorIdAndTitleAndStorage(author.getId(), bookToSearch.getTitle(), true).stream()
                .findAny().orElseThrow(() -> new AvailableBookIsNotFound());
        book.setInStorage(false);
        bookDao.update(book);
        return book.getId();
    }

    @Override
    public void returnBook(Long id) throws BookkeeperException {
        Book book = bookDao.findById(id).orElseThrow(() -> new BookNotFoundException());
        book.setInStorage(true);
        bookDao.update(book);
    }
}
