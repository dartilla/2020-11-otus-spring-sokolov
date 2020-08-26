package ru.dartilla.bookkeeper.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.book.vo.BookInsertVo;
import ru.dartilla.bookkeeper.comment.vo.CommentInsertVo;
import ru.dartilla.bookkeeper.comment.vo.CommentTree;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.script.vo.ScriptSearchVo;
import ru.dartilla.bookkeeper.comment.vo.CommentNode;
import ru.dartilla.bookkeeper.domain.Comment;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.exception.IdIsNotValidException;
import ru.dartilla.bookkeeper.exception.BookkeeperException;
import ru.dartilla.bookkeeper.exception.MessageIsEmpty;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserInterfaceImpl implements UserInterface {

    private final InOut inOut;

    @Override
    public BookInsertVo readBookInsertVo() {
        PrintStream out = inOut.getOut();
        Scanner in = inOut.getIn();
        ScriptSearchVo scriptSearchVo = readScriptSearchVo();
        out.println("Введите жанры (через запятую):");
        String[] genreNames = in.nextLine().split(", ");
        return new BookInsertVo(scriptSearchVo.getTitle(), scriptSearchVo.getAuthorName(), Set.of(genreNames));
    }

    @Override
    public ScriptSearchVo readScriptSearchVo() {
        PrintStream out = inOut.getOut();
        Scanner in = inOut.getIn();
        out.println("Введите инициалы и фамилию автора:");
        String authorName = in.nextLine();
        out.println("Введите наименование книги:");
        String bookTitle = in.nextLine();
        return new ScriptSearchVo(bookTitle, authorName);
    }

    @Override
    public void printBooks(Collection<BookOverviewVo> books) {
        PrintStream out = inOut.getOut();
        for (BookOverviewVo book : books) {
            out.println(String.format("%s - %s; %s; Идентификатор - %s; Доступно для выдачи: %s",
                    book.getAuthorName(), book.getTitle(), book.getGenreNames().stream().collect(Collectors.joining(", ")),
                    book.getScriptId(), book.getAvailableToBorrow()));
        }
    }

    @Override
    public void printException(Exception ex) {
        inOut.getOut().println(ex.getMessage());
    }

    @Override
    public void printBookBorrowed(Long id) {
        inOut.getOut().println(String.format("Вы взяли из библиотеки книгу с id=%d. Не забудьте вернуть ее обратно!", id));
    }

    @Override
    public Long readBookId() throws BookkeeperException {
        PrintStream out = inOut.getOut();
        Scanner in = inOut.getIn();
        out.println("Введите идентификатор экземпляра книги:");
        String bookIdStr = in.nextLine();
        try {
            return Long.valueOf(bookIdStr);
        } catch (Exception e) {
            throw new IdIsNotValidException();
        }
    }

    @Override
    public Long readScriptId() throws BookkeeperException {
        PrintStream out = inOut.getOut();
        Scanner in = inOut.getIn();
        out.println("Введите идентификатор книги:");
        String scriptIdStr = in.nextLine();
        try {
            return Long.valueOf(scriptIdStr);
        } catch (Exception e) {
            throw new IdIsNotValidException();
        }
    }

    @Override
    public void printBookReturned() {
        PrintStream out = inOut.getOut();
        out.println("Вы успешно вернули книгу");
    }

    @Override
    public void printGenres(Collection<Genre> genres) {
        PrintStream out = inOut.getOut();
        StringBuilder sb = new StringBuilder();
        for (Genre genre : genres) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(genre.getName());
        }
        out.println("В библиотеке представлены жанры: " + sb.toString());
    }

    @Override
    public void printComments(CommentTree commentTree) {
        PrintStream out = inOut.getOut();
        Script script = commentTree.getScript();
        out.println(String.format("Комментарии для книги %s - %s", script.getAuthor().getName(), script.getTitle()));
        List<CommentNode> nodes = commentTree.getNodes();
        if (!CollectionUtils.isEmpty(nodes)) {
            printComments(nodes, 1);
        } else {
            out.println("  Нет комментариев");
        }
    }

    private void printComments(List<CommentNode> comments, int level) {
        for (CommentNode vo : comments) {
            printCommentMessage(level, vo.getMessage(), vo.getId());
            printComments(vo.getChildren(), level + 1);
        }
    }

    @Override
    public CommentInsertVo readComment() {
        PrintStream out = inOut.getOut();
        Scanner in = inOut.getIn();
        Long scriptId = readScriptId();
        out.println("Введите идентификатор комментария, на который вы отвечаете (пусто если вы не отвечаете):");
        Long parentId = null;
        String parentIdStr = in.nextLine();
        if (!StringUtils.isEmpty(parentIdStr)) {
            try {
                parentId = Long.valueOf(parentIdStr);
            } catch (Exception e) {
                throw new IdIsNotValidException();
            }
        }
        out.println("Введите сообщение комментария:");
        String message = in.nextLine();
        if (StringUtils.isEmpty(message)) {
            throw new MessageIsEmpty();
        }
        return new CommentInsertVo(scriptId, parentId, message);
    }

    private void printCommentMessage(int level, String message, Long id) {
        PrintStream out = inOut.getOut();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
             sb.append("  ");
        }
        sb.append(message).append(" (id=").append(id).append(")");
        out.println(sb.toString());
    }
}
