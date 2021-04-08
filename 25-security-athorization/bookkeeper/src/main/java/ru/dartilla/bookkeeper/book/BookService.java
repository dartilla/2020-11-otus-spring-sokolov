package ru.dartilla.bookkeeper.book;

import ru.dartilla.bookkeeper.script.vo.ScriptDataVo;
import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.script.vo.ScriptSearchVo;

import java.util.Collection;

public interface BookService {
    void addBook(ScriptDataVo book);
    Collection<BookOverviewVo> getBooksOverview();
    Long borrowBook(ScriptSearchVo book);
    void returnBook(Long id);
}
