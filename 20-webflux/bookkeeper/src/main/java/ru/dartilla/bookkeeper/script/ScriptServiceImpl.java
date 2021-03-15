package ru.dartilla.bookkeeper.script;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.exception.GenreNotFoundException;
import ru.dartilla.bookkeeper.exception.ScriptIsNotFoundException;
import ru.dartilla.bookkeeper.repositores.ScriptRepository;
import ru.dartilla.bookkeeper.script.vo.ScriptDataVo;
import ru.dartilla.bookkeeper.service.AuthorService;
import ru.dartilla.bookkeeper.service.GenreService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@AllArgsConstructor
public class ScriptServiceImpl implements ScriptService {

    private final ScriptRepository scriptRepository;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    public Mono<Script> findByAuthorIdAndTitle(String authorId, String title) {
        return scriptRepository.findByAuthorIdAndTitle(authorId, title);
    }

    @Override
    public Mono<Script> save(Script script) {
        return scriptRepository.save(script);
    }

    @Override
    public Mono<Script> findById(String id) {
        return scriptRepository.findById(id);
    }

    @Override
    public Mono<Script> acquireScript(ScriptDataVo scriptDataVo) {
        Mono<Script> script;
        if (StringUtils.isNoneBlank(scriptDataVo.getId())) {
            script = findById(scriptDataVo.getId())
                    .switchIfEmpty(Mono.defer(() -> Mono.error(new ScriptIsNotFoundException())))
                    .zipWith(authorService.acquireAuthor(scriptDataVo.getAuthorName()))
                    .map(t -> {
                        Script aScript = t.getT1();
                        aScript.setAuthor(t.getT2());
                        aScript.setTitle(scriptDataVo.getTitle());
                        return aScript;
                    });
        } else {
            script = authorService.acquireAuthor(scriptDataVo.getAuthorName())
                    .flatMap(author -> findByAuthorIdAndTitle(author.getId(), scriptDataVo.getTitle())
                            .switchIfEmpty(Mono.defer(() ->
                                    Mono.just(new Script(null, scriptDataVo.getTitle(), author, null)))));
        }
        return script.zipWith(genreService.findGenreByNames(scriptDataVo.getGenreNames()).collectList())
                .map(t -> {
                    log.debug("zipped with genres");
                    Script aScript = t.getT1();
                    HashSet<Genre> genres = new HashSet<>(t.getT2());
                    aScript.setGenres(genres);
                    checkGenres(scriptDataVo.getGenreNames(), genres);
                    return aScript;
                }).flatMap(this::save);
    }

    private void checkGenres(Set<String> allGenres, Set<Genre> foundGenres) {
        log.debug("check {} contains all of {}", allGenres, foundGenres);
        Set<String> foundGenreNames = foundGenres.stream().map(Genre::getName).collect(toSet());
        Optional<String> absentGenre = allGenres.stream()
                .filter(name -> !foundGenreNames.contains(name)).findAny();
        if (absentGenre.isPresent()) {
            throw new GenreNotFoundException(absentGenre.get());
        }
    }

    @Override
    public Flux<Script> findAll() {
        return scriptRepository.findAll();
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return scriptRepository.deleteById(id);
    }
}
