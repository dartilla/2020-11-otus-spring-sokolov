package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.dartilla.bookkeeper.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findFirstByScriptAndInStorageTrue(String scriptId);

    List<Book> findAll();
}
