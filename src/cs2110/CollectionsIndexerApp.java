package cs2110;

import java.io.*;
import java.util.*;

/**
 * Indexes words found in text files specified via program arguments, printing the index to
 * `System.out`.  Implemented using Java's Collections library.
 */
public class CollectionsIndexerApp {

    /**
     * Print an index of the words found in the files whose paths are provided in `args`.  The index
     * keys are the distinct words, after converting to upper case, in lexicographic order.  The
     * index entries are the paths to the files in which that word is found, deduplicated (by the
     * exact path provided in `args`) and in lexicographic order, each followed by the list of
     * distinct line numbers on which that word occurs (separated by spaces).  Each entry line is
     * indented with a tab character.
     */
    public static void main(String[] args) throws IOException {
        // TODO 1: Implement this method as specified.  You may use any collections and functions
        //  from `java.util` and `java.io`.  Do not define any new classes (stick with Java's
        //  collection types).  You may ignore all I/O errors.
        throw new UnsupportedOperationException();
    }
}
