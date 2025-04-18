package cs2110;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * An Indexer that uses a sorted array plus binary search to serve as a string map.  Each source is
 * read twice: the first pass is used to assemble the unique keys, while the second pass is used to
 * accumulate the word occurrences.
 */
public class TwoPassIndexer extends Indexer {

    @Override
    public Iterable<WordOccurrences> index(Iterable<String> sortedSrcNames) throws IOException {
        // TODO 18: Implement this method as specified.  Must create an `IndexedSeq` of words (using
        //  `makeIndexedSeq()`), populate it with all words from all sources, and sort+deduplicate
        //  it.  Then must create a corresponding `IndexedSeq` of word occurrences, using binary
        //  search to update the appropriate one as each word is re-read from the sources.  You may
        //  only use methods declared in `IndexedSeq`, `WordOccurrences`, `Scanner`/`WordScanner`,
        //  `String`, `FileReader`, and `Iterable`/`Iterator`, as well as `Sorting.binarySearch()`.
        IndexedSeq<String> words = makeIndexedSeq();

        for (String file : sortedSrcNames){
            try (Scanner scanner = new Scanner(new File(file))) {
                while (scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    for (String word : line.split("\\s+")){
                        if (!word.isEmpty()){
                            words.add(word.toUpperCase());
                        }
                    }
                }
            }
        }

        words.sortDistinct();

        IndexedSeq<WordOccurrences> occurrences = makeIndexedSeq();
        for(int i=0; i < words.size(); i++){
            occurrences.add(new WordOccurrences(words.get(i)));
        }

        for (String file : sortedSrcNames){
            try (Scanner scanner = new Scanner(new File(file))){
                int lineIndex = 0;
                while (scanner.hasNextLine()){
                    lineIndex++;
                    String line = scanner.nextLine();
                    for (String word : line.split("\\s+")){
                        if(!word.isEmpty()){
                            int index = Sorting.binarySearch(words, word.toUpperCase());
                            occurrences.get(index).addOccurrence(file, lineIndex);
                        }
                    }
                }

            }
        }

        return occurrences;
    }

    /**
     * Create a `DynamicArrayIndexedSeq`.
     */
    @Override
    protected <T> DynamicArrayIndexedSeq<T> makeIndexedSeq() {
        return new DynamicArrayIndexedSeq<>();
    }
}
