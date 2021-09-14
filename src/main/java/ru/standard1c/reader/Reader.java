package ru.standard1c.reader;

import ru.standard1c.reader.source.Attribute;
import ru.standard1c.reader.source.AttributeSource;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Абстракция инструмента для чтения последовательности
 * {@link Attribute} из {@link AttributeSource}.
 *
 * @author Maxim Tereshchenko
 */
public interface Reader<T, R> {

    String startOfSectionAttributeKey();

    String endOfSectionAttributeKey();

    Reader<T, R> onAttribute(String key, BiConsumer<T, String> valueConsumer);

    <E> Reader<T, R> onAttribute(String key, Function<String, E> mapper, BiConsumer<T, E> valueConsumer);

    <S, U> Reader<T, R> onSection(Reader<S, U> reader, BiConsumer<T, U> sectionConsumer);

    <U> Reader<T, U> onEndOfSection(Function<T, U> finisher);

    Reader<T, R> onAnyOtherAttribute(BiConsumer<T, Attribute> attributeConsumer);

    Reader<T, R> failOnUnknownAttribute();

    R read(AttributeSource attributeSource);
}
