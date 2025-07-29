package QuestionNo3;

import java.util.*;

public class MagicalWordPower {

    /*
     * Problem Summary:
     * In a magical world, a magical word is a palindrome with odd length.
     * Given a string M, find two non-overlapping magical words to maximize the product
     * of their lengths (i.e., their combined power).
     *
     * Approach:
     * 1. Find all palindromic substrings of M that have odd length.
     * 2. Store the start and end indices and lengths of each valid magical word.
     * 3. Try all pairs of non-overlapping magical words and compute the product of their lengths.
     * 4. Return the maximum product found.
     */

    // Expand around center to find all odd-length palindromes
    public static List<int[]> findOddLengthPalindromes(String s) {
        List<int[]> palindromes = new ArrayList<>();

        for (int center = 0; center < s.length(); center++) {
            int left = center, right = center;
            while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
                int length = right - left + 1;
                if (length % 2 == 1) {
                    palindromes.add(new int[]{left, right, length});
                }
                left--;
                right++;
            }
        }

        return palindromes;
    }

    public static int maxPowerCombination(String M) {
        List<int[]> magicalWords = findOddLengthPalindromes(M);

        int maxProduct = 0;

        // Try all pairs of magical words
        for (int i = 0; i < magicalWords.size(); i++) {
            int[] p1 = magicalWords.get(i);
            for (int j = i + 1; j < magicalWords.size(); j++) {
                int[] p2 = magicalWords.get(j);

                // Check if non-overlapping
                if (p1[1] < p2[0] || p2[1] < p1[0]) {
                    int product = p1[2] * p2[2];
                    maxProduct = Math.max(maxProduct, product);
                }
            }
        }

        return maxProduct;
    }

    public static void main(String[] args) {
        String M1 = "xyzyxabc";
        System.out.println("Example 1 Output: " + maxPowerCombination(M1)); // Output: 5

        String M2 = "levelwowracecar";
        System.out.println("Example 2 Output: " + maxPowerCombination(M2)); // Output: 35
    }
}
