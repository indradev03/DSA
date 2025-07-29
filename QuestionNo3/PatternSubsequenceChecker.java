package QuestionNo3;

public class PatternSubsequenceChecker {

    /*
     * Problem Summary:
     * Given two strings `p1` and `p2`, and two integers `t1` and `t2`, this program determines
     * how many times the repeated pattern p2 (repeated t2 times) can be formed by deleting characters
     * (without reordering) from the repeated pattern p1 (repeated t1 times).
     * 
     * Approach:
     * - Construct the full sequence A by repeating p1 t1 times.
     * - Use a greedy pointer approach to scan the full sequence and attempt to extract p2 repeatedly.
     * - Count how many times p2 is fully matched in order inside A.
     * - The final answer is how many full repetitions of p2 we could extract.
     */

    public static int getMaxPatternRepeats(String p1, int t1, String p2, int t2) {
        StringBuilder seqA = new StringBuilder();

        // Step 1: Build the full sequence A by repeating p1 t1 times
        for (int i = 0; i < t1; i++) {
            seqA.append(p1);
        }

        int count = 0; // Total number of full p2 matches
        int index = 0; // Pointer in p2

        // Step 2: Scan through seqA to extract p2 repeatedly
        for (int i = 0; i < seqA.length(); i++) {
            if (seqA.charAt(i) == p2.charAt(index)) {
                index++; // Match one character
                if (index == p2.length()) {
                    count++;     // Completed one p2
                    index = 0;   // Reset pointer for next p2
                }
            }
        }

        // Step 3: Return how many full seqB (p2 * t2) we can make
        return count / t2;
    }

    // Main method to test the function
    public static void main(String[] args) {
        String p1 = "bca";
        int t1 = 6;
        String p2 = "ba";
        int t2 = 3;

        int result = getMaxPatternRepeats(p1, t1, p2, t2);
        System.out.println("Output: " + result); // Expected Output: 3
    }
}
