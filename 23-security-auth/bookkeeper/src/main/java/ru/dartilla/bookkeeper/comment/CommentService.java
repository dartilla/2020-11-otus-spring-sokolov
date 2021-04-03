package ru.dartilla.bookkeeper.comment;

import ru.dartilla.bookkeeper.comment.vo.CommentInsertVo;
import ru.dartilla.bookkeeper.comment.vo.CommentTree;

public interface CommentService {

    CommentTree findByScript(Long scriptId);

    void addComment(CommentInsertVo comment);
}
