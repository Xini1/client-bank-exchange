package ru.standard1c.exception;

/**
 * @author Maxim Tereshchenko
 */
public class NoEndOfSection extends SyntaxError {

    public NoEndOfSection(String endOfSectionKey) {
        super(String.format("Could not find end of section attribute key '%s'", endOfSectionKey));
    }
}
