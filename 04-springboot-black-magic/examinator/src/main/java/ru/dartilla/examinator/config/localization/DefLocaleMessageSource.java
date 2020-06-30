package ru.dartilla.examinator.config.localization;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.lang.Nullable;

import java.util.Locale;

public class DefLocaleMessageSource extends ReloadableResourceBundleMessageSource {

    private final Locale locale;

    public DefLocaleMessageSource(Locale locale) {
        this.locale = locale;
    }

    public String getMessage(String code) throws NoSuchMessageException {
        return getMessage(code, null, locale);
    }

    public String getMessage(String code, @Nullable Object[] args) throws NoSuchMessageException {
        return getMessage(code, args, locale);
    }
}
