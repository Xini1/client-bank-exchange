package ru.standard1c.exception;

/**
 * @author Maxim Tereshchenko
 */
public abstract class SyntaxError extends RuntimeException {

    protected SyntaxError(String message) {
        super(message);
    }
}
