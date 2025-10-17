package org.tasker.common.source;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ExceptionMessageSource implements MessageSource {

    private static Locale locale = Locale.getDefault();

    private final ResourceBundle resourceBundle;

    public ExceptionMessageSource() {
        resourceBundle = ResourceBundle.getBundle("localize.rest_exception_tasker",
                locale,
                Thread.currentThread().getContextClassLoader());
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        try {
            return resourceBundle.getString(code);
        } catch (MissingResourceException ignored) {}
        return defaultMessage;
    }

    @Override
    public String getMessage(String message, Object[] args, Locale locale) throws NoSuchMessageException {
        return getMessage(message, args, message, locale);
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        String message = resolvable.getDefaultMessage();
        return getMessage(message, resolvable.getArguments(), message, locale);
    }
}
