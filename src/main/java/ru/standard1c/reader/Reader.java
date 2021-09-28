package ru.standard1c.reader;

import ru.standard1c.reader.source.Attribute;
import ru.standard1c.reader.source.AttributeSource;

/**
 * Абстракция инструмента для чтения последовательности
 * {@link Attribute} из {@link AttributeSource}.
 *
 * @author Maxim Tereshchenko
 */
public interface Reader<T> {

    String startOfSectionAttributeKey();

    String endOfSectionAttributeKey();

    T read(AttributeSource attributeSource);
}
