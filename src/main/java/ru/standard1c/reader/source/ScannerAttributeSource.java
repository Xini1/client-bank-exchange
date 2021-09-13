package ru.standard1c.reader.source;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Scanner;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public class ScannerAttributeSource implements AttributeSource {

    private final Scanner scanner;

    @Override
    public Optional<Attribute> next() {
        if (!hasNext()) {
            return Optional.empty();
        }

        return Optional.of(Attribute.from(scanner.nextLine()));
    }

    @Override
    public boolean hasNext() {
        return scanner.hasNextLine();
    }
}
