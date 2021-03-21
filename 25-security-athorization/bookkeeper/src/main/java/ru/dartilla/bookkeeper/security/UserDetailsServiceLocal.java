package ru.dartilla.bookkeeper.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.dartilla.bookkeeper.domain.User;
import ru.dartilla.bookkeeper.repositores.UserRepository;

import java.util.Collections;

@RequiredArgsConstructor
public class UserDetailsServiceLocal implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Login is not found: " + username));
        boolean isEnabled = user.isEnabled();
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                isEnabled, isEnabled, isEnabled, isEnabled, Collections.emptyList());
    }
}
