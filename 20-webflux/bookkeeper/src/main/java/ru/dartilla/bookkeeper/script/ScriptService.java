package ru.dartilla.bookkeeper.script;

import ru.dartilla.bookkeeper.script.vo.ScriptDataVo;
import ru.dartilla.bookkeeper.domain.Script;

import java.util.List;
import java.util.Optional;

public interface ScriptService {

    Optional<Script> findByAuthorIdAndTitle(String authorId, String title);

    Script save(Script script);

    Optional<Script> findById(String id);

    Script acquireScript(ScriptDataVo scriptDataVo);

    List<Script> findAll();

    void deleteById(String id);
}
