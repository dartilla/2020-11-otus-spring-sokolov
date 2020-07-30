package ru.dartilla.bookkeeper.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Book {

    private Long id;
    private String title;
    private Long authorId;
    private Long genreId;
    @Setter
    private boolean inStorage;

    public int getInStorageAsInt() {
        return isInStorage() ? 1 : 0;
    }
}
