package ru.dartilla.bookkeeper.config;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import ru.dartilla.bookkeeper.exception.BookkeeperException;
import ru.dartilla.bookkeeper.service.UserInterface;


@Component
@Aspect
@AllArgsConstructor
public class ShellCatchAspect {

    private static Logger logger = LogManager.getLogger();

    private final UserInterface ui;

    @Pointcut("within(ru.dartilla.bookkeeper.shell.BookkeeperShell)")
    public void bookkeeperShell() {
    }

    @Pointcut("@annotation(org.springframework.shell.standard.ShellMethod)")
    public void shellMethods() {
    }

    /**
     * Обрабатываем бизнес исключения
     */
    @Around("bookkeeperShell() && shellMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        } catch (BookkeeperException ex) {
            logger.error(ex);
            ui.printException(ex);
        }
        return proceed;
    }
}
