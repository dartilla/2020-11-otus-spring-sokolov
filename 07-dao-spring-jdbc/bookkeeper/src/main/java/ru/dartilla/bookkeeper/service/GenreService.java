package ru.dartilla.bookkeeper.service;

import ru.dartilla.bookkeeper.domain.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreService {

    Optional<Genre> findGenreByName(String genreName);

    Collection<Genre> getGenres();
}
