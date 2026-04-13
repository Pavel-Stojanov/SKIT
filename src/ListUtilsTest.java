import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ListUtilsTest {

    // Test 1: Base Test [Populated, Populated, Output >1, Yes Dupes, Yes Upper]
    @Test
    public void test1_BaseChoice() {
        List<String> list1 = Arrays.asList("Apple", "banana", "Cherry", "Apple");
        List<String> list2 = Arrays.asList("BANANA", "cherry", "Durian", "Durian");
        List<String> expected = Arrays.asList("banana", "Cherry");

        List<String> result = ListUtils.findCommonIgnoreCase(list1, list2);
        assertEquals(expected, result);
        assertTrue(result.size() > 1);

    }

    // Test 2: Vary C1 to Null [Null, Populated, Output 0 (Fixed), Yes Dupes, Yes Upper]
    @Test
    public void test2_List1Null() {
        List<String> list1 = null;
        List<String> list2 = Arrays.asList("BANANA", "cherry", "Durian", "Durian");

        List<String> result = ListUtils.findCommonIgnoreCase(list1, list2);
        assertTrue(result.isEmpty());
    }

    // Test 3: Vary C1 to Empty [Empty, Populated, Output 0 (Fixed), Yes Dupes, Yes Upper]
    @Test
    public void test3_List1Empty() {
        List<String> list1 = Collections.emptyList();
        List<String> list2 = Arrays.asList("BANANA", "cherry", "Durian", "Durian");

        List<String> result = ListUtils.findCommonIgnoreCase(list1, list2);
        assertTrue(result.isEmpty());
    }

    // Test 4: Vary C2 to Null [Populated, Null, Output 0 (Fixed), Yes Dupes, Yes Upper]
    @Test
    public void test4_List2Null() {
        List<String> list1 = Arrays.asList("Apple", "banana", "Cherry", "Apple");
        List<String> list2 = null;

        List<String> result = ListUtils.findCommonIgnoreCase(list1, list2);
        assertTrue(result.isEmpty());
    }

    // Test 5: Vary C2 to Empty [Populated, Empty, Output 0 (Fixed), Yes Dupes, Yes Upper]
    @Test
    public void test5_List2Empty() {
        List<String> list1 = Arrays.asList("Apple", "banana", "Cherry", "Apple");
        List<String> list2 = Collections.emptyList();

        List<String> result = ListUtils.findCommonIgnoreCase(list1, list2);
        assertTrue(result.isEmpty());
    }

    // Test 6: Vary C3 to Output 0 [Populated, Populated, Output 0, Yes Dupes, Yes Upper]
    @Test
    public void test6_OutputSizeZero() {
        List<String> list1 = Arrays.asList("Apple", "Apple", "Grape");
        List<String> list2 = Arrays.asList("Durian", "Durian", "Fig");

        List<String> result = ListUtils.findCommonIgnoreCase(list1, list2);
        assertTrue(result.isEmpty());
    }

    // Test 7: Vary C3 to Output 1 [Populated, Populated, Output 1, Yes Dupes, Yes Upper]
    @Test
    public void test7_OutputSizeOne() {
        List<String> list1 = Arrays.asList("Apple", "Apple", "Grape");
        List<String> list2 = Arrays.asList("APPLE", "Durian", "Durian");
        List<String> expected = Arrays.asList("Apple");

        List<String> result = ListUtils.findCommonIgnoreCase(list1, list2);
        assertEquals(expected, result);
        assertEquals(1, result.size());
    }

    // Test 8: Vary C4 to No Dupes [Populated, Populated, Output >1, No Dupes, Yes Upper]
    @Test
    public void test8_NoDuplicatesInputs() {
        List<String> list1 = Arrays.asList("Apple", "banana", "Cherry");
        List<String> list2 = Arrays.asList("BANANA", "cherry", "Durian");
        List<String> expected = Arrays.asList("banana", "Cherry");

        List<String> result = ListUtils.findCommonIgnoreCase(list1, list2);
        assertEquals(expected, result);
    }

    // Test 9: Vary C5 to No Upper [Populated, Populated, Output >1, Yes Dupes, No Upper]
    @Test
    public void test9_StrictlyLowercaseInputs() {
        List<String> list1 = Arrays.asList("apple", "banana", "cherry", "apple");
        List<String> list2 = Arrays.asList("banana", "cherry", "durian", "durian");
        List<String> expected = Arrays.asList("banana", "cherry");

        List<String> result = ListUtils.findCommonIgnoreCase(list1, list2);
        assertEquals(expected, result);
    }

    // The name attribute formats the test output in your IDE to make it highly readable.
    // {index} is the test number, and {3} injects the 4th parameter (the testDescription).
    @ParameterizedTest(name = "Test {index}: {3}")
    @MethodSource("provideBccTestCases")
    public void testFindCommonIgnoreCase(List<String> list1, List<String> list2, List<String> expected, String testDescription) {

        List<String> result = ListUtils.findCommonIgnoreCase(list1, list2);

        // A single assertion cleanly handles all 9 scenarios.
        // If a test fails, the testDescription will print out to tell you exactly which partition broke.
        assertEquals(expected, result, "Failed on BCC partition: " + testDescription);
    }

    // This method supplies the data for the parameterized test above.
    private static Stream<Arguments> provideBccTestCases() {
        return Stream.of(
                // Test 1: Base Test [Populated, Populated, Output >1, Yes Dupes, Yes Upper]
                Arguments.of(
                        Arrays.asList("Apple", "banana", "Cherry", "Apple"),
                        Arrays.asList("BANANA", "cherry", "Durian", "Durian"),
                        Arrays.asList("banana", "Cherry"),
                        "Base Choice"
                ),

                // Test 2: Vary C1 to Null [Null, Populated, Output 0, Yes Dupes, Yes Upper]
                Arguments.of(
                        null,
                        Arrays.asList("BANANA", "cherry", "Durian", "Durian"),
                        Collections.emptyList(),
                        "Vary C1: list1 is Null"
                ),

                // Test 3: Vary C1 to Empty [Empty, Populated, Output 0, Yes Dupes, Yes Upper]
                Arguments.of(
                        Collections.emptyList(),
                        Arrays.asList("BANANA", "cherry", "Durian", "Durian"),
                        Collections.emptyList(),
                        "Vary C1: list1 is Empty"
                ),

                // Test 4: Vary C2 to Null [Populated, Null, Output 0, Yes Dupes, Yes Upper]
                Arguments.of(
                        Arrays.asList("Apple", "banana", "Cherry", "Apple"),
                        null,
                        Collections.emptyList(),
                        "Vary C2: list2 is Null"
                ),

                // Test 5: Vary C2 to Empty [Populated, Empty, Output 0, Yes Dupes, Yes Upper]
                Arguments.of(
                        Arrays.asList("Apple", "banana", "Cherry", "Apple"),
                        Collections.emptyList(),
                        Collections.emptyList(),
                        "Vary C2: list2 is Empty"
                ),

                // Test 6: Vary C3 to Output 0 [Populated, Populated, Output 0, Yes Dupes, Yes Upper]
                Arguments.of(
                        Arrays.asList("Apple", "Apple", "Grape"),
                        Arrays.asList("Durian", "Durian", "Fig"),
                        Collections.emptyList(),
                        "Vary C3: Output Size Zero"
                ),

                // Test 7: Vary C3 to Output 1 [Populated, Populated, Output 1, Yes Dupes, Yes Upper]
                Arguments.of(
                        Arrays.asList("Apple", "Apple", "Grape"),
                        Arrays.asList("APPLE", "Durian", "Durian"),
                        Arrays.asList("Apple"),
                        "Vary C3: Output Size One"
                ),

                // Test 8: Vary C4 to No Dupes [Populated, Populated, Output >1, No Dupes, Yes Upper]
                Arguments.of(
                        Arrays.asList("Apple", "banana", "Cherry"),
                        Arrays.asList("BANANA", "cherry", "Durian"),
                        Arrays.asList("banana", "Cherry"),
                        "Vary C4: No Duplicates in Inputs"
                ),

                // Test 9: Vary C5 to No Upper [Populated, Populated, Output >1, Yes Dupes, No Upper]
                Arguments.of(
                        Arrays.asList("apple", "banana", "cherry", "apple"),
                        Arrays.asList("banana", "cherry", "durian", "durian"),
                        Arrays.asList("banana", "cherry"),
                        "Vary C5: Strictly Lowercase Inputs"
                )
        );
    }
}