package ru.dartilla.bookkeeper.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.repositores.GenreRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public Optional<Genre> findGenreByName(String name) {
        return genreRepository.findByName(name);
    }

    @Override
    public List<Genre> findGenreByNames(Collection<String> name) {
        return genreRepository.findByNameIn(name);
    }

    @Override
    public Collection<Genre> getGenres() {
        return genreRepository.findAll();
    }
}
