import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListUtils {

    /**
     * Given two lists of strings, return a list of all unique strings that appear in both lists,
     * ignoring case differences.
     * * Example:
     * list1 = ["Apple", "banana", "Cherry", "apple"]
     * list2 = ["BANANA", "cherry", "Durian"]
     * Output: ["banana", "Cherry"]
     * * The result should contain the matching strings from the first list only,
     * preserving their original casing and order, but without duplicates.
     */
    public static List<String> findCommonIgnoreCase(List<String> list1, List<String> list2) {
        // Handle interface boundaries: if either list is null or empty, return an empty list.
        if (list1 == null || list2 == null || list1.isEmpty() || list2.isEmpty()) {
            return Collections.emptyList();
        }

        // Convert list2 elements to lowercase and store in a Set.
        // This gives us O(1) constant time lookup and handles the "ignore case" requirement.
        Set<String> list2Lower = new HashSet<>();
        for (String item : list2) {
            if (item != null) {
                list2Lower.add(item.toLowerCase());
            }
        }

        List<String> result = new ArrayList<>();
        Set<String> seen = new HashSet<>(); // Tracks items we've already added to avoid duplicates

        // Iterate through list1 to preserve original order and casing
        for (String item : list1) {
            if (item != null) {
                String lowerItem = item.toLowerCase();

                // If the item exists in list2 AND we haven't already added it to our result
                if (list2Lower.contains(lowerItem) && !seen.contains(lowerItem)) {
                    result.add(item);       // Add the original string (preserving case)
                    seen.add(lowerItem);    // Mark this element as seen
                }
            }
        }

        return result;
    }
}