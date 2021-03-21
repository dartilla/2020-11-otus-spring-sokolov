package ru.dartilla.bookkeeper.script;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.repositores.ScriptRepository;
import ru.dartilla.bookkeeper.service.AuthorService;
import ru.dartilla.bookkeeper.service.GenreService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        Script expected = new Script(1L, "Новый мир", null, null, null);
        when(scriptRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        assertThat(scriptService.findById(expected.getId()).get()).isEqualTo(expected);
    }
}