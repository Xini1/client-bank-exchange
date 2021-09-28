package ru.standard1c.util;

import java.util.Arrays;

/**
 * Интерфейс для пометки перечисления, у которых есть
 * альтернативное представление (например, на русском языке).
 *
 * @author Maxim Tereshchenko
 */
public interface AlternativeRepresentation {

    static <E extends Enum<E> & AlternativeRepresentation> E from(Class<E> type, String alternativeRepresentation) {
        return Arrays.stream(type.getEnumConstants())
                .filter(enumItem -> enumItem.alternativeRepresentation().equalsIgnoreCase(alternativeRepresentation))
                .findAny()
                .orElse(null);
    }

    String alternativeRepresentation();
}
