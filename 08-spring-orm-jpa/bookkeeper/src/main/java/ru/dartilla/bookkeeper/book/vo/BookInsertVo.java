package ru.dartilla.bookkeeper.book.vo;

import lombok.Data;

import java.util.Set;

@Data
public class BookInsertVo {
    private final String title;
    private final String authorName;
    private final Set<String> genreNames;
}
