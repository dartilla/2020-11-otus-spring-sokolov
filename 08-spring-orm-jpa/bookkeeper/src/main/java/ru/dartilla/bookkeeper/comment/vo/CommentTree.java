package ru.dartilla.bookkeeper.comment.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import ru.dartilla.bookkeeper.domain.Script;

import java.util.List;

@Data
@AllArgsConstructor
public class CommentTree {
    private Script script;
    private List<CommentNode> nodes;
}
