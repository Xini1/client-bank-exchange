package ru.standard1c.reader;

import lombok.RequiredArgsConstructor;
import ru.standard1c.reader.source.Attribute;
import ru.standard1c.reader.source.AttributeSource;

import java.util.Optional;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
class IncludingCurrentAttributeSource implements AttributeSource {

    private final AttributeSource original;
    private final Attribute previous;
    private boolean isAdvanced = false;

    @Override
    public Optional<Attribute> next() {
        if (isAdvanced) {
            return original.next();
        }

        isAdvanced = true;

        return Optional.ofNullable(previous);
    }

    @Override
    public boolean hasNext() {
        if (isAdvanced) {
            return original.hasNext();
        }

        return true;
    }
}
