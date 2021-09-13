package ru.standard1c.format;

import lombok.RequiredArgsConstructor;
import ru.standard1c.AlternativeRepresentation;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public enum DocumentType implements AlternativeRepresentation {

    PAYMENT_ORDER("Платежное поручение"),
    LETTER_OF_CREDIT("Аккредитив"),
    PAYMENT_CLAIM("Платежное требование"),
    COLLECTION_ORDER("Инкассовое поручение");

    private final String alternativeRepresentation;

    public static DocumentType from(String alternativeRepresentation) {
        return AlternativeRepresentation.from(DocumentType.class, alternativeRepresentation);
    }

    @Override
    public String alternativeRepresentation() {
        return alternativeRepresentation;
    }
}
