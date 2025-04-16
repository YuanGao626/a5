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
        Map<String, Map<String, Set<Integer>>> index = new HashMap<>();
        Set<String> files = new HashSet<>();

        Collections.addAll(files, args);

        for (String eachFile : files){
            File file = new File(eachFile);
            try (Scanner scanner = new Scanner(file)) {
                int lineIndex = 0;
                while (scanner.hasNextLine()){
                    lineIndex++;
                    String line = scanner.nextLine();
                    String[] words = line.split("\\s+");
                    for (String word : words){
                        if (word.isEmpty()){
                            continue;
                        }
                        word = word.toUpperCase();

                        if(!index.containsKey(word)){
                            index.put(word, new HashMap<>());
                        }

                        Map<String, Set<Integer>> fileMap = index.get(word);

                        if (!fileMap.containsKey(eachFile)){
                            fileMap.put(eachFile, new HashSet<>());
                        }

                        fileMap.get(eachFile).add(lineIndex);

                    }
                }
            }
        }

        List<String> sortedWords = new ArrayList<>(index.keySet());
        Collections.sort(sortedWords);

        for (String word : sortedWords){
            System.out.println(word);

            Map<String, Set<Integer>> fileMap = index.get(word);

            List<String> sortedFIles = new ArrayList<>(fileMap.keySet());
            Collections.sort(sortedFIles);

            for (String file : sortedFIles){
                System.out.print("\t" + file);

                List<Integer> sortedLines = new ArrayList<>(fileMap.get(file));
                Collections.sort(sortedLines);

                for(int line : sortedLines){
                    System.out.print(" " + line);
                }

                System.out.println();
            }
        }

    }
}
