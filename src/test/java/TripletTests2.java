import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mpei.Containerable;
import ru.mpei.TripletDeque;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TripletTests2 {

    private Deque<String> tQueue;
    private Containerable cQueue = (Containerable) tQueue;

    @BeforeEach
    void beforeEach() {
        tQueue = new TripletDeque<>();
        cQueue = (Containerable) tQueue;
        tQueue.clear();
    }

    @Test
    void addTestNew() {
        tQueue.addLast("1");
        tQueue.addLast("2");
        tQueue.addLast("3");
        tQueue.addLast("4");
        tQueue.addLast("5");
        tQueue.addLast("6");
        tQueue.addLast("7");
        assertEquals("1", tQueue.getFirst());
        assertEquals("7", tQueue.getLast());
    }

    @Test
    void addTest() {
        tQueue.addFirst("one");
        tQueue.addFirst("two");
        assertEquals("two", tQueue.getFirst());
        assertEquals("one", tQueue.getLast());
        tQueue.addFirst("three");
        assertEquals("three", tQueue.getFirst());
        assertEquals("one", tQueue.getLast());
        tQueue.addFirst("four");
        tQueue.addLast("five");
        tQueue.addFirst("six");
        assertEquals("six", tQueue.getFirst());
        assertEquals("five", tQueue.getLast());
    }

    @Test
    void removeTest() {
        for (int i = 0; i < 15; i++) {
            tQueue.addFirst("n_" + i);
        }

        Assertions.assertTrue(tQueue.contains("n_3"));
        tQueue.remove("n_3");
        Assertions.assertFalse(tQueue.contains("n_3"));
    }

    @Test
    public void testIsEmptyAfterAddRemoveFirst() {
        tQueue.addFirst("Something");
        boolean empty = tQueue.isEmpty();
        assertFalse( empty );
        tQueue.removeFirst();

        empty = tQueue.isEmpty();
        assertTrue(empty);

    }

    @Test
    public void testAddFirst() {
        String[] aBunchOfString = {
                "One",
                "Two",
                "Three",
                "Four"
        };

        for(String aString : aBunchOfString){
            tQueue.addFirst(aString);
        }

        for(int i = aBunchOfString.length - 1; i >= 0; i--){
            assertEquals(aBunchOfString[i], tQueue.removeFirst());
        }
    }

    @Test
    public void testAddLast() {
        String[] aBunchOfString = {
                "One",
                "Two",
                "Three",
                "Four"
        };

        for(String aString : aBunchOfString){
            tQueue.addLast(aString);
        }

        for(int i = aBunchOfString.length - 1; i >= 0; i--){
            assertEquals(aBunchOfString[i], tQueue.removeLast());
        }
    }
}