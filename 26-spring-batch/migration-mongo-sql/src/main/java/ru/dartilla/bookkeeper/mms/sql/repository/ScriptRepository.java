package ru.dartilla.bookkeeper.mms.sql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dartilla.bookkeeper.mms.sql.domain.Script;

public interface ScriptRepository extends JpaRepository<Script, Long> {

    Script getByMongoId(String mongoId);
}
