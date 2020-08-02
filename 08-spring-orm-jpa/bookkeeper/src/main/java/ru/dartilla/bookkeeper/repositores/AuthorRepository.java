package ru.dartilla.bookkeeper.repositores;

import ru.dartilla.bookkeeper.domain.Author;

import java.util.Optional;

public interface AuthorRepository {
    Author save(Author author);
    Author getById(Long id);
    Optional<Author> findByName(String name);
}
