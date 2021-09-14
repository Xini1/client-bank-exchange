package ru.standard1c.reader.source;

/**
 * Абстракция источника {@link Attribute} с возможностью итерации.
 *
 * @author Maxim Tereshchenko
 */
public interface AttributeSource {

    Attribute next();

    boolean hasNext();
}
