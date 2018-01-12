package ru.otus.l42.tdd;

/**
 *
 */
public interface MyList {

    int get(int pos);

    void add(int val);

    int removeFirst() throws ListException;

    int size();

    boolean isEmpty();

}
