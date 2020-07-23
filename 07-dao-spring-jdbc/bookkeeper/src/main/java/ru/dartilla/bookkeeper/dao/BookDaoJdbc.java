package ru.dartilla.bookkeeper.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Service;
import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.domain.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

@Service
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations jdbcOperations;

    private RowMapper<Book> bookRowMapper = (rs, rowNum) -> new Book(
            rs.getLong("id"), rs.getString("title"), rs.getLong("author_id"),
            rs.getLong("genre_id"), rs.getBoolean("in_storage"));

    private final RowMapper<BookOverviewVo> bookStatRowMapper = (rs, rowNum) -> new BookOverviewVo(
            rs.getString("title"), rs.getString("author_name"), rs.getInt("in_storage")
    );

    public BookDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public void insert(Book book) {
        jdbcOperations.update(
                "insert into book (id, title, author_id, genre_id, in_storage) values " +
                        "(:id, :title, :authorId, :genreId, :inStorage)",
                new MapSqlParameterSource()
                        .addValue("id", book.getId())
                        .addValue("title", book.getTitle())
                        .addValue("authorId", book.getAuthorId())
                        .addValue("genreId", book.getGenreId())
                        .addValue("inStorage", book.getInStorageAsInt())
                );
    }

    @Override
    public void update(Book book) {
        jdbcOperations.update(
                "update book set title = :title, author_id = :authorId, genre_id = :genreId, in_storage = :inStorage" +
                        " where id = :id",
                new MapSqlParameterSource()
                        .addValue("id", book.getId())
                        .addValue("title", book.getTitle())
                        .addValue("authorId", book.getAuthorId())
                        .addValue("genreId", book.getGenreId())
                        .addValue("inStorage", book.getInStorageAsInt())
        );
    }

    @Override
    public void delete(Long id) {
        jdbcOperations.update("delete from book where id = :id", Map.of("id", id));
    }

    @Override
    public Book getById(Long id) {
        return jdbcOperations.queryForObject("select * from book where id = :id", Map.of("id", id), bookRowMapper);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return jdbcOperations.query("select * from book where id = :id", Map.of("id", id), bookRowMapper).stream().findAny();
    }

    @Override
    public List<Book> findByAuthorIdAndTitleAndStorage(Long authorId, String title, boolean isInStorage) {
        return jdbcOperations.query("select * from book where author_id = :authorId and title = :title " +
                        "and in_storage = :isInStorage",
                new MapSqlParameterSource("authorId", authorId)
                        .addValue("title", title)
                        .addValue("isInStorage", isInStorage ? 1 : 0),
                bookRowMapper);
    }

    @Override
    public List<Book> getAll() {
        return jdbcOperations.query("select * from book", emptyMap(), bookRowMapper);
    }

    @Override
    public List<BookOverviewVo> getBooksOverview() {
        return jdbcOperations.query(
                "select book.title, max(author.name) as author_name, " +
                        " sum(book.in_storage) as in_storage from book" +
                " join author on author.id = book.author_id" +
                " group by book.title, book.author_id order by author.name, book.title", emptyMap(), bookStatRowMapper);
    }
}
