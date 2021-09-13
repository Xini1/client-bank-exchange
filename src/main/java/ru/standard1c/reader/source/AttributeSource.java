package ru.standard1c.reader.source;

import java.util.Optional;

/**
 * @author Maxim Tereshchenko
 */
public interface AttributeSource {

    Optional<Attribute> next();

    boolean hasNext();
}
