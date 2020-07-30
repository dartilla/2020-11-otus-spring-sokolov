package ru.dartilla.bookkeeper.book.vo;

import lombok.Data;

@Data
public class BookInsertVo {
    private final String title;
    private final String authorName;
    private final String genreName;
}
