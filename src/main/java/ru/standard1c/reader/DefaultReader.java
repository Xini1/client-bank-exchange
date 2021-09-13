package ru.standard1c.reader;

import ru.standard1c.exception.NoStartOfSection;
import ru.standard1c.exception.UnknownAttribute;
import ru.standard1c.reader.source.Attribute;
import ru.standard1c.reader.source.AttributeSource;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Maxim Tereshchenko
 */
public class DefaultReader<T, R> implements Reader<T, R> {

    private final String startOfSectionAttributeKey;
    private final String endOfSectionAttributeKey;
    private final Function<String, T> accumulatorFunction;
    private final Map<String, AttributeConsumer<T>> attributeConsumerMap;
    private final AttributeConsumer<T> anyAttributeConsumer;
    private final EndOfSectionAttributeConsumer<T> endOfSectionAttributeConsumer;
    private final Function<T, R> finisher;

    private <E> DefaultReader(
            String startOfSectionAttributeKey,
            String endOfSectionAttributeKey,
            Function<String, E> mapper,
            Function<E, T> accumulatorFunction,
            Map<String, AttributeConsumer<T>> attributeConsumerMap,
            AttributeConsumer<T> anyAttributeConsumer,
            Function<T, R> finisher
    ) {
        this.startOfSectionAttributeKey = startOfSectionAttributeKey;
        this.endOfSectionAttributeKey = endOfSectionAttributeKey;
        this.accumulatorFunction = mapper.andThen(accumulatorFunction);
        this.attributeConsumerMap = attributeConsumerMap;
        this.anyAttributeConsumer = anyAttributeConsumer;
        this.finisher = finisher;
        endOfSectionAttributeConsumer = new EndOfSectionAttributeConsumer<>(endOfSectionAttributeKey);
        attributeConsumerMap.put(endOfSectionAttributeKey, endOfSectionAttributeConsumer);
    }

    public static <U> Reader<U, U> from(
            String startOfSectionAttributeKey,
            String endOfSectionAttributeKey,
            Supplier<U> accumulatorSupplier
    ) {
        return from(
                startOfSectionAttributeKey,
                endOfSectionAttributeKey,
                Function.identity(),
                name -> accumulatorSupplier.get()
        );
    }

    public static <U> Reader<U, U> from(
            String startOfSectionAttributeKey,
            String endOfSectionAttributeKey,
            Function<String, U> accumulatorFunction
    ) {
        return from(
                startOfSectionAttributeKey,
                endOfSectionAttributeKey,
                Function.identity(),
                accumulatorFunction
        );
    }

    public static <U, E> Reader<U, U> from(
            String startOfSectionAttributeKey,
            String endOfSectionAttributeKey,
            Function<String, E> mapper,
            Function<E, U> accumulatorFunction
    ) {
        return new DefaultReader<>(
                startOfSectionAttributeKey,
                endOfSectionAttributeKey,
                mapper,
                accumulatorFunction,
                new HashMap<>(),
                new NoAttributeConsumer<>(),
                Function.identity()
        );
    }

    @Override
    public String startOfSectionAttributeKey() {
        return startOfSectionAttributeKey;
    }

    @Override
    public String endOfSectionAttributeKey() {
        return endOfSectionAttributeKey;
    }

    @Override
    public Reader<T, R> onAttribute(String key, BiConsumer<T, String> valueConsumer) {
        return onAttribute(key, Function.identity(), valueConsumer);
    }

    @Override
    public <E> Reader<T, R> onAttribute(String key, Function<String, E> mapper, BiConsumer<T, E> valueConsumer) {
        attributeConsumerMap.put(
                key,
                (attributeSource, attribute, accumulator) -> valueConsumer.accept(
                        accumulator,
                        mapper.apply(attribute.value())
                )
        );

        return this;
    }

    @Override
    public <S, U> Reader<T, R> onSection(Reader<S, U> reader, BiConsumer<T, U> sectionConsumer) {
        attributeConsumerMap.put(
                reader.startOfSectionAttributeKey(),
                (attributeSource, attribute, accumulator) -> sectionConsumer.accept(
                        accumulator,
                        reader.read(new IncludingCurrentAttributeSource(attributeSource, attribute))
                )
        );

        return this;
    }

    @Override
    public <U> Reader<T, U> onEndOfSection(Function<T, U> finisher) {
        return new DefaultReader<>(
                startOfSectionAttributeKey(),
                endOfSectionAttributeKey(),
                Function.identity(),
                accumulatorFunction,
                attributeConsumerMap,
                anyAttributeConsumer,
                finisher
        );
    }

    @Override
    public Reader<T, R> onAnyOtherAttribute(BiConsumer<T, Attribute> attributeConsumer) {
        return new DefaultReader<>(
                startOfSectionAttributeKey(),
                endOfSectionAttributeKey(),
                Function.identity(),
                accumulatorFunction,
                attributeConsumerMap,
                (attributeSource, attribute, accumulator) -> attributeConsumer.accept(accumulator, attribute),
                finisher
        );
    }

    @Override
    public Reader<T, R> failOnUnknownAttribute() {
        return onAnyOtherAttribute(
                (accumulator, attribute) -> {
                    throw new UnknownAttribute(attribute);
                }
        );
    }

    @Override
    public R read(AttributeSource attributeSource) {
        var accumulator = accumulatorFunction.apply(openSectionAttribute(attributeSource).value());

        while (attributeSource.hasNext()) {
            var current = attributeSource.next();
            current.ifPresent(attribute -> consume(attributeSource, accumulator, attribute));

            if (current.map(attribute -> attribute.hasKey(endOfSectionAttributeKey)).orElse(Boolean.FALSE)) {
                break;
            }
        }

        endOfSectionAttributeConsumer.throwSyntax1CErrorIfEndOfSectionNotReached();

        return finisher.apply(accumulator);
    }

    private void consume(AttributeSource attributeSource, T accumulator, Attribute attribute) {
        attributeConsumerMap.getOrDefault(attribute.key(), anyAttributeConsumer)
                .accept(attributeSource, attribute, accumulator);
    }

    private Attribute openSectionAttribute(AttributeSource attributeSource) {
        while (attributeSource.hasNext()) {
            var current = attributeSource.next();

            if (current.map(attribute -> attribute.hasKey(startOfSectionAttributeKey)).orElse(Boolean.FALSE)) {
                return current.get();
            }
        }

        throw new NoStartOfSection(startOfSectionAttributeKey);
    }
}
