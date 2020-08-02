package ru.dartilla.bookkeeper.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dartilla.bookkeeper.book.vo.BookInsertVo;
import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.book.vo.BookSearchVo;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Book;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.exception.*;
import ru.dartilla.bookkeeper.repositores.BookRepository;
import ru.dartilla.bookkeeper.service.AuthorService;
import ru.dartilla.bookkeeper.service.GenreService;

import java.util.*;

import static java.util.stream.Collectors.toSet;

@Service
@AllArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    @Transactional
    public void addBook(BookInsertVo bookInsertVo) throws BookkeeperException {
        Author author = authorService.acquireAuthor(bookInsertVo.getAuthorName());
        Set<Genre> genres = new HashSet<>(genreService.findGenreByNames(bookInsertVo.getGenreNames()));
        Set<String> foundGenreNames = genres.stream().map(Genre::getName).collect(toSet());
        Optional<String> absentGenre = bookInsertVo.getGenreNames().stream()
                .filter(name -> !foundGenreNames.contains(name)).findAny();
        if (absentGenre.isPresent()) {
            throw new GenreNotFoundException(absentGenre.get());
        }
        bookRepository.save(new Book(null, bookInsertVo.getTitle(), true, author, genres));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<BookOverviewVo> getBooksOverview() {
        Map<BookOverviewKey, BookOverviewVo> overviewMap = new TreeMap<>(Comparator
                .comparing(BookOverviewKey::getAuthorName)
                .thenComparing(BookOverviewKey::getTitle));
        for (Book book : bookRepository.getAll()) {
            BookOverviewVo bookOverview = overviewMap.computeIfAbsent(
                    new BookOverviewKey(book.getAuthor().getName(), book.getTitle()),
                    key -> new BookOverviewVo(book.getTitle(), book.getAuthor().getName(), 0,
                            book.getGenres().stream().map(Genre::getName).collect(toSet())));
            if (book.isInStorage()) {
                bookOverview.setAvailableToBorrow(bookOverview.getAvailableToBorrow() + 1);
            }
        }
        return overviewMap.values();
    }

    @Data
    private static class BookOverviewKey {
        private final String authorName;
        private final String title;
    }

    @Override
    @Transactional
    public Long borrowBook(BookSearchVo bookToSearch) throws BookkeeperException {
        Author author = authorService.findAuthor(bookToSearch.getAuthorName())
                .orElseThrow(() -> new AuthorNotFoundException());
        Book book = bookRepository.findByAuthorIdAndTitleAndStorage(author.getId(), bookToSearch.getTitle(), true).stream()
                .findAny().orElseThrow(() -> new AvailableBookIsNotFound());
        book.setInStorage(false);
        return book.getId();
    }

    @Override
    @Transactional
    public void returnBook(Long id) throws BookkeeperException {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException());
        if (book.isInStorage()) {
            throw new BookInStorageException();
        }
        book.setInStorage(true);
    }
}
