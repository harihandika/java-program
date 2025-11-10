import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class WordCounter {

    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern PUNCTUATION = Pattern.compile("\\p{Punct}+");

    private WordCounter() {
    }

    public static Map<String, Integer> countWords(String text) {
        Objects.requireNonNull(text, "text must not be null");

        if (text.trim().isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Integer> counts = new LinkedHashMap<>();
        String[] tokens = WHITESPACE.split(text);

        for (String raw : tokens) {
            String word = raw.trim().toLowerCase();
            if (word.isEmpty())
                continue;
            counts.merge(word, 1, Integer::sum);
        }

        return Collections.unmodifiableMap(counts);
    }

    public static Map<String, Integer> countWordsStripped(String text) {
        Objects.requireNonNull(text, "text must not be null");

        if (text.trim().isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Integer> counts = new LinkedHashMap<>();
        String[] tokens = WHITESPACE.split(text);

        for (String raw : tokens) {
            String cleaned = PUNCTUATION.matcher(raw).replaceAll("").trim().toLowerCase();
            if (cleaned.isEmpty())
                continue;
            counts.merge(cleaned, 1, Integer::sum);
        }

        return Collections.unmodifiableMap(counts);
    }

    private static String prettyPrint(Map<String, Integer> map) {
        return map.entrySet().stream()
                .map(e -> e.getKey() + " -> " + e.getValue())
                .collect(Collectors.joining(", "));
    }

    private static boolean equalsMap(Map<String, Integer> a, Map<String, Integer> b) {
        return Objects.equals(a, b);
    }

    /* ---------- Main: 5 test cases ---------- */
    public static void main(String[] args) {
        System.out.println("Running WordCounter tests...\n");

        // Test 1: sample from user (note the trailing period to match example: test. ->
        // 2)
        String t1 = "This is a test. This is only a test.";
        Map<String, Integer> exp1 = Map.ofEntries(
                Map.entry("this", 2),
                Map.entry("is", 2),
                Map.entry("a", 2),
                Map.entry("test.", 2),
                Map.entry("only", 1));
        runTest(1, t1, exp1, WordCounter::countWords);

        // Test 2: punctuation preserved (different tokens)
        String t2 = "Hello, hello! HELLO";
        Map<String, Integer> exp2 = Map.ofEntries(
                Map.entry("hello,", 1),
                Map.entry("hello!", 1),
                Map.entry("hello", 1));
        runTest(2, t2, exp2, WordCounter::countWords);

        // Test 3: empty / whitespace-only string
        String t3 = "   \t\n ";
        Map<String, Integer> exp3 = Collections.emptyMap();
        runTest(3, t3, exp3, WordCounter::countWords);

        // Test 4: multiple whitespace types and repeated words
        String t4 = "one   two\tthree\none";
        Map<String, Integer> exp4 = Map.ofEntries(
                Map.entry("one", 2),
                Map.entry("two", 1),
                Map.entry("three", 1));
        runTest(4, t4, exp4, WordCounter::countWords);

        // Test 5: unicode and numbers; demonstrates lowercasing of accents
        String t5 = "Café café CAFÉ 123 123";
        Map<String, Integer> exp5 = Map.ofEntries(
                Map.entry("café", 3),
                Map.entry("123", 2));
        runTest(5, t5, exp5, WordCounter::countWords);

    }

    private static void runTest(int id, String input, Map<String, Integer> expected,
            java.util.function.Function<String, Map<String, Integer>> fn) {
        System.out.println("---- Test " + id + " ----");
        System.out.println("Input: " + input);
        Map<String, Integer> actual = fn.apply(input);
        System.out.println("Expected: " + prettyPrint(expected));
        System.out.println("Actual  : " + prettyPrint(actual));
        System.out.println("Result  : " + (equalsMap(expected, actual) ? "PASS" : "FAIL"));
        System.out.println();
    }
}
