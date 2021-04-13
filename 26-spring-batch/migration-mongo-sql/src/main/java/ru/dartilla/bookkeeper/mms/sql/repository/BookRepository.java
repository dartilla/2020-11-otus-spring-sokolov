package ru.dartilla.bookkeeper.mms.sql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dartilla.bookkeeper.mms.sql.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
