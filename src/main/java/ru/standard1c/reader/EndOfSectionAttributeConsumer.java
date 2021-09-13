package ru.standard1c.reader;

import lombok.RequiredArgsConstructor;
import ru.standard1c.exception.NoEndOfSection;
import ru.standard1c.reader.source.Attribute;
import ru.standard1c.reader.source.AttributeSource;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
class EndOfSectionAttributeConsumer<T> implements AttributeConsumer<T> {

    private final String closeSectionAttributeKey;
    private boolean isEndOfSectionNotReached = true;

    @Override
    public void accept(AttributeSource attributeSource, Attribute attribute, T accumulator) {
        isEndOfSectionNotReached = false;
    }

    void throwSyntax1CErrorIfEndOfSectionNotReached() {
        if (isEndOfSectionNotReached) {
            throw new NoEndOfSection(closeSectionAttributeKey);
        }
    }
}
