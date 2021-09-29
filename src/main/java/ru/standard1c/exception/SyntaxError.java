package ru.standard1c.exception;

/**
 * Базовый класс ошибки при чтении формата 1С.
 *
 * @author Maxim Tereshchenko
 */
public abstract class SyntaxError extends RuntimeException {

    protected SyntaxError(String message) {
        super(message);
    }
}
