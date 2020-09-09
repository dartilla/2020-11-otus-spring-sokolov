package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dartilla.bookkeeper.domain.Author;

import java.util.Optional;


public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByName(String name);
}
