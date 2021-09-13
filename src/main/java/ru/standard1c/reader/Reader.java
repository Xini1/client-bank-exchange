package ru.standard1c.reader;

import lombok.RequiredArgsConstructor;
import ru.standard1c.exception.NoEndOfSection;
import ru.standard1c.exception.NoStartOfSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Maxim Tereshchenko
 */
public class Reader<T> {

    private final String openSectionAttributeKey;
    private final String closeSectionAttributeKey;
    private final Function<String, T> accumulatorFunction;
    private final Map<String, AttributeConsumer<T>> attributeConsumerMap = new HashMap<>();
    private final EndOfSectionAttributeConsumer<T> endOfSectionAttributeConsumer;

    public Reader(String openSectionAttributeKey, String closeSectionAttributeKey, Function<String, T> accumulatorFunction) {
        this(openSectionAttributeKey, closeSectionAttributeKey, Function.identity(), accumulatorFunction);
    }

    public <E> Reader(String openSectionAttributeKey, String closeSectionAttributeKey, Function<String, E> mapper, Function<E, T> accumulatorFunction) {
        this.openSectionAttributeKey = openSectionAttributeKey;
        this.closeSectionAttributeKey = closeSectionAttributeKey;
        this.accumulatorFunction = mapper.andThen(accumulatorFunction);
        endOfSectionAttributeConsumer = new EndOfSectionAttributeConsumer<>(closeSectionAttributeKey);
        attributeConsumerMap.put(closeSectionAttributeKey, endOfSectionAttributeConsumer);
    }

    public Reader<T> onAttribute(String key, BiConsumer<T, String> valueConsumer) {
        return onAttribute(key, Function.identity(), valueConsumer);
    }

    public <E> Reader<T> onAttribute(String key, Function<String, E> mapper, BiConsumer<T, E> valueConsumer) {
        attributeConsumerMap.put(key, (attributeSource, attribute, accumulator) -> valueConsumer.accept(accumulator, mapper.apply(attribute.getValue())));

        return this;
    }

    public <S> Reader<T> onSection(Reader<S> reader1C, BiConsumer<T, S> sectionConsumer) {
        attributeConsumerMap.put(reader1C.openSectionAttributeKey, ((attributeSource, attribute, accumulator) -> sectionConsumer.accept(accumulator, reader1C.read(new IncludingCurrentAttributeSource(attributeSource, attribute)))));

        return this;
    }

    public T read(AttributeSource attributeSource) {
        var accumulator = accumulatorFunction.apply(openSectionAttribute(attributeSource).getValue());

        while (attributeSource.hasNext()) {
            var current = attributeSource.next();
            current.ifPresent(attribute ->
                    Optional.ofNullable(attributeConsumerMap.get(attribute.getKey()))
                            .ifPresent(attributeHandler -> attributeHandler.accept(attributeSource, attribute, accumulator))
            );

            if (current.map(attribute -> attribute.hasKey(closeSectionAttributeKey)).orElse(Boolean.FALSE)) {
                break;
            }
        }

        endOfSectionAttributeConsumer.throwSyntax1CErrorIfEndOfSectionNotReached();

        return accumulator;
    }

    private Attribute openSectionAttribute(AttributeSource attributeSource) {
        while (attributeSource.hasNext()) {
            var current = attributeSource.next();

            if (current.map(attribute -> attribute.hasKey(openSectionAttributeKey)).orElse(Boolean.FALSE)) {
                return current.get();
            }
        }

        throw new NoStartOfSection(openSectionAttributeKey);
    }

    @FunctionalInterface
    private interface AttributeConsumer<T> {

        void accept(AttributeSource attributeSource, Attribute attribute, T accumulator);
    }

    @RequiredArgsConstructor
    private static class EndOfSectionAttributeConsumer<T> implements AttributeConsumer<T> {

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

    @RequiredArgsConstructor
    private static class IncludingCurrentAttributeSource implements AttributeSource {

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
}
