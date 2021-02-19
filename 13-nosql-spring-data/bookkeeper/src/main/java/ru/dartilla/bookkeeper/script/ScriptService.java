package ru.dartilla.bookkeeper.script;

import ru.dartilla.bookkeeper.domain.Script;

import java.util.Optional;

public interface ScriptService {

    Optional<Script> findByAuthorIdAndTitle(String authorId, String title);

    Script save(Script script);

    Optional<Script> findById(String id);
}
