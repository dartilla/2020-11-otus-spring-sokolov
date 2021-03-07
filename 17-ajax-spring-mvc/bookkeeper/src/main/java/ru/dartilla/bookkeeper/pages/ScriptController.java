package ru.dartilla.bookkeeper.pages;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.dartilla.bookkeeper.script.ScriptService;

@Controller
@AllArgsConstructor
public class ScriptController {

    private ScriptService scriptService;

    @GetMapping("/script")
    public String listBook(Model model) {
        model.addAttribute("scripts", scriptService.findAll());
        return "script/list";
    }
}
