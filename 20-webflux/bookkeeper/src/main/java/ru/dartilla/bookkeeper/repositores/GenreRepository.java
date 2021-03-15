package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dartilla.bookkeeper.domain.Genre;

import java.util.Collection;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

    Mono<Genre> findByName(String name);

    Flux<Genre> findByNameIn(Collection<String> name);
}
