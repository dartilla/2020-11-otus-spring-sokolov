package ru.dartilla.bookkeeper.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dartilla.bookkeeper.domain.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreService {

    Mono<Genre> findGenreByName(String genreName);

    Flux<Genre> findGenreByNames(Collection<String> name);

    Flux<Genre> getGenres();
}
