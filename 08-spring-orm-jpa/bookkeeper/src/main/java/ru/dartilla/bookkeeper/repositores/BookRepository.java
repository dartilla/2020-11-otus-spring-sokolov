package ru.dartilla.bookkeeper.repositores;

import ru.dartilla.bookkeeper.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);
    Optional<Book> findById(Long id);
    List<Book> getAll();
    Optional<Book> findInStorageByScript(Long scriptId);
}
