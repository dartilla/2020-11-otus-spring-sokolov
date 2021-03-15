package ru.dartilla.bookkeeper.script.vo;

import lombok.Data;

import java.util.Set;

@Data
public class ScriptDataVo {
    private final Long id;
    private final String title;
    private final String authorName;
    private final Set<String> genreNames;
}
