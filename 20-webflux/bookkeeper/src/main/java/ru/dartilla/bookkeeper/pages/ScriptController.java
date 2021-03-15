package ru.dartilla.bookkeeper.pages;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class ScriptController {

    @GetMapping("/script")
    public String listBook() {
        return "script/list";
    }
}
