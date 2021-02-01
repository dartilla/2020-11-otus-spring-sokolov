package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.dartilla.bookkeeper.domain.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String> {

    Optional<Genre> findByName(String name);

    List<Genre> findByNameIn(Collection<String> name);
}
