package cs2110;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A dictionary mapping String keys to values of type `V`, implemented using a hash table with
 * linear probing. Employs a custom hash function for keys.
 */
public class ProbingStringDict<V> implements StringDict<V> {
    // Note: Many members of this class that would normally be `private` have instead been declared
    //  as `protected` to facilitate testing and to emphasize that they should not be changed.

    /**
     * Represents an association of a key `key` (of type `K`) with a value `value` (of type `V`).
     */
    protected record Entry<K, V>(K key, V value) {
        // A "record" class will automatically generate a constructor `Entry(key, value)` and
        //  accessors `key()` and `value()`, as well as overrides for `toString()`, `equals()`, and
        //  `hashCode()`.  It is otherwise just a nested, static (not inner) class.
    }

    /**
     * The hash table.  Elements without an entry are null.  For keys contained in this dictionary,
     * if the key's hash code maps to index `i`, then the (unique) entry containing that key will be
     * reachable via linear search starting at element `i` (wrapping around the array if necessary)
     * without encountering null.  Because this dictionary does not support removing keys, there is
     * no need to represent "tombstones".
     */
    protected Entry<String, V>[] entries;

    /**
     * The number of keys currently mapped to values in this dictionary.  Confined to
     * `[0..entries.length]`.
     */
    protected int size;

    /**
     * Maximum allowed value for the ratio of `size` to `entries.length`.  If the load factor were
     * to exceed this value after adding a new key, then the hash table must be enlarged so that the
     * load factor does not exceed this value.  Must be positive.
     */
    protected double maxLoadFactor;


    /**
     * The initial capacity of the hash table for new instances of `ProbingStringDict`.
     */
    protected static final int INITIAL_CAPACITY = 16;

    /**
     * The initial maximum load factor for new instances of `ProbingStringDict`.
     */
    private static final double DEFAULT_MAX_LOAD_FACTOR = 0.5;


    /**
     * Create a new empty `ProbingStringDict`.
     */
    public ProbingStringDict() {
        @SuppressWarnings("unchecked")
        Entry<String, V>[] entries = new Entry[INITIAL_CAPACITY];
        this.entries = entries;
        size = 0;
        maxLoadFactor = DEFAULT_MAX_LOAD_FACTOR;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Derive a hash code from the key `key`.  May return any `int` value, but the same key will
     * always produce the same hash code.  Should be resistant to collisions for closely-related
     * keys.
     */
    protected int hash(String key) {
        // TODO 12: Implement this method as specified.  We expect the only methods you need will be
        //  `String.charAt()` and `String.length()`, though `Math` functions would be fine too.  Do
        //  not use `hashCode()` or `Objects.hash()` or `Arrays.hashCode()`.  Be creative!
        int even = 0, odd = 0;

        for (int i = 0; i < key.length(); i++) {
            if (i % 2 == 0) {
                even += key.charAt(i);
            } else {
                odd += key.charAt(i);
            }
        }

        return (even * 3 + odd * 7) & 0x7FFFFFFF;
    }

    /**
     * Return the current ratio of the number of keys in this dictionary to its backing hash table's
     * size.
     */
    protected double loadFactor() {
        // TODO 13: Implement this method as specified.
        return (double) size / entries.length;
    }

    /**
     * If `key` is a key in this dictionary, return the index of the entry in `entries` for this
     * key. Otherwise, return the first index of a null entry in the table at or after the index
     * corresponding to the key's hash code (wrapping around).  Throws `NoSuchElementException` if
     * the key is not in this dictionary and the table is full.
     */
    protected int findEntry(String key) {
        // TODO 14: Implement this method as specified.
        int index = hash(key) % entries.length;

        for(int i=0; i<entries.length; i++){
            Entry<String, V> entry = entries[index];

            if (entry == null){
                return index;
            } else if (entry.key.equals(key)){
                return index;
            }

            index = (index + 1) % entries.length;
        }

        throw new NoSuchElementException("Table is full, key can't be placed");
    }

    @Override
    public boolean containsKey(String key) {
        // TODO 15: Implement this method as specified.  `findEntry()` will probably be useful.
        int index = findEntry(key);

        if(entries[index] != null && entries[index].key.equals(key)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public V get(String key) {
        // TODO 16: Implement this method as specified.  `findEntry()` will probably be useful.
        int index = findEntry(key);

        if(entries[index] == null || !entries[index].key.equals(key)){
            throw new NoSuchElementException();
        }

        return entries[index].value;
    }

    @Override
    public void put(String key, V value) {
        // TODO 17: Implement this method as specified.  `findEntry()` will probably be useful.
        //  You may define additional (private) helper methods as well if you like.
        assert key != null;
        assert value != null;

        int index = findEntry(key);

        if(entries[index] == null){
            entries[index] = new Entry<>(key, value);
            size++;
        } else {
            entries[index] = new Entry<>(key, value);
        }
    }

    @Override
    public Iterator<V> iterator() {
        return new Itr();
    }


    /**
     * An iterator over the values in this hash table.  This dictionary must not be structurally
     * modified while any such iterators are alive.
     */
    private class Itr implements Iterator<V> {

        /**
         * The index of the entry in `entries` containing the next value to yield, or
         * `entries.length` if all values have been yielded.
         */
        private int iNext;

        /**
         * Create a new iterator over this dictionary's values.
         */
        Itr() {
            iNext = 0;
            findNext();
        }

        /**
         * Set `iNext` to the first index `i` not less than the current value of `iNext` such that
         * `entries[i] != null`, or set it to `entries.length` if there are no remaining non-null
         * entries.  Note that if `iNext` is already the index of a non-null entry, then it will not
         * be changed.
         */
        private void findNext() {
            while (iNext < entries.length && entries[iNext] == null) {
                iNext += 1;
            }
        }

        @Override
        public boolean hasNext() {
            return iNext < entries.length;
        }

        @Override
        public V next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            V ans = entries[iNext].value;
            iNext += 1;
            findNext();
            return ans;
        }
    }
}
