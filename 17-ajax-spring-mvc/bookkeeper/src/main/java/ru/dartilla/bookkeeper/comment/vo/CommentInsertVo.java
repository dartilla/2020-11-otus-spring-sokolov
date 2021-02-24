package ru.dartilla.bookkeeper.comment.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentInsertVo {
    private final Long scriptId;
    private final Long parentId;
    private final String message;
}
