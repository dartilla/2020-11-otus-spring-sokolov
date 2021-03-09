package ru.dartilla.bookkeeper.script;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dartilla.bookkeeper.script.vo.ScriptDataVo;
import ru.dartilla.bookkeeper.domain.Script;

import java.util.List;
import java.util.Optional;

public interface ScriptService {

    Mono<Script> findByAuthorIdAndTitle(String authorId, String title);

    Mono<Script> save(Script script);

    Mono<Script> findById(String id);

    Mono<Script> acquireScript(ScriptDataVo scriptDataVo);

    Flux<Script> findAll();

    Mono<Void> deleteById(String id);
}
