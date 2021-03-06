package ru.standard1c.exception;

/**
 * Ошибка, в случае отсутствия закрывающего секцию атрибута.
 *
 * @author Maxim Tereshchenko
 */
public class NoEndOfSection extends SyntaxError {

    public NoEndOfSection(String endOfSectionAttributeKey) {
        super(String.format("Could not find end of section attribute key '%s'", endOfSectionAttributeKey));
    }
}
