package ru.standard1c.util;

import java.util.Arrays;

/**
 * @author Maxim Tereshchenko
 */
public interface AlternativeRepresentation {

    static <E extends Enum<E> & AlternativeRepresentation> E from(Class<E> type, String alternativeRepresentation) {
        return Arrays.stream(type.getEnumConstants())
                .filter(enumItem -> enumItem.alternativeRepresentation().equals(alternativeRepresentation))
                .findAny()
                .orElse(null);
    }

    String alternativeRepresentation();
}
