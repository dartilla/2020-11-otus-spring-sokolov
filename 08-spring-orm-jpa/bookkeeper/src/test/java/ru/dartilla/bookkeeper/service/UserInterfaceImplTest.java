package ru.dartilla.bookkeeper.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.bookkeeper.book.vo.BookOverviewVo;
import ru.dartilla.bookkeeper.book.vo.BookInsertVo;
import ru.dartilla.bookkeeper.comment.vo.CommentNode;
import ru.dartilla.bookkeeper.comment.vo.CommentTree;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.exception.IdIsNotValidException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Консольный ui")
class UserInterfaceImplTest {

    @Mock
    private InOut inOut;

    private UserInterface userInterface;
    private ByteArrayOutputStream outByteArrayStream;

    @BeforeEach
    private void setUp() {
        outByteArrayStream = new ByteArrayOutputStream(1000);
        userInterface = new UserInterfaceImpl(inOut);
    }

    @DisplayName("позволяет ввести книгу для вставки")
    @Test
    public void shouldReadBookInsertVo() {
        BookInsertVo expected = new BookInsertVo("Книга 1", "Тютчев А.", Set.of("Фантастика"));
        when(inOut.getOut()).thenReturn(new PrintStream(outByteArrayStream));
        List<String> data = Stream.of(expected.getAuthorName(), expected.getTitle()).collect(Collectors.toList());
        data.addAll(expected.getGenreNames());
        when(inOut.getIn()).thenReturn(new Scanner(new ByteArrayInputStream(String.join(System.lineSeparator(), data).getBytes())));
        assertThat(userInterface.readBookInsertVo()).isEqualToComparingFieldByField(expected);
        String outResult = new String(outByteArrayStream.toByteArray(), StandardCharsets.UTF_8);
        assertThat(outResult).contains("наименование книги", "Введите инициалы и фамилию автора", "жанр");
    }

    @DisplayName("позволяет ввести книгу для поиска")
    @Test
    public void shouldReadBookSearchVo() {
        BookInsertVo expected = new BookInsertVo("Книга 1", "Тютчев А.", Set.of("Фантастика"));
        when(inOut.getOut()).thenReturn(new PrintStream(outByteArrayStream));
        List<String> data = Stream.of(expected.getAuthorName(), expected.getTitle()).collect(Collectors.toList());
        when(inOut.getIn()).thenReturn(new Scanner(new ByteArrayInputStream(String.join(System.lineSeparator(), data).getBytes())));
        assertThat(userInterface.readScriptSearchVo()).isEqualToComparingFieldByField(expected);
        String outResult = new String(outByteArrayStream.toByteArray(), StandardCharsets.UTF_8);
        assertThat(outResult).contains("наименование книги", "инициалы и фамилию автора");
    }

    @DisplayName("позволяет вывести статистику по книгам")
    @Test
    public void printBooks() {
        when(inOut.getOut()).thenReturn(new PrintStream(outByteArrayStream));
        BookOverviewVo expected = new BookOverviewVo(1L, "Книга 1", "Тютчев А.", 2, Set.of("Стихи, Проза"));
        userInterface.printBooks(Arrays.asList(expected));
        String outResult = new String(outByteArrayStream.toByteArray(), StandardCharsets.UTF_8);
        assertThat(outResult).contains("Доступно для выдачи");
        assertThat(outResult).contains(expected.getTitle(), expected.getAuthorName(),
                String.valueOf(expected.getAvailableToBorrow()),
                expected.getGenreNames().stream().collect(Collectors.joining(", ")));
    }

    @DisplayName("читает валидный идентификатор книги")
    @Test
    public void shouldReadValidId() {
        when(inOut.getOut()).thenReturn(new PrintStream(outByteArrayStream));
        Long expected = 22L;
        when(inOut.getIn()).thenReturn(new Scanner(new ByteArrayInputStream(expected.toString().getBytes())));
        assertThat(userInterface.readBookId()).isEqualTo(expected);
    }

    @DisplayName("выкидывает исключение при не валидном идентификаторе книги")
    @Test
    public void shouldThrowWhileReadNotValidId() {
        when(inOut.getOut()).thenReturn(new PrintStream(outByteArrayStream));
        when(inOut.getIn()).thenReturn(new Scanner(new ByteArrayInputStream("bla".getBytes())));
        assertThatThrownBy(() -> userInterface.readBookId()).isInstanceOf(IdIsNotValidException.class);
    }

    @DisplayName("позволяет вывести существующие жанры")
    @Test
    public void shouldPrintGenres() {
        when(inOut.getOut()).thenReturn(new PrintStream(outByteArrayStream));
        List<Genre> expectedList = Arrays.asList(new Genre(22L, "Дорожное приключение"), new Genre(33L, "Роман"));
        userInterface.printGenres(expectedList);
        String outResult = new String(outByteArrayStream.toByteArray(), StandardCharsets.UTF_8);
        assertThat(outResult).contains(expectedList.get(0).getName(), expectedList.get(1).getName());
        assertThat(outResult).contains("представлены жанры");
    }

    @DisplayName("позволяет вывести комментарии")
    @Test
    public void shouldPrintComments() {
        when(inOut.getOut()).thenReturn(new PrintStream(outByteArrayStream));
        List<CommentNode> rootNodes = Arrays.asList(
                new CommentNode(1L, "firstMessage", Arrays.asList(
                        new CommentNode(2L, "firstChildMessage", emptyList()),
                        new CommentNode(3L, "secondChildMessage", Arrays.asList(
                                new CommentNode(4L, "firstGrandChildMessage", emptyList())
                        ))))
        );
        CommentTree commentTree = new CommentTree(new Script(1L, "Новая заря", new Author(1L, "Неизветсных О."), null, null), rootNodes);
        userInterface.printComments(commentTree);
        String outResult = new String(outByteArrayStream.toByteArray(), StandardCharsets.UTF_8);
        System.out.println(outResult);
        outResult.contains(commentTree.getScript().getAuthor().getName());
        outResult.contains(commentTree.getScript().getTitle());
        assertThat(outResult.split(System.lineSeparator()).length).isEqualTo(5);
    }
}

