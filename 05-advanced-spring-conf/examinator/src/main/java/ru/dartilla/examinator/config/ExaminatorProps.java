package ru.dartilla.examinator.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@ConfigurationProperties
@Getter
public class ExaminatorProps {

    private Locale locale;

    public void setLocale(String locale) {
        this.locale = Locale.forLanguageTag(locale);
    }
}
