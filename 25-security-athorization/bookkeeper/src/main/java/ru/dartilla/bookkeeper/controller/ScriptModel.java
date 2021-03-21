package ru.dartilla.bookkeeper.controller;

import lombok.Data;

@Data
public class ScriptModel {
    private Long id;
    private String title;
    private String authorName;
    private String genreNames;
}
