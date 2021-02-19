package ru.dartilla.bookkeeper.service;

import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.book.vo.BookInsertVo;
import ru.dartilla.bookkeeper.comment.vo.CommentInsertVo;
import ru.dartilla.bookkeeper.comment.vo.CommentTree;
import ru.dartilla.bookkeeper.script.vo.ScriptSearchVo;
import ru.dartilla.bookkeeper.domain.Comment;
import ru.dartilla.bookkeeper.domain.Genre;

import java.util.Collection;

public interface UserInterface {

    BookInsertVo readBookInsertVo();

    ScriptSearchVo readScriptSearchVo();

    void printBooks(Collection<BookOverviewVo> books);

    void printException(Exception ex);

    void printBookBorrowed(String id);

    String readBookId();

    String readScriptId();

    void printBookReturned();

    void printGenres(Collection<Genre> genres);

    void printComments(CommentTree commentTree);

    CommentInsertVo readComment();
}
