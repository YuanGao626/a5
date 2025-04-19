package cs2110;

/**
 * Finds words that frequently appear on the same line as a target word.
 */
public class WordAssociates {

    /**
     * Find words found on the same line as `word` at least `threshold` times.
     * Comparison is case-insensitive. Only uses DynamicArrayIndexedSeq and parameter types.
     * @return An Iterable (DynamicArrayIndexedSeq) of associated words.
     */
    public static Iterable<String> associatedWords(Iterable<WordOccurrences> index, String word, int threshold) {
        // Store candidate words and their counts
        DynamicArrayIndexedSeq<String> candidateWords = new DynamicArrayIndexedSeq<>();
        DynamicArrayIndexedSeq<Integer> candidateCounts = new DynamicArrayIndexedSeq<>();
        DynamicArrayIndexedSeq<String> finalResult = new DynamicArrayIndexedSeq<>();

        String targetUpperWord = word.toUpperCase();
        WordOccurrences target = findWordOccurrences(index, targetUpperWord);

        if (target == null) {
            return finalResult;
        }

        // Check every other word in the index
        for (WordOccurrences potentialAssociateWo : index) {
            String potentialAssociateUpperWord = potentialAssociateWo.word();
            if (potentialAssociateUpperWord.equals(targetUpperWord)) {
                continue; // Skip self
            }

            // Count co-occurrences with the target word
            int coOccurrenceCount = 0;
            for (SourceLines targetSl : target.sources()) {
                String currentSource = targetSl.sourceName();
                for (int targetLineNum : targetSl.lines()) {
                    if (checkCoOccurrence(potentialAssociateWo, currentSource, targetLineNum)) {
                        coOccurrenceCount++;
                    }
                }
            }

            if (coOccurrenceCount > 0) {
                updateCounts(candidateWords, candidateCounts, potentialAssociateUpperWord, coOccurrenceCount);
            }
        }

        for (int i = 0; i < candidateWords.size(); i++) {
            if (candidateCounts.get(i) >= threshold) {
                finalResult.add(candidateWords.get(i));
            }
        }

        return finalResult;
    }

    /** Finds WordOccurrences for `upperWord`. Returns null if not found. */
    private static WordOccurrences findWordOccurrences(Iterable<WordOccurrences> index, String upperWord) {
        for (WordOccurrences wo : index) {
            if (wo.word().equals(upperWord)) {
                return wo;
            }
        }
        return null;
    }

    /** Checks if `wo` occurs in `filename` on `lineNum`. */
    private static boolean checkCoOccurrence(WordOccurrences wo, String filename, int lineNum) {
        for (SourceLines sl : wo.sources()) {
            if (sl.sourceName().equals(filename)) {
                for (int currentLine : sl.lines()) {
                    if (currentLine == lineNum) return true;
                }
                return false;
            }
        }
        return false;
    }

    /** Updates counts for `word` in parallel sequences `words` and `counts`. */
    private static void updateCounts(DynamicArrayIndexedSeq<String> words, DynamicArrayIndexedSeq<Integer> counts, String word, int countToAdd) {
        int foundIndex = -1;
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).equals(word)) {
                foundIndex = i;
                break;
            }
        }

        if (foundIndex != -1) { // Word exists
            counts.set(foundIndex, counts.get(foundIndex) + countToAdd);
        } else { // New word
            words.add(word);
            counts.add(countToAdd);
        }
    }
}