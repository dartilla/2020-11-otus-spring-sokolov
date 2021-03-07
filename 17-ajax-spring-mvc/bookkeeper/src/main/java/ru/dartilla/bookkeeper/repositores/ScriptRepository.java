package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dartilla.bookkeeper.domain.Script;

import java.util.Optional;

public interface ScriptRepository extends JpaRepository<Script, Long> {

    Optional<Script> findFirstByAuthorIdAndTitle(Long authorId, String title);
}
