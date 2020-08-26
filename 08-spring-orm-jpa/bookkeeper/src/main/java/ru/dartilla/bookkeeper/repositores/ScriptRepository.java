package ru.dartilla.bookkeeper.repositores;

import ru.dartilla.bookkeeper.domain.Script;

import java.util.List;
import java.util.Optional;

public interface ScriptRepository {

    Script save(Script book);

    Script getById(Long id);

    Optional<Script> findById(Long id);

    List<Script> getAll();

    Optional<Script> findByAuthorIdAndTitle(Long authorId, String title);
}
