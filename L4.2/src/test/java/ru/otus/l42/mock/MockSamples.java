package ru.otus.l42.mock;

import java.util.Iterator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class MockSamples {

    @Test
    public void iteratorTest() {
        Iterator i = mock(Iterator.class);
        when(i.next())
                .thenReturn("one")
                .thenReturn("two");

        String result = i.next() + " " + i.next();
        assertEquals("one two", result);
    }
}
