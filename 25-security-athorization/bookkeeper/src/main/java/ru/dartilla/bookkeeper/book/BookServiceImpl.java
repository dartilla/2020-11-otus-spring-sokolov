package ru.dartilla.bookkeeper.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dartilla.bookkeeper.script.vo.ScriptDataVo;
import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.script.ScriptService;
import ru.dartilla.bookkeeper.script.vo.ScriptSearchVo;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.domain.Book;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.exception.*;
import ru.dartilla.bookkeeper.repositores.BookRepository;
import ru.dartilla.bookkeeper.service.AuthorService;

import java.util.*;

import static java.util.stream.Collectors.toSet;

@Service
@AllArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final ScriptService scriptService;

    @Override
    @Transactional
    public void addBook(ScriptDataVo scriptDataVo) throws BookkeeperException {
        Script script = scriptService.acquireScript(scriptDataVo);
        bookRepository.save(new Book(null, true, script));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<BookOverviewVo> getBooksOverview() {
        Map<BookOverviewKey, BookOverviewVo> overviewMap = new TreeMap<>(Comparator
                .comparing(BookOverviewKey::getAuthorName)
                .thenComparing(BookOverviewKey::getTitle));
        for (Book book : bookRepository.findAll()) {
            Script script = book.getScript();
            BookOverviewVo bookOverview = overviewMap.computeIfAbsent(
                    new BookOverviewKey(script.getAuthor().getName(), script.getTitle()),
                    key -> new BookOverviewVo(script.getId(), script.getTitle(), script.getAuthor().getName(), 0,
                            script.getGenres().stream().map(Genre::getName).collect(toSet())));
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
    public Long borrowBook(ScriptSearchVo bookToSearch) throws BookkeeperException {
        Author author = authorService.findAuthor(bookToSearch.getAuthorName())
                .orElseThrow(() -> new AuthorNotFoundException());
        Script script = scriptService.findByAuthorIdAndTitle(author.getId(), bookToSearch.getTitle())
                .orElseThrow(() -> new ScriptIsNotFoundException());
        Book book = bookRepository.findInStorageByScript(script.getId())
                .orElseThrow(() -> new AvailableBookIsNotFound());
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
