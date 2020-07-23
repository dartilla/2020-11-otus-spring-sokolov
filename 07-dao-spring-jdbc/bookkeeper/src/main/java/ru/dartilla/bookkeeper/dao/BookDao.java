package ru.dartilla.bookkeeper.dao;

import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    void insert(Book book);
    void update(Book book);
    void delete(Long id);
    Book getById(Long id);

    Optional<Book> findById(Long id);

    List<Book> findByAuthorIdAndTitleAndStorage(Long authorId, String title, boolean isInStorage);

    List<Book> getAll();

    List<BookOverviewVo> getBooksOverview();
}
