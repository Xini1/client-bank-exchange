package ru.standard1c.reader.source;

import lombok.RequiredArgsConstructor;

import java.util.Scanner;

/**
 * Реализация {@link AttributeSource} на основе {@link Scanner}.
 *
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public class ScannerAttributeSource implements AttributeSource {

    private final Scanner scanner;

    @Override
    public Attribute next() {
        if (!hasNext()) {
            return null;
        }

        return Attribute.from(scanner.nextLine());
    }

    @Override
    public boolean hasNext() {
        return scanner.hasNextLine();
    }
}
