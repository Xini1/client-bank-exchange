package ru.standard1c.exception;

import ru.standard1c.reader.source.Attribute;

/**
 * @author Maxim Tereshchenko
 */
public class UnknownAttribute extends SyntaxError {

    public UnknownAttribute(Attribute attribute) {
        super(String.format("Unknown attribute: %s", attribute));
    }
}
