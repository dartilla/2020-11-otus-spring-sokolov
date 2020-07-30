package ru.dartilla.bookkeeper.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dartilla.bookkeeper.dao.AuthorDao;
import ru.dartilla.bookkeeper.domain.Author;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Override
    public Author insertAuthor(Author author) {
        long id = authorDao.insert(author);
        return new Author(id, author.getName());
    }

    @Override
    public Author acquireAuthor(String name) {
        return authorDao.findByName(name)
                .orElseGet(() -> insertAuthor(new Author(null, name)));
    }

    @Override
    public Optional<Author> findAuthor(String name) {
        return authorDao.findByName(name);
    }
}
