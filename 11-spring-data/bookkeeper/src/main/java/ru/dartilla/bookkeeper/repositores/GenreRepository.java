package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dartilla.bookkeeper.domain.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String name);

    List<Genre> findByNameIn(Collection<String> name);
}
