package ru.dartilla.bookkeeper.book;

import ru.dartilla.bookkeeper.book.vo.BookInsertVo;
import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.book.vo.BookSearchVo;

import java.util.Collection;
import java.util.List;

public interface BookService {
    void addBook(BookInsertVo book);
    Collection<BookOverviewVo> getBooksOverview();
    Long borrowBook(BookSearchVo book);
    void returnBook(Long id);
}
