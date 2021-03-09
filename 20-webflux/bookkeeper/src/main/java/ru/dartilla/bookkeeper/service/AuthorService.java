package ru.dartilla.bookkeeper.service;

import reactor.core.publisher.Mono;
import ru.dartilla.bookkeeper.domain.Author;

import java.util.Optional;

public interface AuthorService {

    Mono<Author> insertAuthor(Author author);

    Mono<Author> acquireAuthor(String name);

    Mono<Author> findAuthor(String name);
}
