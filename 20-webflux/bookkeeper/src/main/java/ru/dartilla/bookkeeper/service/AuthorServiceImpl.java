package ru.dartilla.bookkeeper.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.repositores.AuthorRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Mono<Author> insertAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Mono<Author> acquireAuthor(String name) {
        return authorRepository.findByName(name)
                .switchIfEmpty(Mono.defer(() -> insertAuthor(new Author(null, name))));
    }

    @Override
    public Mono<Author> findAuthor(String name) {
        return authorRepository.findByName(name);
    }
}
