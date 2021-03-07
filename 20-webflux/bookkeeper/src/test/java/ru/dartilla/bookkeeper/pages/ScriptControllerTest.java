package ru.dartilla.bookkeeper.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ScriptController.class)
@DisplayName("ScriptControllerTest должен")
class ScriptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("выводить страницу куда подгружаем рукописи")
    public void shouldListScripts() throws Exception {
        mockMvc.perform(get("/script")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("List of all scripts")));
    }
}