package cs2110;

import java.util.Iterator;
import java.util.NoSuchElementException; // Required for get()

/**
 * A Dictionary using a Trie. Keys are Strings of 'A'-'Z' and '0'-'9'.
 * Implements the cs2110.StringDict interface.
 *
 * @param <V> The type of values stored.
 */
public class TrieStringDict<V> implements StringDict<V> {

    private static final int ALPHABET_SIZE = 36; // '0'-'9' + 'A'-'Z'

    /** Represents a node in the Trie. */
    private static class TrieNode<V> {
        TrieNode<V>[] children; // Child nodes for next characters
        boolean isEndOfWord;    // Does this node mark the end of a stored key?
        V value;                // Value if this node is the end of a key

        /** Creates a new TrieNode. */
        @SuppressWarnings("unchecked")
        TrieNode() {
            children = (TrieNode<V>[]) new TrieNode<?>[ALPHABET_SIZE];
            // isEndOfWord defaults to false, value defaults to null
        }
    }

    private final TrieNode<V> root; // Root of the Trie
    private int size;               // Number of keys stored

    /** Creates an empty TrieStringDict. */
    public TrieStringDict() {
        root = new TrieNode<>();
        size = 0;
    }

    /** Converts a character ('0'-'9', 'A'-'Z') to an index (0-35). */
    private int charToIndex(char c) {
        if (c >= '0' && c <= '9') return c - '0';
        if (c >= 'A' && c <= 'Z') return c - 'A' + 10;
        throw new IllegalArgumentException("Invalid character for Trie key: " + c);
    }

    /** Finds the node for the given key. Returns null if key prefix not found. */
    private TrieNode<V> getNode(String key) {
        TrieNode<V> current = root;
        for (int i = 0; i < key.length(); i++) {
            int index = charToIndex(key.charAt(i));
            current = current.children[index];
            if (current == null) return null;
        }
        return current;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Requires: `key` is not null and contains only '0'-'9' or 'A'-'Z'.
     * @throws NoSuchElementException if `key` is not found in the dictionary.
     * @throws NullPointerException if `key` is null.
     * @throws IllegalArgumentException if `key` contains invalid characters.
     */
    @Override
    public V get(String key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        TrieNode<V> node = getNode(key); // Handles illegal chars
        if (node != null && node.isEndOfWord) {
            if (node.value == null) {
                throw new NoSuchElementException("Key found but value is unexpectedly null: " + key);
            }
            return node.value;
        }
        throw new NoSuchElementException("Key not found: " + key);
    }

    /**
     * Requires: `key` and `value` are not null. `key` contains only '0'-'9' or 'A'-'Z'.
     * @throws NullPointerException if `key` or `value` is null.
     * @throws IllegalArgumentException if `key` contains invalid characters.
     */
    @Override
    public void put(String key, V value) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        if (value == null) throw new NullPointerException("Value cannot be null");

        TrieNode<V> current = root;
        for (int i = 0; i < key.length(); i++) {
            int index = charToIndex(key.charAt(i));
            if (current.children[index] == null) {
                current.children[index] = new TrieNode<>();
            }
            current = current.children[index];
        }

        if (!current.isEndOfWord) {
            // Key is new
            size++;
            current.isEndOfWord = true;
        }
        // Set/update the value
        current.value = value;
    }

    /**
     * Requires: `key` is not null and contains only '0'-'9' or 'A'-'Z'.
     * @throws NullPointerException if `key` is null.
     * @throws IllegalArgumentException if `key` contains invalid characters.
     */
    @Override
    public boolean containsKey(String key) {
        if (key == null) throw new NullPointerException("Key cannot be null");
        TrieNode<V> node = getNode(key);
        return node != null && node.isEndOfWord;
    }

    /**
     * Returns an iterator over the values in this dictionary.
     * Order is undefined. Iterator does not support remove().
     * @return an Iterator over the values (`V`).
     */
    @Override
    public Iterator<V> iterator() {
        DynamicArrayIndexedSeq<V> values = new DynamicArrayIndexedSeq<>();
        collectValues(root, values);
        return values.iterator();
    }

    /** Recursively collects all non-null values into the `values` sequence. */
    private void collectValues(TrieNode<V> node, DynamicArrayIndexedSeq<V> values) {
        if (node == null) {
            return;
        }

        if (node.isEndOfWord) {
            values.add(node.value);
        }

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            if (node.children[i] != null) {
                collectValues(node.children[i], values);
            }
        }
    }
}