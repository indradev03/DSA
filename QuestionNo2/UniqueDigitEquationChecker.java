package QuestionNo2;

import java.util.HashMap;
import java.util.Map;

public class UniqueDigitEquationChecker {

    /*
     * This program checks whether a given cryptarithm (word-based arithmetic puzzle)
     * is valid for a specific digit-letter mapping.
     * 
     * Rules:
     * - Each letter must be assigned a unique digit (0–9).
     * - No number can have a leading zero.
     * - The equation must satisfy word1 + word2 = word3, when converted to digits.
     */

    // Converts a word into a numeric value based on the given letter-digit map
    public static int wordToNumber(String word, Map<Character, Integer> mapping) {
        int num = 0;
        for (char ch : word.toCharArray()) {
            // If a letter is missing from the map, it's an invalid setup
            if (!mapping.containsKey(ch)) {
                throw new IllegalArgumentException("No digit assigned to letter: " + ch);
            }
            // Build number by adding digit for each letter
            num = num * 10 + mapping.get(ch);
        }
        return num;
    }

    // Verifies if word1 + word2 == word3 using the provided digit mapping
    public static boolean verifyEquation(String word1, String word2, String word3, Map<Character, Integer> mapping) {
        // Check for leading zeros in any of the words — not allowed
        if (mapping.get(word1.charAt(0)) == 0 || mapping.get(word2.charAt(0)) == 0 || mapping.get(word3.charAt(0)) == 0) {
            System.out.println("Invalid: One or more words have a leading zero.");
            return false;
        }

        // Convert each word to its corresponding number
        int num1 = wordToNumber(word1, mapping);
        int num2 = wordToNumber(word2, mapping);
        int num3 = wordToNumber(word3, mapping);

        // Output conversion details for verification
        System.out.println(word1 + " → " + num1);
        System.out.println(word2 + " → " + num2);
        System.out.println(word3 + " → " + num3);
        System.out.println(num1 + " + " + num2 + " = " + (num1 + num2));

        // Return true if the equation holds
        return num1 + num2 == num3;
    }

    // Main method to run sample scenarios
    public static void main(String[] args) {

        // Scenario 1: "STAR" + "MOON" = "NIGHT"
        // Letter to digit mapping:
        // S=8, T=4, A=2, R=5, M=7, O=1, N=9, I=6, G=3, H=0
        Map<Character, Integer> mapping1 = new HashMap<>();
        mapping1.put('S', 8);
        mapping1.put('T', 4);
        mapping1.put('A', 2);
        mapping1.put('R', 5);
        mapping1.put('M', 7);
        mapping1.put('O', 1);
        mapping1.put('N', 9);
        mapping1.put('I', 6);
        mapping1.put('G', 3);
        mapping1.put('H', 0);

        System.out.println("Scenario 1: STAR + MOON = NIGHT");
        boolean result1 = verifyEquation("STAR", "MOON", "NIGHT", mapping1);
        System.out.println("Equation valid? " + result1);
        System.out.println();

        // Scenario 2: "CODE" + "BUG" = "DEBUG"
        // Letter to digit mapping:
        // C=1, O=0, D=5, E=7, B=3, U=9, G=2
        Map<Character, Integer> mapping2 = new HashMap<>();
        mapping2.put('C', 1);
        mapping2.put('O', 0);
        mapping2.put('D', 5);
        mapping2.put('E', 7);
        mapping2.put('B', 3);
        mapping2.put('U', 9);
        mapping2.put('G', 2);

        System.out.println("Scenario 2: CODE + BUG = DEBUG");
        boolean result2 = verifyEquation("CODE", "BUG", "DEBUG", mapping2);
        System.out.println("Equation valid? " + result2);
    }
}


