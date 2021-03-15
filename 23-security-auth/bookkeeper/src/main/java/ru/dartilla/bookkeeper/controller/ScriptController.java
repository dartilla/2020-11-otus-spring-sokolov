package ru.dartilla.bookkeeper.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dartilla.bookkeeper.domain.Genre;
import ru.dartilla.bookkeeper.domain.Script;
import ru.dartilla.bookkeeper.exception.GenreNotFoundException;
import ru.dartilla.bookkeeper.exception.ScriptIsNotFoundException;
import ru.dartilla.bookkeeper.script.ScriptService;
import ru.dartilla.bookkeeper.script.vo.ScriptDataVo;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class ScriptController {

    private ScriptService scriptService;

    @GetMapping("/script")
    public String listBook(Model model) {
        model.addAttribute("scripts", scriptService.findAll());
        return "script/list";
    }

    @PostMapping("/script")
    public String save(ScriptModel scriptModel) {
        if (StringUtils.isEmpty(scriptModel.getGenreNames())) throw new GenreNotFoundException("");

        scriptService.acquireScript(new ScriptDataVo(scriptModel.getId(), scriptModel.getTitle()
                , scriptModel.getAuthorName(), Set.of(scriptModel.getGenreNames().split(","))));
        return "redirect:/script";
    }

    @GetMapping("/script/add")
    public String add(Model model) {
        return "script/edit";
    }

    @GetMapping("/script/edit")
    public String edit(@RequestParam Long id, Model model) {
        Script script = scriptService.findById(id).orElseThrow(ScriptIsNotFoundException::new);
        model.addAttribute("script", script);
        model.addAttribute("genres", script.getGenres().stream().map(Genre::getName).collect(Collectors.joining(",")));
        return "script/edit";
    }

    @GetMapping("/script/delete")
    public String delete(@RequestParam Long id) {
        scriptService.deleteById(id);
        return "redirect:/script";
    }

    @ExceptionHandler(Exception.class)
    public String handleError(Exception e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
}
