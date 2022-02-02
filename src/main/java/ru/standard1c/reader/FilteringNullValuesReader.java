package ru.standard1c.reader;

import lombok.RequiredArgsConstructor;
import ru.standard1c.reader.source.Attribute;
import ru.standard1c.reader.source.AttributeSource;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Декоратор {@link Reader}, которая дополняет обработчики атрибутов
 * при конфигурации в проверки на null.
 *
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public class FilteringNullValuesReader<T, R> implements ConfigurableReader<T, R> {

    private final ConfigurableReader<T, R> original;

    @Override
    public String startOfSectionAttributeKey() {
        return original.startOfSectionAttributeKey();
    }

    @Override
    public String endOfSectionAttributeKey() {
        return original.endOfSectionAttributeKey();
    }

    @Override
    public R read(AttributeSource attributeSource) {
        return original.read(attributeSource);
    }

    @Override
    public ConfigurableReader<T, R> onAttribute(String key, BiConsumer<T, String> valueConsumer) {
        original.onAttribute(key, valueConsumer);

        return this;
    }

    @Override
    public <U> ConfigurableReader<T, R> onAttribute(
            String key,
            Function<String, U> mapper,
            BiConsumer<T, U> valueConsumer
    ) {
        original.onAttribute(key, value -> nullSafeMap(value, mapper), valueConsumer);

        return this;
    }

    @Override
    public <U> ConfigurableReader<T, R> onSection(Reader<U> reader, BiConsumer<T, U> sectionConsumer) {
        original.onSection(reader, sectionConsumer);

        return this;
    }

    @Override
    public <U> ConfigurableReader<T, U> onEndOfSection(Function<T, U> finisher) {
        return new FilteringNullValuesReader<>(original.onEndOfSection(finisher));
    }

    @Override
    public ConfigurableReader<T, R> onAnyOtherAttribute(BiConsumer<T, Attribute> attributeConsumer) {
        original.onAnyOtherAttribute(attributeConsumer);

        return this;
    }

    @Override
    public ConfigurableReader<T, R> failOnUnknownAttribute() {
        original.failOnUnknownAttribute();

        return this;
    }

    private <U> U nullSafeMap(String value, Function<String, U> mapper) {
        return Optional.ofNullable(value)
                .map(mapper)
                .orElse(null);
    }
}
