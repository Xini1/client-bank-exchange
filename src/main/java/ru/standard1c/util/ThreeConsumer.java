package ru.standard1c.util;

/**
 * @author Maxim Tereshchenko
 */
public interface ThreeConsumer<T, U, R> {

    void accept(T t, U u, R r);
}
