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

    @DisplayName("Overwriting an existing key does not increase size")
    @Test
    void testOverwriteKey() {
        StringDict<String> dict = makeDict();
        dict.put("HELLO", "world");
        dict.put("HELLO", "universe");

        assertEquals(1, dict.size());
        assertTrue(dict.containsKey("HELLO"));
        assertEquals("universe", dict.get("HELLO"));
    }

    @DisplayName("get() throws NoSuchElementException when key is missing")
    @Test
    void testGetMissingKeyThrows() {
        StringDict<Integer> dict = makeDict();
        assertThrows(NoSuchElementException.class, () -> dict.get("MISSING"));
    }

    @DisplayName("Multiple unique keys can be stored and retrieved")
    @Test
    void testMultipleKeys() {
        StringDict<Integer> dict = makeDict();
        dict.put("A", 1);
        dict.put("B", 2);
        dict.put("C", 3);

        assertEquals(3, dict.size());
        assertEquals(1, dict.get("A"));
        assertEquals(2, dict.get("B"));
        assertEquals(3, dict.get("C"));
    }

    @DisplayName("Iterator returns all inserted values exactly once")
    @Test
    void testIteratorContents() {
        StringDict<String> dict = fromMap(Map.of(
                "A", "a",
                "B", "b",
                "C", "c"
        ));

        Set<String> values = new HashSet<>();
        for (String val : dict) {
            values.add(val);
        }

        assertEquals(Set.of("a", "b", "c"), values);
    }

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

    @DisplayName("findEntry returns empty slot for new key")
    @Test
    void testFindEntry_NewKey() {
        ProbingStringDict<Integer> dict = makeDict();
        int index = dict.findEntry("A");
        assertNull(dict.entries[index]);
    }

    @DisplayName("findEntry returns correct index for existing key")
    @Test
    void testFindEntry_ExistingKey() {
        ProbingStringDict<Integer> dict = makeDict();
        dict.put("Z", 9);
        int index = dict.findEntry("Z");
        assertEquals("Z", dict.entries[index].key());
    }

    @DisplayName("containsKey returns true if key is found")
    @Test
    void testContainsKey_Found() {
        ProbingStringDict<Integer> dict = makeDict();
        dict.put("KEY1", 111);
        assertTrue(dict.containsKey("KEY1"));
    }

    @DisplayName("containsKey returns false for missing key")
    @Test
    void testContainsKey_NotFound() {
        ProbingStringDict<Integer> dict = makeDict();
        assertFalse(dict.containsKey("NOPE"));
    }

    @DisplayName("get returns value if key is found")
    @Test
    void testGet_Found() {
        ProbingStringDict<Integer> dict = makeDict();
        dict.put("ITEM", 42);
        assertEquals(42, dict.get("ITEM"));
    }

    @DisplayName("get throws if key is missing")
    @Test
    void testGet_NotFound() {
        ProbingStringDict<Integer> dict = makeDict();
        assertThrows(NoSuchElementException.class, () -> dict.get("MISSING"));
    }

    @DisplayName("put adds new key and increases size")
    @Test
    void testPut_AddNew() {
        ProbingStringDict<Integer> dict = makeDict();
        assertEquals(0, dict.size());
        dict.put("X", 1);
        assertEquals(1, dict.size());
        assertEquals(1, dict.get("X"));
    }

    @DisplayName("put updates existing key without increasing size")
    @Test
    void testPut_Overwrite() {
        ProbingStringDict<Integer> dict = makeDict();
        dict.put("X", 1);
        dict.put("X", 99);
        assertEquals(1, dict.size());
        assertEquals(99, dict.get("X"));
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
