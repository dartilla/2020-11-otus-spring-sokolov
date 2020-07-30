package ru.dartilla.bookkeeper.book.vo;

import lombok.Data;

@Data
public class BookOverviewVo {
    private final String title;
    private final String authorName;
    private final int availableToBorrow;
}
