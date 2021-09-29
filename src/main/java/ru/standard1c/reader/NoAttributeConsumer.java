package ru.standard1c.reader;

import ru.standard1c.reader.source.Attribute;
import ru.standard1c.reader.source.AttributeSource;

/**
 * Пустой {@link AttributeConsumer}.
 *
 * @author Maxim Tereshchenko
 */
class NoAttributeConsumer<T> implements AttributeConsumer<T> {

    @Override
    public void accept(AttributeSource attributeSource, Attribute attribute, T accumulator) {
        //empty
    }
}
