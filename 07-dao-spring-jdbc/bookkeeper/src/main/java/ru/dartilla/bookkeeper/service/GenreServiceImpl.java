package ru.dartilla.bookkeeper.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dartilla.bookkeeper.dao.GenreDao;
import ru.dartilla.bookkeeper.domain.Genre;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Override
    public Optional<Genre> findGenreByName(String name) {
        return genreDao.findByName(name);
    }

    @Override
    public Collection<Genre> getGenres() {
        return genreDao.getAll();
    }
}
