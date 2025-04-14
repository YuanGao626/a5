package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SortingTest {

    /**
     * Create an `IndexedSeq` with `items` as its elements.
     */
    @SafeVarargs
    static <T extends Comparable<? super T>> IndexedSeq<T> makeSeq(T... items) {
        return new JavaIndexedSeq<>(new ArrayList<>(Arrays.asList(items)));
    }

    @DisplayName("deduplicateSorted()")
    @Nested
    class DeduplicateSortedTest {

        @DisplayName("will have no effect on a seq containing no duplicate values")
        @Test
        void testNoDups() {
            IndexedSeq<Integer> items = makeSeq(0, 1, 2, 3, 4);
            int originalSize = items.size();
            Sorting.deduplicateSorted(items);

            assertEquals(originalSize, items.size());
            // Use `assertIterableEquals()` rather than `assertEquals()` so that we can use Java
            //  lists for our expected value.
            assertIterableEquals(List.of(0, 1, 2, 3, 4), items);
        }

        // TODO 5: Write at least two additional testcases to thoroughly cover
        //  `deduplicateSorted()`.
    }

    // Tests of `med3()` from A1.  You are welcome to add additional testcases if you like.
    @Nested
    @DisplayName("med3()")
    class Med3Test {

        @Test
        @DisplayName("computes the median of distinct, sorted values")
        void testSorted() {
            assertEquals(2, Sorting.med3(1, 2, 3));
        }

        @Test
        @DisplayName("computes the median of distinct, unsorted values")
        void testUnsorted() {
            // Observe here how a single test case can make multiple assertions.
            assertEquals(2, Sorting.med3(2, 1, 3));
            assertEquals(2, Sorting.med3(1, 3, 2));
            assertEquals(2, Sorting.med3(3, 2, 1));
        }

        @Test
        @DisplayName("computes the median in the presence of negative numbers")
        void testNegative() {
            assertEquals(0, Sorting.med3(-3, 0, 4));
        }

        @Test
        @DisplayName("computes the median when two values are duplicates")
        void testDup2() {
            assertEquals(1, Sorting.med3(1, 2, 1));
            assertEquals(2, Sorting.med3(2, 2, 1));
        }

        @Test
        @DisplayName("computes the median when all three values are the same")
        void testDup3() {
            assertEquals(1, Sorting.med3(1, 1, 1));
        }

        @Test
        @DisplayName("computes the median in the presence of extreme values")
        void testExtreme() {
            assertEquals(2, Sorting.med3(Integer.MIN_VALUE, 2, Integer.MAX_VALUE));
            assertEquals(Integer.MAX_VALUE,
                    Sorting.med3(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
        }
    }

    @DisplayName("partitionShiftedDiscardPivot()")
    @Nested
    class PartitionShiftedDiscardPivotTest {

        @DisplayName("Successfully shifts to the beginning of the array when pivot is present")
        @Test
        void testDstZero() {
            IndexedSeq<Character> items = makeSeq('X', 'X', 'Z', 'A', 'M');
            int originalSize = items.size();
            IPair ij = Sorting.partitionShiftedDiscardPivot(items, 2, 5, 0, 'M');

            // Seq's size should not have been affected
            assertEquals(originalSize, items.size());

            // Check return value(s)
            assertEquals(1, ij.i());
            assertEquals(4, ij.j());

            // Check elements in partitions (in this case, since only one element is in each
            //  partition, we know exactly where each will be)
            assertEquals('A', items.get(0));
            assertEquals('Z', items.get(4));
            // Cannot make any claims about `items[i..j)`.
        }

        // TODO 8: Write additional testcases to thoroughly cover `partitionShiftedDiscardPivot()`.
        //  Be sure to cover the following situations:
        //  * Pivot is not present; pivot is present more than once
        //  * `dst` is equal to `begin`; `dst` is not 0
        //  * `end` is not `items.size()`
        //  * There are no values smaller/larger than the pivot
        //  A single testcase may be able to cover multiple of the above situations; use
        //  DisplayNames and/or comments to clearly indicate which situations are being covered in
        //  each case.  Be careful not to assert more than is guaranteed by the spec (any valid
        //  implementation must pass your tests).
    }

    @DisplayName("quicksortDistinct()")
    @Nested
    class SortDistinctTest {

        @DisplayName("has no effect on a seq that is already sorted and distinct")
        @Test
        void testAlreadySortedDistinct() {
            IndexedSeq<Integer> items = makeSeq(0, 1, 2, 3, 4);
            int originalSize = items.size();
            Sorting.sortDistinct(items);

            assertEquals(originalSize, items.size());
            // Use `assertIterableEquals()` rather than `assertEquals()` so that we can use Java
            //  lists for our expected value.
            assertIterableEquals(List.of(0, 1, 2, 3, 4), items);
        }

        // TODO 10: Write at least two additional testcases to thoroughly cover
        //  `quicksortDistinct()`.
    }
}
