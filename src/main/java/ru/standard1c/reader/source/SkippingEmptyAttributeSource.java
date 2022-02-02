package ru.standard1c.reader.source;

import lombok.RequiredArgsConstructor;

/**
 * Декоратор {@link AttributeSource}, который пропускает null атрибуты
 * (при присутствии в файле пустых строк между строками с атрибутами).
 *
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public class SkippingEmptyAttributeSource implements AttributeSource {

    private final AttributeSource original;
    private Attribute found;

    @Override
    public Attribute next() {
        if (!hasNext()) {
            return null;
        }

        var next = found;
        found = null;

        return next;
    }

    @Override
    public boolean hasNext() {
        while (found == null && original.hasNext()) {
            found = original.next();
        }

        return found != null;
    }
}
