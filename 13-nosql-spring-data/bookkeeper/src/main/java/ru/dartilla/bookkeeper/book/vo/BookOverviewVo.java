package ru.dartilla.bookkeeper.book.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class BookOverviewVo {
    private final String scriptId;
    private final String title;
    private final String authorName;
    private int availableToBorrow;
    private final Set<String> genreNames;
}
