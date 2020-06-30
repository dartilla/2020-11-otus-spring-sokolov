package ru.dartilla.examinator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.examinator.config.localization.DefLocaleMessageSource;
import ru.dartilla.examinator.domain.User;


import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    InOut inOut;

    @Mock
    DefLocaleMessageSource messageSource;

    @Test
    public void perform() {
        when(inOut.getIn()).thenReturn(new Scanner(new ByteArrayInputStream("Ivan\nProhorov".getBytes())));
        when(inOut.getOut()).thenReturn(System.out);

        User user = new AuthServiceImpl(inOut, messageSource).authenticate();
        assertThat(user.getName()).isEqualTo("Ivan");
        assertThat(user.getSurname()).isEqualTo("Prohorov");
    }
}