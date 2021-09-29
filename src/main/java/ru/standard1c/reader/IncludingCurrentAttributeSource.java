package ru.standard1c.reader;

import lombok.RequiredArgsConstructor;
import ru.standard1c.reader.source.Attribute;
import ru.standard1c.reader.source.AttributeSource;

/**
 * Используется для сохранения последнего прочитанного атрибута
 * в {@link DefaultReader}.
 *
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
class IncludingCurrentAttributeSource implements AttributeSource {

    private final AttributeSource original;
    private final Attribute previous;
    private boolean isAdvanced = false;

    @Override
    public Attribute next() {
        if (isAdvanced) {
            return original.next();
        }

        isAdvanced = true;

        return previous;
    }

    @Override
    public boolean hasNext() {
        if (isAdvanced) {
            return original.hasNext();
        }

        return true;
    }
}
