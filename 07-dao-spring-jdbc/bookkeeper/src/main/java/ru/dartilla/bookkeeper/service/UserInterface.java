package ru.dartilla.bookkeeper.service;

import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.book.vo.BookInsertVo;
import ru.dartilla.bookkeeper.book.vo.BookSearchVo;
import ru.dartilla.bookkeeper.domain.Genre;

import java.util.Collection;

public interface UserInterface {

    BookInsertVo readBookInsertVo();

    BookSearchVo readBookSearchVo();

    void printBooks(Collection<BookOverviewVo> books);

    void printException(Exception ex);

    void printBookBorrowed(Long id);

    Long readBookId();

    void printBookReturned();

    void printGenres(Collection<Genre> genres);
}
