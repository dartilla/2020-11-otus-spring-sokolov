package ru.dartilla.bookkeeper.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.script.ScriptService;
import ru.dartilla.bookkeeper.script.vo.ScriptDataVo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                new Script(1L, "Учение дона Хуана", new Author(1L, "Кастанеда"), null, null),
                new Script(2L, "Колобок", new Author(2L, "Народная"), null, null)));

        mockMvc.perform(get("/script")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Учение дона Хуана")))
                .andExpect(content().string(containsString("Народная")));
    }

    @DisplayName("сохранять скрипт")
    @Test
    public void shouldSaveScript() throws Exception {
        ScriptDataVo expected = new ScriptDataVo(1L, "Учение дона Хуана", "Кастанеда", Set.of("Драма", "Комедия"));

        mockMvc.perform(post("/script")
                .param("id", expected.getId().toString())
                .param("title", expected.getTitle())
                .param("authorName", expected.getAuthorName())
                .param("genreNames", String.join(",", expected.getGenreNames())))
                .andDo(print()).andExpect(status().is3xxRedirection());

        ArgumentCaptor<ScriptDataVo> captor = ArgumentCaptor.forClass(ScriptDataVo.class);
        verify(scriptService, times(1)).acquireScript(captor.capture());
        assertThat(captor.getValue()).isEqualTo(expected);
    }

    @DisplayName("возвращать скрипт на изменение")
    @Test
    public void shouldEditScript() throws Exception {
        ScriptDataVo expected = new ScriptDataVo(1L, "Учение дона Хуана", "Кастанеда", Set.of("Драма", "Комедия"));

        when(scriptService.findById(expected.getId())).thenReturn(Optional.of(
                new Script(expected.getId(), expected.getTitle(),
                        new Author(1L, expected.getAuthorName()),
                        Set.of(new Genre(1L, "Драма"), new Genre(2L, "Комедия")), null)));
        mockMvc.perform(get("/script/edit")
                .param("id", expected.getId().toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(expected.getTitle())))
                .andExpect(content().string(containsString(expected.getAuthorName())))
                .andExpect(content().string(containsString(String.join(",", expected.getGenreNames()))))
                .andExpect(content().string(containsString("Fill script")));
    }

    @DisplayName("удалять скрипт")
    @Test
    public void shouldDeleteScript() throws Exception {
        ScriptDataVo expected = new ScriptDataVo(1L, "Учение дона Хуана", "Кастанеда", Set.of("Драма", "Комедия"));

        mockMvc.perform(get("/script/delete")
                .param("id", expected.getId().toString()))
                .andDo(print()).andExpect(status().is3xxRedirection());

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(scriptService, times(1)).deleteById(captor.capture());
        assertThat(captor.getValue()).isEqualTo(expected.getId());
    }

    @DisplayName("перенаправлять на форму создания")
    @Test
    public void shouldAddScript() throws Exception {
        mockMvc.perform(get("/script/add"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Fill script")));
    }
}