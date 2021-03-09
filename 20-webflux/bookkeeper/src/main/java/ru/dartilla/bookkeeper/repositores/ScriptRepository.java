package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.dartilla.bookkeeper.domain.Script;

public interface ScriptRepository extends ReactiveMongoRepository<Script, String> {

    Mono<Script> findByAuthorIdAndTitle(String authorId, String title);

    @DeleteQuery
    Mono<Void> deleteById(String id);
}
