package ru.dartilla.examinator.config.localization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.dartilla.examinator.config.ExaminatorProps;

import java.nio.charset.StandardCharsets;

@Configuration
public class Localization {

    private final ExaminatorProps examinatorProps;

    public Localization(ExaminatorProps examinatorProps) {
        this.examinatorProps = examinatorProps;
    }

    @Bean
    DefLocaleMessageSource messageSource() {
        DefLocaleMessageSource messageSource = new DefLocaleMessageSource(examinatorProps.getLocale());
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setBasename("classpath:/i18n/bundle");
        return messageSource;
    }
}
