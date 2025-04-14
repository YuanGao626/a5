package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for `StringDict` implementations.  Subclasses must provide a way to create a dict
 * instance to test.
 */
abstract class StringDictTest {
    // Note: For compatibility with the `TrieStringDict` challenge extension, be sure the tests in
    //  this class only use keys with capital letters and digits.

    /**
     * Create a new, empty dictionary of the class to be tested (mapping String keys to values of
     * type `V`).
     */
    abstract <V> StringDict<V> makeDict();

    /**
     * Create a new dictionary of the class to be tested containing the same mappings as `map`.
     * Requires `map`'s keys and values are non-null.
     */
    <V> StringDict<V> fromMap(Map<String, V> map) {
        StringDict<V> dict = makeDict();
        for (Map.Entry<String, V> kv : map.entrySet()) {
            // Deliberately avoid string aliasing
            dict.put(new String(kv.getKey()), kv.getValue());
        }
        return dict;
    }

    @DisplayName("A newly-created dict is initially empty")
    @Test
    void testConstruction() {
        StringDict<Integer> dict = makeDict();
        assertEquals(0, dict.size());

        assertFalse(dict.iterator().hasNext());
    }

    @DisplayName("Associating a value with a new key in a dict will increase its size by 1, the "
            + "key will be contained in the dict, and the mapped value will be returned by "
            + "`get()`.")
    @Test
    void testAddition() {
        StringDict<Integer> dict = makeDict();
        dict.put(new String("KEY"), 42);
        assertEquals(1, dict.size());
        assertTrue(dict.containsKey(new String("KEY")));
        assertEquals(42, dict.get(new String("KEY")));
    }

    // TODO 11: Write additional testcases to thoroughly cover `StrictDict` functionality.  Your
    //  test suite will be evaluated on whether or not it catches common bugs.
    //  If you plan to complete the `TrieStringDict` challenge extension, be sure these tests only
    //  use keys with capital letters and digits.
}

/**
 * Test suite for `JavaStringDict`.
 */
class JavaStringDictTest extends StringDictTest {

    @Override
    <V> StringDict<V> makeDict() {
        return new JavaStringDict<>(new HashMap<>());
    }
}

/**
 * Test suite for `ProbingStringDict`.  Includes additional testcases specific to that
 * implementation.
 */
class ProbingStringDictTest extends StringDictTest {

    @Override
    <V> ProbingStringDict<V> makeDict() {
        return new ProbingStringDict<>();
    }

    // This is an example of testing functionality that is specific to `ProbingStringDict` (that is,
    //  these tests would not make sense for other `StringDict` implementations).  You may add your
    //  own tests here to aid in debugging (note: such tests do not count towards TODO 11).

    @DisplayName("The load factor of an empty dict must be 0")
    @Test
    void testLoadFactorEmpty() {
        ProbingStringDict<Integer> dict = makeDict();
        assertEquals(0, dict.loadFactor());
    }

}

// TODO (challenge extension): Uncomment this after implementing `TrieStringDict`.

///**
// * Test suite for `TrieStringDict`.  Only valid if superclass restricts keys to strings containing
// * only capital letters and digits.
// */
//class TrieStringDictTest extends StringDictTest {
//
//    @Override
//    <V> StringDict<V> makeDict() {
//        return new TrieStringDict<>();
//    }
//}
