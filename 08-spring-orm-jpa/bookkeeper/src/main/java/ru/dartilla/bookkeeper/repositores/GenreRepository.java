package ru.dartilla.bookkeeper.repositores;

import ru.dartilla.bookkeeper.domain.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository {

    Genre getById(Long id);

    List<Genre> getAll();

    Optional<Genre> findByName(String name);

    List<Genre> findByNames(Collection<String> name);
}
