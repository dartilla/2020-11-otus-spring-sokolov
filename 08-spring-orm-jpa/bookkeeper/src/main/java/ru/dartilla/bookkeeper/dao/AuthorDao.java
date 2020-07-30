package ru.dartilla.bookkeeper.dao;

import ru.dartilla.bookkeeper.domain.Author;

import java.util.Optional;

public interface AuthorDao {

    long insert(Author author);
    Author getById(Long id);
    Optional<Author> findByName(String firstName);
}
