import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

public final class TopKFrequentWords {

    private TopKFrequentWords() {
    }

    /**
     * @param words input array of words (may contain nulls; they are ignored)
     * @param k     number of top elements to return (k <= 0 -> empty list)
     * @return unmodifiable list of top-k words (size <= k)
     * @throws NullPointerException if words is null
     */
    public static List<String> topKFrequent(String[] words, int k) {
        Objects.requireNonNull(words, "words must not be null");

        if (k <= 0) {
            return Collections.emptyList();
        }

        Map<String, Integer> freq = new HashMap<>();
        for (String w : words) {
            if (w == null)
                continue;
            freq.merge(w, 1, Integer::sum);
        }

        if (freq.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(freq.entrySet());
        entries.sort((e1, e2) -> {
            int cmp = e2.getValue().compareTo(e1.getValue());
            if (cmp != 0)
                return cmp;
            return e1.getKey().compareTo(e2.getKey());
        });

        int limit = Math.min(k, entries.size());
        List<String> result = new ArrayList<>(limit);
        for (int i = 0; i < limit; i++) {
            result.add(entries.get(i).getKey());
        }

        return Collections.unmodifiableList(result);
    }

    public static List<String> topKFrequentUsingHeap(String[] words, int k) {
        Objects.requireNonNull(words, "words must not be null");

        if (k <= 0) {
            return Collections.emptyList();
        }

        Map<String, Integer> freq = new HashMap<>();
        for (String w : words) {
            if (w == null)
                continue;
            freq.merge(w, 1, Integer::sum);
        }

        Comparator<String> comparator = (a, b) -> {
            int fa = freq.get(a);
            int fb = freq.get(b);
            if (fa != fb)
                return Integer.compare(fa, fb);
            return b.compareTo(a);
        };

        PriorityQueue<String> heap = new PriorityQueue<>(comparator);

        for (String word : freq.keySet()) {
            heap.offer(word);
            if (heap.size() > k)
                heap.poll();
        }

        List<String> result = new ArrayList<>(heap.size());
        while (!heap.isEmpty())
            result.add(heap.poll());
        Collections.reverse(result);

        return Collections.unmodifiableList(result);
    }

    public static void main(String[] args) {
        String[] words1 = { "java", "python", "java", "golang", "java", "python" };
        String[] words2 = { "java", "python", "java", "golang", "java", "python",
                "ruby", "ruby", "ruby", "ruby" };

        printTest("Test 1 (sort)", words1, 2, TopKFrequentWords::topKFrequent);
        printTest("Test 2 (sort)", words2, 4, TopKFrequentWords::topKFrequent);

        printTest("Test 2 (heap)", words2, 4, TopKFrequentWords::topKFrequentUsingHeap);
    }

    private static void printTest(String title, String[] words, int k,
            BiTopKFunction fn) {
        System.out.println("=== " + title + " ===");
        System.out.println("Input: " + Arrays.toString(words));
        System.out.println("k = " + k);
        List<String> out = fn.apply(words, k);
        System.out.println("Output: " + out);
        System.out.println();
    }

    @FunctionalInterface
    private interface BiTopKFunction {
        List<String> apply(String[] words, int k);
    }
}