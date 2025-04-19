package cs2110;

import java.util.Iterator;
import java.util.NoSuchElementException; // Allowed dependency

/**
 * A Dictionary using a Trie, implementing StringDict<V>.
 * Keys must be Strings containing only 'A'-'Z' and '0'-'9'.
 * @param <V> The type of values stored.
 */
public class TrieStringDict<V> implements StringDict<V> {

    private static final int ALPHABET_SIZE = 36; // For '0'-'9' and 'A'-'Z'

    /** Represents a node in the Trie. */
    private static class TrieNode<V> {
        @SuppressWarnings("unchecked")
        TrieNode<V>[] children = (TrieNode<V>[]) new TrieNode<?>[ALPHABET_SIZE];
        boolean isEndOfWord = false;
        V value = null;
    }

    private final TrieNode<V> root; // Root of the Trie
    private int size;               // Number of keys stored

    /** Creates an empty TrieStringDict. */
    public TrieStringDict() {
        root = new TrieNode<>();
        size = 0;
    }

    /** Converts a valid character ('0'-'9', 'A'-'Z') to an index (0-35). */
    private int charToIndex(char c) {
        if (c >= '0' && c <= '9') return c - '0';
        if (c >= 'A' && c <= 'Z') return c - 'A' + 10;
        throw new IllegalArgumentException("Invalid character for Trie key: " + c);
    }

    /** Finds the node corresponding to the key. Returns null if prefix not found. */
    private TrieNode<V> findNode(String key) {
        TrieNode<V> current = root;
        for (int i = 0; i < key.length(); i++) {
            int index;
            try {
                index = charToIndex(key.charAt(i));
            } catch (IllegalArgumentException e) {
                throw e;
            }
            current = current.children[index];
            if (current == null) {
                return null; // Prefix doesn't exist
            }
        }
        return current;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Requires `key` is not null.
     * @throws NoSuchElementException if `key` is not found.
     * @throws NullPointerException if `key` is null.
     * @throws IllegalArgumentException if `key` contains invalid characters.
     */
    @Override
    public V get(String key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        TrieNode<V> node = findNode(key);
        if (node != null && node.isEndOfWord) {
            return node.value;
        }
        throw new NoSuchElementException("Key not found: " + key);
    }

    /**
     * Requires: `key` and `value` are not null.
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
            size++;
            current.isEndOfWord = true;
        }
        current.value = value;
    }

    /**
     * Requires `key` is not null.
     * @throws NullPointerException if `key` is null.
     * @throws IllegalArgumentException if `key` contains invalid characters.
     */
    @Override
    public boolean containsKey(String key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        TrieNode<V> node = null;
        try {
            node = findNode(key);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return node != null && node.isEndOfWord;
    }


    /**
     * Returns an iterator over the *values* stored in the dictionary.
     * Order is undefined.
     * @return an Iterator over the values (`V`).
     */
    @Override
    public Iterator<V> iterator() {
        DynamicArrayIndexedSeq<V> values = new DynamicArrayIndexedSeq<>();
        collectValues(root, values);
        return values.iterator();
    }

    /** Recursively collects values from the Trie into the sequence. */
    private void collectValues(TrieNode<V> node, DynamicArrayIndexedSeq<V> valueList) {
        if (node == null) return;

        if (node.isEndOfWord) {
            valueList.add(node.value);
        }

        for (TrieNode<V> child : node.children) {
            if (child != null) {
                collectValues(child, valueList);
            }
        }
    }
}