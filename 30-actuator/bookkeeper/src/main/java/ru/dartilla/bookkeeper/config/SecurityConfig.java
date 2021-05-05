package ru.dartilla.bookkeeper.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.dartilla.bookkeeper.repositores.AuthorityRepository;
import ru.dartilla.bookkeeper.repositores.UserRepository;
import ru.dartilla.bookkeeper.security.UserDetailsServiceLocal;


@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = false)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().mvcMatchers("/script").hasAnyRole("USER", "MANAGER")
                .and()
                .authorizeRequests().mvcMatchers("/script/**").hasAnyRole("MANAGER")
                .and()
                .authorizeRequests().antMatchers("/**").authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/")
                .and()
                .rememberMe()
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsServiceLocal(userRepository, authorityRepository));
    }
}
