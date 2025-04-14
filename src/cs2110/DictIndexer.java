package cs2110;

import java.io.FileReader;
import java.io.IOException;

/**
 * An indexer that takes advantage of a `StringDict`.  Subclasses determine the implementation of
 * the dictionary and list types used.
 */
public abstract class DictIndexer extends Indexer {

    /**
     * Create an empty dictionary mapping strings to values of type `V`.  Such a dictionary will be
     * used to associate words with `WordOccurrences` objects.
     */
    protected abstract <V> StringDict<V> makeStringDict();

    @Override
    public Iterable<WordOccurrences> index(Iterable<String> sortedSrcNames) throws IOException {
        // TODO 3: Implement this method as specified.  Must create a
        //  `StringDict<WordOccurrences>` using `makeStringDict()` and use it to track the
        //  occurrences of each encountered word.  You may only use methods declared in
        //  `IndexedSeq`, `StringDict`, `WordOccurrences`, `FileReader`, `Scanner` or `WordScanner`,
        //  `String`, and `Iterable`/`Iterator`.  If you need a sequence, use `makeIndexedSeq()`.
        throw new UnsupportedOperationException();
    }
}
