package ru.dartilla.bookkeeper.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import com.mongodb.client.MongoDatabase;
import ru.dartilla.bookkeeper.domain.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@ChangeLog
public class DatabaseChangelogTest {

    private Map<String, Author> authIndex = List.of(
            new Author("1", "Кастанеда К."),
            new Author("2", "Дойль A. К."))
            .stream().collect(Collectors.toMap(Author::getId, Function.identity()));

    private Map<String, Genre> genreIndex = List.of(
            new Genre("1", "Мистика"),
            new Genre("2", "Детектив"),
            new Genre("3", "Эзотерика"))
            .stream().collect(Collectors.toMap(Genre::getId, Function.identity()));

    private Map<String, Script> scriptIndex = List.of(
            new Script("1", "Учение дона Хуана", authIndex.get("1"), Set.of(genreIndex.get("1"))),
            new Script("2", "Отдельная реальность", authIndex.get("1"), Set.of(genreIndex.get("1"))),
            new Script("3", "Новое видение", authIndex.get("1"), Set.of(genreIndex.get("1"))),
            new Script("4", "Сияние чистоты", authIndex.get("1"), Set.of(genreIndex.get("1"), genreIndex.get("3")))
    ).stream().collect(Collectors.toMap(Script::getId, Function.identity()));

    @ChangeSet(order = "001", id = "dropDb", author = "agsokolov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "003", id = "insert data", author = "agsokolov")
    public void insertData(MongockTemplate mongockTemplate) {
        authIndex.forEach((k, v) -> mongockTemplate.insert(v));
        genreIndex.forEach((k, v) -> mongockTemplate.insert(v));
        scriptIndex.forEach((k, v) -> mongockTemplate.insert(v));
    }
}
