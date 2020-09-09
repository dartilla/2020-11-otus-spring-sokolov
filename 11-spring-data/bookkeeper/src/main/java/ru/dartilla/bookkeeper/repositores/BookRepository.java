package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.dartilla.bookkeeper.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b where b.inStorage = true and b.script.id = :scriptId")
    Optional<Book> findInStorageByScript(Long scriptId);

    @EntityGraph("bookEntityGraphWithScriptAndAuthor")
    List<Book> findAll();
}
