package ru.dartilla.bookkeeper.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.dartilla.bookkeeper.book.BookService;
import ru.dartilla.bookkeeper.exception.BookkeeperException;
import ru.dartilla.bookkeeper.service.GenreService;
import ru.dartilla.bookkeeper.service.UserInterface;

@ShellComponent
@AllArgsConstructor
public class BookkeeperShell {

    private final UserInterface ui;
    private final BookService bookService;
    private final GenreService genreService;

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
        Long id = bookService.borrowBook(ui.readBookSearchVo());
        ui.printBookBorrowed(id);
    }

    @ShellMethod(value = "Return book", key = {"returnBook"})
    public void returnBook() {
        bookService.returnBook(ui.readBookId());
        ui.printBookReturned();
    }
}
