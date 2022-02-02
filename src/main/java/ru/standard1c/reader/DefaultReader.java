package ru.standard1c.reader;

import ru.standard1c.exception.NoEndOfSection;
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
 * Реализация по умолчанию {@link ConfigurableReader}.
 *
 * @author Maxim Tereshchenko
 */
public class DefaultReader<T, R> implements ConfigurableReader<T, R> {

    private final String startOfSectionAttributeKey;
    private final String endOfSectionAttributeKey;
    private final Function<String, T> accumulatorFunction;
    private final Map<String, AttributeConsumer<T>> attributeConsumerMap;
    private final AttributeConsumer<T> anyAttributeConsumer;
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
    }

    public static <U> ConfigurableReader<U, U> from(
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

    public static <U> ConfigurableReader<U, U> from(
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

    public static <U, E> ConfigurableReader<U, U> from(
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
    public R read(AttributeSource attributeSource) {
        var accumulator = accumulatorFunction.apply(openSectionAttribute(attributeSource).value());

        while (attributeSource.hasNext()) {
            var current = attributeSource.next();

            if (current.hasKey(endOfSectionAttributeKey)) {
                return finisher.apply(accumulator);
            }

            consume(attributeSource, current, accumulator);
        }

        throw new NoEndOfSection(endOfSectionAttributeKey);
    }

    @Override
    public ConfigurableReader<T, R> onAttribute(String key, BiConsumer<T, String> valueConsumer) {
        return onAttribute(key, Function.identity(), valueConsumer);
    }

    @Override
    public <U> ConfigurableReader<T, R> onAttribute(
            String key,
            Function<String, U> mapper,
            BiConsumer<T, U> valueConsumer
    ) {
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
    public <U> ConfigurableReader<T, R> onSection(Reader<U> reader, BiConsumer<T, U> sectionConsumer) {
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
    public <U> ConfigurableReader<T, U> onEndOfSection(Function<T, U> finisher) {
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
    public ConfigurableReader<T, R> onAnyOtherAttribute(BiConsumer<T, Attribute> attributeConsumer) {
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
    public ConfigurableReader<T, R> failOnUnknownAttribute() {
        return onAnyOtherAttribute(
                (accumulator, attribute) -> {
                    throw new UnknownAttribute(attribute);
                }
        );
    }

    private void consume(AttributeSource attributeSource, Attribute attribute, T accumulator) {
        attributeConsumerMap.getOrDefault(attribute.key(), anyAttributeConsumer)
                .accept(attributeSource, attribute, accumulator);
    }

    private Attribute openSectionAttribute(AttributeSource attributeSource) {
        while (attributeSource.hasNext()) {
            var current = attributeSource.next();

            if (current.hasKey(startOfSectionAttributeKey)) {
                return current;
            }
        }

        throw new NoStartOfSection(startOfSectionAttributeKey);
    }
}
