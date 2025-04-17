package cs2110;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

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
        StringDict<WordOccurrences> dict = makeStringDict();

        for(String filename : sortedSrcNames){
            File file = new File(filename);
            try(Scanner scanner = new Scanner(file)){
                int lineIndex = 0;

                while(scanner.hasNextLine()){
                    lineIndex++;
                    String line = scanner.nextLine();
                    String[] words = line.split("\\s+");

                    for(String word : words){
                        if(word.isEmpty()){
                            continue;
                        }
                        word = word.toUpperCase();

                        WordOccurrences occurrences;
                        if(dict.containsKey(word)){
                            occurrences = dict.get(word);
                        } else {
                            occurrences = new WordOccurrences(word);
                            dict.put(word, occurrences);
                        }


                        occurrences.addOccurrence(filename, lineIndex);
                    }
                }
            }
        }

        IndexedSeq<WordOccurrences> output = makeIndexedSeq();

        for(WordOccurrences value : dict){
            output.add(value);
        }

        output.sortDistinct();
        return output;
    }
}
