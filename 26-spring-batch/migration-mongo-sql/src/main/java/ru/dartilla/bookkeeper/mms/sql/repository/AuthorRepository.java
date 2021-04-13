package ru.dartilla.bookkeeper.mms.sql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dartilla.bookkeeper.mms.sql.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author getByMongoId(String mongoId);
}
