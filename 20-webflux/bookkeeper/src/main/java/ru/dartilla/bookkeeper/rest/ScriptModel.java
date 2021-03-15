package ru.dartilla.bookkeeper.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScriptModel {
    private String id;
    private String title;
    private String authorName;
    private String genreNames;
}
