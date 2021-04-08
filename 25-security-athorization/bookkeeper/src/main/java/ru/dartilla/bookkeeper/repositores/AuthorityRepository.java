package ru.dartilla.bookkeeper.repositores;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dartilla.bookkeeper.domain.Authority;
import ru.dartilla.bookkeeper.domain.id.AuthorityId;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, AuthorityId> {

    List<Authority> findByLogin(String login);
}
