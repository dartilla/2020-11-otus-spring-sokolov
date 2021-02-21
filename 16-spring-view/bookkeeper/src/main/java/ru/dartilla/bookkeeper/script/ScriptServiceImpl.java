package ru.dartilla.bookkeeper.script;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dartilla.bookkeeper.exception.ScriptIsNotFoundException;
import ru.dartilla.bookkeeper.script.vo.ScriptDataVo;
import ru.dartilla.bookkeeper.domain.Author;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.exception.GenreNotFoundException;
import ru.dartilla.bookkeeper.repositores.ScriptRepository;
import ru.dartilla.bookkeeper.service.AuthorService;
import ru.dartilla.bookkeeper.service.GenreService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
@AllArgsConstructor
public class ScriptServiceImpl implements ScriptService {

    private final ScriptRepository scriptRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    @Transactional(readOnly = true)
    public Optional<Script> findByAuthorIdAndTitle(Long authorId, String title) {
        return scriptRepository.findFirstByAuthorIdAndTitle(authorId, title);
    }

    @Override
    @Transactional
    public Script save(Script script) {
        return scriptRepository.save(script);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Script> findById(Long id) {
        return scriptRepository.findById(id);
    }

    @Override
    public Script acquireScript(ScriptDataVo scriptDataVo) {
        Author author = authorService.acquireAuthor(scriptDataVo.getAuthorName());
        Set<Genre> genres = new HashSet<>(genreService.findGenreByNames(scriptDataVo.getGenreNames()));
        Set<String> foundGenreNames = genres.stream().map(Genre::getName).collect(toSet());
        Optional<String> absentGenre = scriptDataVo.getGenreNames().stream()
                .filter(name -> !foundGenreNames.contains(name)).findAny();
        if (absentGenre.isPresent()) {
            throw new GenreNotFoundException(absentGenre.get());
        }
        Script script;
        if (scriptDataVo.getId() != null) {
            script = findById(scriptDataVo.getId()).orElseThrow(ScriptIsNotFoundException::new);
            script.setAuthor(author);
            script.setGenres(genres);
            script.setTitle(scriptDataVo.getTitle());
        } else {
            script = findByAuthorIdAndTitle(author.getId(), scriptDataVo.getTitle())
                    .orElseGet(() -> new Script(null, scriptDataVo.getTitle(), author, genres, null));
        }
        save(script);
        return script;
    }

    @Override
    public List<Script> findAll() {
        return scriptRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        scriptRepository.deleteById(id);
    }
}
