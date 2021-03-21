package ru.dartilla.bookkeeper.comment.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CommentNode {
    private final Long id;
    private final String message;
    private final List<CommentNode> children;
}
