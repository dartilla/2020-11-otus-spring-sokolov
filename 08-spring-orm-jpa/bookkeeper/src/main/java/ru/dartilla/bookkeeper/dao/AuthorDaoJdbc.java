package ru.dartilla.bookkeeper.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.dartilla.bookkeeper.domain.Author;

import java.util.Map;
import java.util.Optional;

@Service
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final RowMapper<Author> authorRowMapper = (rs, rowNum) -> new Author(
            rs.getLong("id"), rs.getString("name"));

    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public long insert(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update("insert into author (id, name) values (:id, :name)",
                new MapSqlParameterSource()
                        .addValue("id", author.getId())
                        .addValue("name", author.getName()),
                keyHolder);
        return (long) keyHolder.getKey();
    }

    @Override
    public Author getById(Long id) {
        return jdbcOperations.queryForObject("select * from author where id = :id", Map.of("id", id), authorRowMapper);
    }

    @Override
    public Optional<Author> findByName(String name) {
        return jdbcOperations.query("select * from author where name = :name", Map.of("name", name),
                authorRowMapper).stream().findAny();
    }
}
