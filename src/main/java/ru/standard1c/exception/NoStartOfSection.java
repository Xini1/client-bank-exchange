package ru.standard1c.exception;

/**
 * @author Maxim Tereshchenko
 */
public class NoStartOfSection extends SyntaxError {

    public NoStartOfSection(String startOfSectionAttributeKey) {
        super(String.format("Could not find start of section attribute key '%s'", startOfSectionAttributeKey));
    }
}
