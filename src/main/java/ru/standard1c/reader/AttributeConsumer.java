package ru.standard1c.reader;

import ru.standard1c.reader.source.Attribute;
import ru.standard1c.reader.source.AttributeSource;

/**
 * Используется для обработки атрибутов в {@link DefaultReader}.
 *
 * @author Maxim Tereshchenko
 */
@FunctionalInterface
interface AttributeConsumer<T> {

    void accept(AttributeSource attributeSource, Attribute attribute, T accumulator);
}
