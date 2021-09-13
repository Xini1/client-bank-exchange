package ru.standard1c.format;

import lombok.RequiredArgsConstructor;
import ru.standard1c.AlternativeRepresentation;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public enum Encoding implements AlternativeRepresentation {

    DOS("DOS"),
    WINDOWS("Windows");

    private final String alternativeRepresentation;

    public static Encoding from(String alternativeRepresentation) {
        return AlternativeRepresentation.from(Encoding.class, alternativeRepresentation);
    }

    @Override
    public String alternativeRepresentation() {
        return alternativeRepresentation;
    }
}
