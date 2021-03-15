package ru.dartilla.bookkeeper.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.script.ScriptService;
import ru.dartilla.bookkeeper.script.vo.ScriptDataVo;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = ScriptRestController.class)
@DisplayName("Rest контроллер книг должен")
class ScriptRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ScriptService scriptService;

    @DisplayName("возвращать все книги")
    @Test
    public void shouldGetList() {
        when(scriptService.findAll()).thenReturn(Flux.fromIterable(List.of(
                new Script("1", "One", null, null), new Script("2", "Two", null, null))));
        Flux<Script> response = webTestClient.get().uri("/rest/script").exchange()
                .expectStatus().isOk().returnResult(Script.class).getResponseBody();
        StepVerifier.create(response)
                .expectNextCount(2).verifyComplete();
    }

    @DisplayName("возвращать удалять книгу")
    @Test
    public void shouldDeleteScript() {
        when(scriptService.deleteById(any())).thenReturn(Mono.empty());
        Flux<Script> response = webTestClient.delete().uri("/rest/script/1").exchange()
                .expectStatus().isOk().returnResult(Script.class).getResponseBody();
        StepVerifier.create(response).verifyComplete();
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(scriptService, times(1)).deleteById(captor.capture());
        assertThat(captor.getValue()).isEqualTo("1");
    }

    @DisplayName("возвращать книгу по id")
    @Test
    public void shouldGetScriptById() {
        Script expected = new Script("1", "One", null, null);
        when(scriptService.findById(expected.getId())).thenReturn(Mono.just(expected));
        Flux<Script> response = webTestClient.get().uri("/rest/script/1").exchange()
                .expectStatus().isOk().returnResult(Script.class).getResponseBody();
        StepVerifier.create(response)
                .assertNext(x -> assertThat(x).isEqualTo(expected))
                .verifyComplete();
        verify(scriptService, times(1)).findById("1");
    }

    @DisplayName("сохранять книгу")
    @Test
    public void shouldSaveScript() {
        Script scriptVo = new Script("1", "One", new Author("1", "Author"), Set.of(new Genre("1", "Драма"),
                new Genre("2", "Сказка")));
        ScriptDataVo expected = new ScriptDataVo(scriptVo.getId(), scriptVo.getTitle(),
                scriptVo.getAuthor().getName(), scriptVo.getGenres().stream().map(Genre::getName).collect(toSet()));
        when(scriptService.acquireScript(expected)).thenReturn(Mono.just(scriptVo));

        Flux<Script> response = webTestClient.post().uri("/rest/script")
                .body(Mono.just(new ScriptModel(scriptVo.getId(), scriptVo.getTitle(), scriptVo.getAuthor().getName(),
                        scriptVo.getGenres().stream().map(Genre::getName).collect(Collectors.joining(",")))), ScriptModel.class)
                .exchange().expectStatus().isOk().returnResult(Script.class).getResponseBody();

        StepVerifier.create(response)
                .assertNext(x -> assertThat(x).isEqualTo(scriptVo))
                .verifyComplete();

        verify(scriptService, times(1)).acquireScript(expected);
    }
}