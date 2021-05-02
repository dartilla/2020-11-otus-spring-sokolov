package ru.dartilla.bookkeeper.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//Этот тест для генерации зашифрованых паролей для проливок данных
@DisplayName("PasswordEncoder")
class PasswordEncoderTest {

    @DisplayName("должен кодировать пароль")
    @Test
    public void shouldEncodePassword() {
        String encodedPassword = new BCryptPasswordEncoder().encode("manager");
        System.out.println(encodedPassword);
        assertThat(encodedPassword).isNotBlank();
    }
}