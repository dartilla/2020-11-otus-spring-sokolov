package ru.dartilla.bookkeeper.rest;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.exception.AuthorNotFoundException;
import ru.dartilla.bookkeeper.exception.GenreNotFoundException;
import ru.dartilla.bookkeeper.exception.ScriptIsNotFoundException;
import ru.dartilla.bookkeeper.exception.TitleIsEmptyException;
import ru.dartilla.bookkeeper.script.ScriptService;
import ru.dartilla.bookkeeper.script.vo.ScriptDataVo;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
public class ScriptRestController {

    private ScriptService scriptService;

    @GetMapping("/rest/script")
    public List<Script> getList() {
        return scriptService.findAll();
    }

    @DeleteMapping("/rest/script/{id}")
    public void deleteScript(@PathVariable String id) {
        scriptService.deleteById(id);
    }

    @GetMapping("/rest/script/{id}")
    public Script getScript(@PathVariable String id) {
        return scriptService.findById(id).orElseThrow(ScriptIsNotFoundException::new);
    }

    @PostMapping("/rest/script")
    public Script saveScript(@RequestBody ScriptModel scriptModel) {
        if (StringUtils.isEmpty(scriptModel.getGenreNames())) throw new GenreNotFoundException("");
        if (StringUtils.isEmpty(scriptModel.getTitle())) throw new TitleIsEmptyException();
        if (StringUtils.isEmpty(scriptModel.getAuthorName())) throw new AuthorNotFoundException();

        return scriptService.acquireScript(new ScriptDataVo(scriptModel.getId(), scriptModel.getTitle()
                , scriptModel.getAuthorName(), Set.of(scriptModel.getGenreNames().split(","))));
    }
}
