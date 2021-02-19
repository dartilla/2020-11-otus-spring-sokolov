package ru.dartilla.bookkeeper.book;

import ru.dartilla.bookkeeper.book.vo.BookInsertVo;
import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.script.vo.ScriptSearchVo;

import java.util.Collection;

public interface BookService {
    void addBook(BookInsertVo book);
    Collection<BookOverviewVo> getBooksOverview();
    String borrowBook(ScriptSearchVo book);
    void returnBook(String id);
}
