package ru.dartilla.bookkeeper.mms.sql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dartilla.bookkeeper.mms.sql.domain.Genre;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findByMongoIdIn(List<String> mongoId);
}
