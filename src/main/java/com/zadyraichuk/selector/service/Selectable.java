package com.zadyraichuk.selector.service;

import java.util.function.Supplier;

/**
 * <p>Collection that allow to select only one value in defined way</p>
 * @param <E> type of stored objects
 */
public interface Selectable<E> {

    E select();

    default E select(Supplier<E> supplier) {
        return supplier.get();
    }

}
