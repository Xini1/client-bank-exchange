package ru.standard1c.format;

import lombok.RequiredArgsConstructor;
import ru.standard1c.util.AlternativeRepresentation;

/**
 * Вид платежа.
 *
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public enum PaymentType implements AlternativeRepresentation {

    POST("Почтой"),
    TELEGRAPH("Телеграфом"),
    URGENTLY("Срочно"),
    DIGITAL("Электронно");

    private final String alternativeRepresentation;

    public static PaymentType from(String alternativeRepresentation) {
        return AlternativeRepresentation.from(PaymentType.class, alternativeRepresentation);
    }

    @Override
    public String alternativeRepresentation() {
        return alternativeRepresentation;
    }
}
