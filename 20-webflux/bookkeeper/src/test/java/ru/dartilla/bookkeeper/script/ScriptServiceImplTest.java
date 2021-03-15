package ru.dartilla.bookkeeper.script;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.repositores.ScriptRepository;
import ru.dartilla.bookkeeper.script.vo.ScriptDataVo;
import ru.dartilla.bookkeeper.service.AuthorService;
import ru.dartilla.bookkeeper.service.GenreService;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Service работы с книгами должен")
@ExtendWith(MockitoExtension.class)
class ScriptServiceImplTest {

    @Mock
    private ScriptRepository scriptRepository;

    @Mock
    private GenreService genreService;

    @Mock
    private AuthorService authorService;

    private ScriptServiceImpl scriptService;

    @BeforeEach
    private void setUp() {
        scriptService = new ScriptServiceImpl(scriptRepository, authorService, genreService);
    }

    @DisplayName("находить экземпляр книги по id")
    @Test
    public void shouldFindById() {
        Script expected = new Script("1", "Новый мир", null, null);
        when(scriptRepository.findById(expected.getId())).thenReturn(Mono.just(expected));
        assertThat(scriptService.findById(expected.getId()).block()).isEqualTo(expected);
    }

    @DisplayName("должен получать книгу по заглавию и автору")
    @Test
    public void shouldAcquireScriptByTitleAndAuthor() {
        Author expectedAuthor = new Author("idAuthor", "Золотых А.");
        Set<Genre> genres = Set.of(new Genre("1", "Драма"), new Genre("2", "Боевик"));
        Set<String> genreNames = genres.stream().map(Genre::getName).collect(Collectors.toSet());
        Script expectedScript = new Script("1", "Новый мир", expectedAuthor, genres);
        when(authorService.acquireAuthor(any())).thenReturn(Mono.just(expectedAuthor));
        when(scriptRepository.findByAuthorIdAndTitle(expectedAuthor.getId(), expectedScript.getTitle())).thenReturn(
                Mono.just(expectedScript));
        when(genreService.findGenreByNames(genreNames)).thenReturn(Flux.fromIterable(genres));
        when(scriptRepository.save(expectedScript)).thenReturn(Mono.just(expectedScript));
        Script actualScript = scriptService.acquireScript(new ScriptDataVo(null, expectedScript.getTitle(),
                expectedScript.getAuthor().getName(),
                genreNames)).block();
        assertThat(actualScript).isEqualTo(expectedScript);
    }

    @DisplayName("должен упасть при получении книги, если жанр не найден")
    @Test
    public void shouldThrowIfAcquireScriptWithNoGenre() {
        Author expectedAuthor = new Author("idAuthor", "Золотых А.");
        Set<Genre> genres = Set.of(new Genre("1", "Драма"), new Genre("2", "Боевик"));
        Set<String> genreNames = genres.stream().map(Genre::getName).collect(Collectors.toSet());
        Script expectedScript = new Script("1", "Новый мир", expectedAuthor, genres);
        when(authorService.acquireAuthor(any())).thenReturn(Mono.just(expectedAuthor));
        when(scriptRepository.findByAuthorIdAndTitle(expectedAuthor.getId(), expectedScript.getTitle())).thenReturn(
                Mono.just(expectedScript));
        when(genreService.findGenreByNames(genreNames)).thenReturn(Flux.just(new Genre("3", "Драма")));
        assertThatThrownBy(() -> scriptService.acquireScript(new ScriptDataVo(null, expectedScript.getTitle(),
                expectedScript.getAuthor().getName(),
                genreNames)).block()).hasMessageContaining("Не найден жанр");
    }

    @DisplayName("должен получать книгу по id")
    @Test
    public void shouldAcquireScriptById() {
        Author expectedAuthor = new Author("idAuthor", "Золотых А.");
        Set<Genre> genres = Set.of(new Genre("1", "Драма"), new Genre("2", "Боевик"));
        Set<String> genreNames = genres.stream().map(Genre::getName).collect(Collectors.toSet());
        Script expectedScript = new Script("1", "Новый мир", expectedAuthor, genres);
        when(authorService.acquireAuthor(any())).thenReturn(Mono.just(expectedAuthor));
        when(scriptRepository.findById(expectedScript.getId())).thenReturn(Mono.just(expectedScript));
        when(genreService.findGenreByNames(genreNames)).thenReturn(Flux.fromIterable(genres));
        when(scriptRepository.save(expectedScript)).thenReturn(Mono.just(expectedScript));
        Script actualScript = scriptService.acquireScript(new ScriptDataVo(expectedScript.getId(), expectedScript.getTitle(),
                expectedScript.getAuthor().getName(),
                genreNames)).block();
        assertThat(actualScript).isEqualTo(expectedScript);
    }
}