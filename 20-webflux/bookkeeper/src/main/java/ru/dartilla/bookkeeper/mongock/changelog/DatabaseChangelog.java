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
public class DatabaseChangelog {

    private Map<String, Author> authIndex = List.of(
            new Author("1000", "Лисина А."),
            new Author("1001", "Ремарк Э. М."),
            new Author("1002", "Народная"))
            .stream().collect(Collectors.toMap(Author::getId, Function.identity()));

    private Map<String, Genre> genreIndex = List.of(
            new Genre("1000", "Детская книга"),
            new Genre("1001", "Драма"))
            .stream().collect(Collectors.toMap(Genre::getId, Function.identity()));

    private Map<String, Script> scriptIndex = List.of(
            new Script("1000", "Дикий пёс", authIndex.get("1000"), Set.of(genreIndex.get("1000"))),
            new Script("1003", "Три товарища", authIndex.get("1001"), Set.of(genreIndex.get("1001"))),
            new Script("1004", "Колобок", authIndex.get("1002"), Set.of(genreIndex.get("1000"), genreIndex.get("1001")))
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
