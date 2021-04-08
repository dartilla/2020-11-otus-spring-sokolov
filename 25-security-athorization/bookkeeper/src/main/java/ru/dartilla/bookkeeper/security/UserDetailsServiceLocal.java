package ru.dartilla.bookkeeper.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.dartilla.bookkeeper.domain.User;
import ru.dartilla.bookkeeper.repositores.AuthorityRepository;
import ru.dartilla.bookkeeper.repositores.UserRepository;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class UserDetailsServiceLocal implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Login is not found: " + username));
        boolean isEnabled = user.isEnabled();
        List<SimpleGrantedAuthority> authorities = authorityRepository.findByLogin(username).stream()
                .map(x -> new SimpleGrantedAuthority("ROLE_" + x.getAuthority())).collect(toList());
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                isEnabled, isEnabled, isEnabled, isEnabled, authorities);
    }
}
