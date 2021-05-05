package ru.dartilla.bookkeeper.security;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.dartilla.bookkeeper.domain.Authority;
import ru.dartilla.bookkeeper.domain.User;
import ru.dartilla.bookkeeper.repositores.AuthorityRepository;
import ru.dartilla.bookkeeper.repositores.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("UserDetailsServiceLocal должен")
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceLocalTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    private UserDetailsServiceLocal userDetailsService;

    @BeforeEach
    private void setUp() {
        userDetailsService = new UserDetailsServiceLocal(userRepository, authorityRepository);
    }

    @DisplayName("грузить пользователя по его логину")
    @Test
    public void shouldLoadUserByUsername() {
        User user = new User(22L, "dLogin", "dPass", true);
        UserDetails expectedUserDetail = new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(), user.isEnabled(), user.isEnabled(), user.isEnabled(), user.isEnabled(),
                List.of(new SimpleGrantedAuthority("ROLE_MANAGER")));
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        when(authorityRepository.findByLogin(user.getLogin())).thenReturn(List.of(new Authority(user.getLogin(), "MANAGER")));

        Assertions.assertThat(userDetailsService.loadUserByUsername(user.getLogin())).isEqualTo(expectedUserDetail);
        verify(userRepository, times(1)).findByLogin(user.getLogin());
    }
}