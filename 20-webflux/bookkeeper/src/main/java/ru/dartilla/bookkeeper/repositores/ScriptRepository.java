package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.dartilla.bookkeeper.domain.Script;

import java.util.Optional;

public interface ScriptRepository extends MongoRepository<Script, String> {

    Optional<Script> findByAuthorIdAndTitle(String authorId, String title);
}
