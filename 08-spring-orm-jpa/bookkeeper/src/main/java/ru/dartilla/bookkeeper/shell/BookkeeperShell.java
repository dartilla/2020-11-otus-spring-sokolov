package ru.dartilla.bookkeeper.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.dartilla.bookkeeper.book.BookService;
import ru.dartilla.bookkeeper.comment.vo.CommentInsertVo;
import ru.dartilla.bookkeeper.comment.vo.CommentTree;
import ru.dartilla.bookkeeper.script.ScriptService;
import ru.dartilla.bookkeeper.domain.Comment;
import ru.dartilla.bookkeeper.comment.CommentService;
import ru.dartilla.bookkeeper.service.GenreService;
import ru.dartilla.bookkeeper.service.UserInterface;

@ShellComponent
@AllArgsConstructor
public class BookkeeperShell {

    private final UserInterface ui;
    private final BookService bookService;
    private final GenreService genreService;
    private final CommentService commentService;
    private final ScriptService scriptService;

    @ShellMethod(value = "Add book", key = {"addBook"})
    public void addBook() {
        bookService.addBook(ui.readBookInsertVo());
    }

    @ShellMethod(value = "Show books", key = {"showBooks"})
    public void showBooks() {
        ui.printBooks(bookService.getBooksOverview());
    }

    @ShellMethod(value = "Show genres", key = {"showGenres"})
    public void showGenres() {
        ui.printGenres(genreService.getGenres());
    }

    @ShellMethod(value = "Borrow book", key = {"borrowBook"})
    public void borrowBook() {
        Long id = bookService.borrowBook(ui.readScriptSearchVo());
        ui.printBookBorrowed(id);
    }

    @ShellMethod(value = "Return book", key = {"returnBook"})
    public void returnBook() {
        bookService.returnBook(ui.readBookId());
        ui.printBookReturned();
    }

    @ShellMethod(value = "Show book comments", key = {"showComments"})
    public void showComments() {
        CommentTree commentTree = commentService.findByScript(ui.readScriptId());
        ui.printComments(commentTree);
    }

    @ShellMethod(value = "Add book comment", key = {"addComment"})
    public void addComment() {
        CommentInsertVo comment = ui.readComment();
        commentService.addComment(comment);
    }
}
