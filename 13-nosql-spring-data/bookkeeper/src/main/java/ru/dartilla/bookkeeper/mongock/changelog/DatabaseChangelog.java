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

    private Map<String, Comment> commentIndex = List.of(
            new Comment("1000", scriptIndex.get("1000"), null, "Пёс - огонь"),
            new Comment("1002", scriptIndex.get("1000"), null, "Полностью согласен"),
            new Comment("1003", scriptIndex.get("1001"), null, "Уснул на заглавии")
    ).stream().collect(Collectors.toMap(Comment::getId, Function.identity()));

    private Map<String, Book> bookIndex = List.of(
            new Book("1000", true, scriptIndex.get("1000")),
            new Book("1001", false, scriptIndex.get("1000")),
            new Book("1002", true, scriptIndex.get("1003")),
            new Book("1003", true, scriptIndex.get("1004"))
    ).stream().collect(Collectors.toMap(Book::getId, Function.identity()));

    @ChangeSet(order = "001", id = "dropDb", author = "agsokolov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insert script and comment", author = "agsokolov")
    public void insertScriptComment(MongockTemplate mongockTemplate) {
        commentIndex.get("1002").setParent(commentIndex.get("1000"));
        commentIndex.forEach((k, v) -> mongockTemplate.insert(v));
        scriptIndex.forEach((k, v) -> mongockTemplate.insert(v));
    }

    @ChangeSet(order = "003", id = "insert data", author = "agsokolov")
    public void insertData(MongockTemplate mongockTemplate) {
        authIndex.forEach((k, v) -> mongockTemplate.insert(v));
        genreIndex.forEach((k, v) -> mongockTemplate.insert(v));
        bookIndex.forEach((k, v) -> mongockTemplate.insert(v));
    }
}
