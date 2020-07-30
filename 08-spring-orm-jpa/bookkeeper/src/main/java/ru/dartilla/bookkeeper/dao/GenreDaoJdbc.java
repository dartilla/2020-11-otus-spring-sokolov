package ru.dartilla.bookkeeper.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import ru.dartilla.bookkeeper.domain.Book;
import ru.dartilla.bookkeeper.domain.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

@Service
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    private RowMapper<Genre> genreRowMapper = (rs, rowNum) -> new Genre(rs.getLong("id"), rs.getString("name"));

    public GenreDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Genre getById(Long id) {
        return jdbcOperations.queryForObject("select * from genre where id = :id", Map.of("id", id), genreRowMapper);
    }

    @Override
    public Optional<Genre> findByName(String name) {
        return jdbcOperations.query("select * from genre where name = :name", Map.of("name", name), genreRowMapper)
                .stream().findAny();
    }

    @Override
    public List<Genre> getAll() {
        return jdbcOperations.query("select * from genre", emptyMap(), genreRowMapper);
    }
}
