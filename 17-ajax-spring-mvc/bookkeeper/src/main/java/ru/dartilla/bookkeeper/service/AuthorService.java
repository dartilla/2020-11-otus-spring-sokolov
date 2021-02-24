package ru.dartilla.bookkeeper.service;

import ru.dartilla.bookkeeper.domain.Author;

import java.util.Optional;

public interface AuthorService {

    Author insertAuthor(Author author);

    Author acquireAuthor(String name);

    Optional<Author> findAuthor(String name);
}
