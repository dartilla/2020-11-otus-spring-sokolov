package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.dartilla.bookkeeper.domain.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

    Mono<Author> findByName(String name);
}
