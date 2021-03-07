package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.dartilla.bookkeeper.domain.Author;

import java.util.Optional;


public interface AuthorRepository extends MongoRepository<Author, String> {

    Optional<Author> findByName(String name);
}
