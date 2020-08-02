package ru.dartilla.bookkeeper.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.book.vo.BookInsertVo;
import ru.dartilla.bookkeeper.book.vo.BookSearchVo;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.exception.BookIdIsNotValidException;
import ru.dartilla.bookkeeper.exception.BookkeeperException;

import java.io.PrintStream;
import java.util.Collection;
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
        BookSearchVo bookSearchVo = readBookSearchVo();
        out.println("Введите жанры (через запятую):");
        String[] genreNames = in.nextLine().split(", ");
        return new BookInsertVo(bookSearchVo.getTitle(), bookSearchVo.getAuthorName(), Set.of(genreNames));
    }

    @Override
    public BookSearchVo readBookSearchVo() {
        PrintStream out = inOut.getOut();
        Scanner in = inOut.getIn();
        out.println("Введите инициалы и фамилию автора:");
        String authorName = in.nextLine();
        out.println("Введите наименование книги:");
        String bookTitle = in.nextLine();
        return new BookSearchVo(bookTitle, authorName);
    }

    @Override
    public void printBooks(Collection<BookOverviewVo> books) {
        PrintStream out = inOut.getOut();
        for (BookOverviewVo book : books) {

            out.println(String.format("%s - %s; %s; Доступно для выдачи: %s",
                    book.getAuthorName(), book.getTitle(), book.getGenreNames().stream().collect(Collectors.joining(", ")),
                    book.getAvailableToBorrow()));
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
        out.println("Введите идентификатор книги:");
        String bookIdStr = in.nextLine();
        try {
            return Long.valueOf(bookIdStr);
        } catch (Exception e) {
            throw new BookIdIsNotValidException();
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
}
