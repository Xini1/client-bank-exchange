package ru.standard1c.reader;

import lombok.Value;

/**
 * @author Maxim Tereshchenko
 */
@Value
public class Attribute {

    String key;
    String value;

    public static Attribute from(String line) {
        var attributeArray = line.split("=");

        return new Attribute(
                attributeArray[0],
                attributeArray.length == 1 ? line.contains("=") ? "" : null : attributeArray[1]
        );
    }

    public boolean hasKey(String key) {
        return this.key.equals(key);
    }
}
