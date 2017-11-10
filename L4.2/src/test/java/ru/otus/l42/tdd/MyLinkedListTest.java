package ru.otus.l42.tdd;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class MyLinkedListTest {

    @Test
    public void add() throws Exception {

//        MyList list = new MyLinkedList();
//        list.add(1);
//        assertEquals(1, list.size());
//        assertEquals(1, list.get(0));
    }

    @Test
    public void removeFirst() throws Exception {

//        MyList list = new MyLinkedList();
//        list.add(1);
//        int val = list.removeFirst();
//        assertEquals(1, val);
//        assertEquals(0, list.size());
    }

    @Test(expected = ListException.class)
    public void removeFirst_Empty() throws Exception {
        MyList list = new MyLinkedList();
        list.removeFirst();
    }

    @Test
    public void size() throws Exception {
//        MyList list = new MyLinkedList();
//        assertEquals(0, list.size());
//        final int N = 10;
//        for (int i = 0; i < N; i++) {
//            list.add(i);
//        }
//        assertEquals(N, list.size());
    }

    @Test
    public void isEmpty() throws Exception {
        MyList list = new MyLinkedList();
        assertTrue(list.isEmpty());
        list.add(1);
        assertFalse(list.isEmpty());
    }

}