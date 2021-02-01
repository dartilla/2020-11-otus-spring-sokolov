package ru.dartilla.bookkeeper.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.repositores.AuthorRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Author insertAuthor(Author author) {
        authorRepository.save(author);
        return author;
    }

    @Override
    public Author acquireAuthor(String name) {
        return authorRepository.findByName(name)
                .orElseGet(() -> insertAuthor(new Author(null, name)));
    }

    @Override
    public Optional<Author> findAuthor(String name) {
        return authorRepository.findByName(name);
    }
}
