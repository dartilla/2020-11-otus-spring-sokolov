package ru.dartilla.bookkeeper.repositores;

import ru.dartilla.bookkeeper.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Book save(Book book);

    void delete(Long id);

    Book getById(Long id);

    Optional<Book> findById(Long id);

    List<Book> getAll();

    List<Book> findByAuthorIdAndTitleAndStorage(Long authorId, String title, boolean isInStorage);
}
