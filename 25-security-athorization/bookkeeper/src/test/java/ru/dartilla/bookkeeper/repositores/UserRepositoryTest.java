package ru.dartilla.bookkeeper.repositores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.dartilla.bookkeeper.config.DataSourceBeanConfig;
import ru.dartilla.bookkeeper.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий для работы с пользователями должен")
@DataJpaTest
@Import({DataSourceBeanConfig.class})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("находить пользователя по его логину")
    @Test
    public void shouldFindByLogin() {
        User expectedUser = new User(1L, "donald", "donald", true);
        assertThat(userRepository.findByLogin(expectedUser.getLogin()).get()).isEqualTo(expectedUser);
    }
}