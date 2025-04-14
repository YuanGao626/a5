package cs2110;

/**
 * Namespace for functions related to sorting/searching indexed sequences.
 */
public class Sorting {

    /**
     * Remove duplicate copies of the elements in `items`, retaining one copy of each distinct
     * element in their original relative order.  Thus, upon return, `items.size()` is the number of
     * distinct values originally in `items`.  Requires `items` is sorted such that duplicate values
     * are all adjacent to one another.
     */
    public static <T> void deduplicateSorted(IndexedSeq<T> items) {
        // TODO 4: Implement this method as specified.  You may only call methods declared in
        //  `IndexedSeq` or `Object`.
        throw new UnsupportedOperationException();
    }

    /**
     * Sort the distinct elements in `items` in ascending order, discarding duplicate copies.  Thus,
     * upon return, `items.size()` is the number of distinct values originally in `items`.
     */
    public static <T extends Comparable<? super T>> void sortDistinct(IndexedSeq<T> items) {
        int newSize = quicksortDistinct(items, 0, items.size(), 0);
        items.truncate(newSize);
    }

    /**
     * Return the median value among `{a, b, c}`.  The median has the property that at least half of
     * the elements are less than or equal to it and at least half of the elements are greater than
     * or equal to it.
     */
    static <T extends Comparable<? super T>> T med3(T a, T b, T c) {
        // TODO 6: Implement this method as specified.  You may only call methods declared in
        //  `Comparable` or `Object`.
        throw new UnsupportedOperationException();
    }

    /**
     * Sort the distinct elements originally in `items[begin..end)` in ascending order, storing them
     * in `items[dst..ret)` and returning `ret`.  Note that the number of distinct elements in the
     * input range is thus given by `ret - dst`.  Upon return, the contents of `items[ret..end)` is
     * unspecified (clients are encouraged to overwrite or clear this range to avoid memory leaks).
     * Requires {@code 0 <= dst <= begin <= end <= items.size()}.
     */
    static <T extends Comparable<? super T>> int quicksortDistinct(IndexedSeq<T> items, int begin,
            int end, int dst) {
        // TODO 9: Implement this method as specified using the QuickSort algorithm.  Use the median
        //  of the first, middle, and last values of the input range as the pivot, and use
        //  `partitionShiftedDiscardPivot()` for the partitioning step.  You may only call methods
        //  declared in `IndexedSeq` or `IPair`, as well as other methods in `Sorting`.
        throw new UnsupportedOperationException();
    }

    /**
     * Partition the elements originally in `items[begin..end)` about `pivot`, storing smaller
     * values in `items[dst..i)` and larger values in `items[j..end)`, and return the pair (`i`,
     * `j`).  Note: values equal to the pivot are not included in either partition.  Upon return,
     * {@code items[dst..i) < pivot}, {@code items[j..end) > pivot}, and `items[i..j)` are
     * unspecified (clients are encouraged to overwrite or clear this range to avoid memory leaks).
     * Requires {@code 0 <= dst <= begin <= end <= items.size()}.
     */
    static <T extends Comparable<? super T>> IPair partitionShiftedDiscardPivot(IndexedSeq<T> items,
            int begin, int end, int dst, T pivot) {
        // TODO 7: Implement this method as specified.  You may only call methods declared in
        //  `IndexedSeq`, `Comparable`, or `Object`, as well as `IPair`'s constructor.  Write a
        //  comment documenting your loop invariant above the initialization of your loop variables.
        throw new UnsupportedOperationException();
    }

    /**
     * Return the index `k` in `[0..seq.size]` such that  `seq[i] < target` for `i < k` and `seq[i]
     * >= target` for `i >= k`.  Requires `seq` is sorted in ascending order.
     */
    public static <T extends Comparable<? super T>> int binarySearch(IndexedSeq<T> seq, T target) {
        // Loop invariant: `0 <= begin <= end <= seq.length`, `seq[..begin) < target`,
        //  `seq[end..] >= target`.
        int begin = 0;
        int end = seq.size();

        // Decrementing function: end - begin
        while (begin != end) {
            // Determine midpoint index (overflow-safe).  Guarantees `begin <= m < end`.
            int m = begin + (end - begin) / 2;

            if (seq.get(m).compareTo(target) < 0) {
                // if seq[m] < target, then m < ans; eliminate left half.
                begin = m + 1;
            } else {
                // ans <= m; eliminate right half.
                end = m;
            }
        }

        // When the loop guard becomes false, all elements have been eliminated; only one legal
        //  index to return.
        return begin;
    }
}
