package com.walmart.routeplanner.services.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Simple implementation of a Buffer using a {@link List}.<br>
 * The list is not synchronized.<br>
 * The caller is responsible to check if the buffer is full,
 * perform the flush getting elements from {@link BufferList#iterator()} and
 * clear the buffer after that through {@link BufferList#empty()}.
 *
 * @param <E> Element type
 *
 * @author Rodrigo Marchesini
 */
public class BufferList<E> implements Iterable<E> {
    private List<E> elements;
    private final int capacity;

    public BufferList(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Invalid capacity: " + capacity);
        }
        this.capacity = capacity;
        this.elements = new ArrayList<E>(this.capacity);
    }

    public void add(E e) {
        elements.add(e);
    }

    public boolean isFull() {
        return elements.size() >= this.capacity;
    }

    @Override
    public Iterator<E> iterator() {
        return elements.iterator();
    }

    public void empty() {
        elements.clear();
    }
}