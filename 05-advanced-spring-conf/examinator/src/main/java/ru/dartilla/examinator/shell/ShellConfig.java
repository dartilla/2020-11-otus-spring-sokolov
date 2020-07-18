package ru.dartilla.examinator.shell;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;

@Configuration
public class ShellConfig {

    @Bean
    public PromptProvider examinatorPromptProvider() {
        return () -> new AttributedString("examinator:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }
}
