package ru.standard1c.exception;

/**
 * @author Maxim Tereshchenko
 */
public class SyntaxError extends RuntimeException {

    public SyntaxError(String message) {
        super(message);
    }
}
