package ru.dartilla.bookkeeper.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.script.ScriptService;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ScriptController.class)
@DisplayName("ScriptControllerTest должен")
class ScriptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScriptService scriptService;

    @Test
    @DisplayName("выводить рукописи")
    public void shouldListScripts() throws Exception {
        when(scriptService.findAll()).thenReturn(List.of(
                new Script(1L, "Учение дона Хуана", new Author(1L, "Кастанеда"), null),
                new Script(2L, "Колобок", new Author(2L, "Народная"), null)));

        mockMvc.perform(get("/script")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Учение дона Хуана")))
                .andExpect(content().string(containsString("Народная")));
    }
}