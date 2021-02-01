package ru.dartilla.bookkeeper.comment.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentInsertVo {
    private final String scriptId;
    private final String parentId;
    private final String message;
}
