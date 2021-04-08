package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.dartilla.bookkeeper.config.DataSourceBeanConfig;
import ru.dartilla.bookkeeper.domain.Authority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий для работы с авторизацией должен")
@DataJpaTest
@Import({DataSourceBeanConfig.class})
class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;

    @DisplayName("находить авторизации по логину")
    @Test
    public void shouldFindByLogin() {
        String login = "donald";
        assertThat(authorityRepository.findByLogin(login)).containsAll(List.of(new Authority(login, "MANAGER"),
                new Authority(login, "USER")));
    }
}