package ru.standard1c.reader;

import ru.standard1c.reader.source.Attribute;
import ru.standard1c.reader.source.AttributeSource;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Абстракция настраиваемого инструмента для чтения последовательности
 * {@link Attribute} из {@link AttributeSource}.
 *
 * @author Maxim Tereshchenko
 */
public interface ConfigurableReader<T, R> extends Reader<R> {

    ConfigurableReader<T, R> onAttribute(String key, BiConsumer<T, String> valueConsumer);

    <U> ConfigurableReader<T, R> onAttribute(String key, Function<String, U> mapper, BiConsumer<T, U> valueConsumer);

    <U> ConfigurableReader<T, R> onSection(Reader<U> reader, BiConsumer<T, U> sectionConsumer);

    <U> ConfigurableReader<T, U> onEndOfSection(Function<T, U> finisher);

    ConfigurableReader<T, R> onAnyOtherAttribute(BiConsumer<T, Attribute> attributeConsumer);

    ConfigurableReader<T, R> failOnUnknownAttribute();
}
