package ru.dartilla.bookkeeper.dao;

import ru.dartilla.bookkeeper.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    Genre getById(Long id);

    List<Genre> getAll();

    Optional<Genre> findByName(String name);
}
