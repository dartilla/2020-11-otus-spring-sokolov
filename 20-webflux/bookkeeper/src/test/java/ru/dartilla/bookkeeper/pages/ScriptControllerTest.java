package ru.dartilla.bookkeeper.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = ScriptController.class)
@DisplayName("ScriptControllerTest должен")
class ScriptControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("выводить страницу куда подгружаем рукописи")
    public void shouldListScripts()  {
        webTestClient.get().uri("/script").exchange().expectStatus().isOk().expectBody(String.class)
                .returnResult().getResponseBody().contains("List of all scripts");
    }
}