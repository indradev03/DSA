package QuestionNo1;
public class SecurePinPolicy {

    // Function to return the minimum number of changes required to make the PIN strong
    public static int strongPinChanges(String pinCode) {
        int n = pinCode.length();
        boolean hasLower = false, hasUpper = false, hasDigit = false;

        int repeatCount = 0;
        int[] repeats = new int[n]; // To store lengths of repeated sequences

        // Step 1: Check character types and count repeated sequences
        for (int i = 0; i < n; ) {
            char ch = pinCode.charAt(i);

            if (Character.isLowerCase(ch)) hasLower = true;
            if (Character.isUpperCase(ch)) hasUpper = true;
            if (Character.isDigit(ch)) hasDigit = true;

            int j = i;
            while (j < n && pinCode.charAt(j) == ch) j++;

            int len = j - i;
            if (len >= 3) {
                repeats[repeatCount++] = len;
            }
            i = j;
        }

        // Step 2: Count missing types (lowercase, uppercase, digit)
        int missingTypes = 0;
        if (!hasLower) missingTypes++;
        if (!hasUpper) missingTypes++;
        if (!hasDigit) missingTypes++;

        // Step 3: Handle short PINs (<6 characters)
        if (n < 6) {
            return Math.max(missingTypes, 6 - n);
        }

        // Step 4: Handle medium length PINs (6 to 20 characters)
        int replace = 0;
        for (int i = 0; i < repeatCount; i++) {
            replace += repeats[i] / 3; // Every 3 repeating characters require 1 replacement
        }

        if (n <= 20) {
            return Math.max(missingTypes, replace);
        }

        // Step 5: Handle long PINs (>20 characters)
        int deleteCount = n - 20;
        int over = deleteCount;

        // Optimize: Try to reduce replacements by deleting in repeating sequences
        for (int i = 0; i < repeatCount && over > 0; i++) {
            if (repeats[i] >= 3) {
                int reduce = Math.min(over, repeats[i] - 2);
                repeats[i] -= reduce;
                over -= reduce;
            }
        }

        // Recalculate replacements after deletion
        replace = 0;
        for (int i = 0; i < repeatCount; i++) {
            if (repeats[i] >= 3) {
                replace += repeats[i] / 3;
            }
        }

        return deleteCount + Math.max(missingTypes, replace);
    }

    // Main method to test examples
    public static void main(String[] args) {
        System.out.println("Example 1:");
        System.out.println("Input: X1!");
        System.out.println("Output: " + strongPinChanges("X1!")); // Output: 3

        System.out.println("\nExample 2:");
        System.out.println("Input: 123456");
        System.out.println("Output: " + strongPinChanges("123456")); // Output: 2

        System.out.println("\nExample 3:");
        System.out.println("Input: Aa1234!");
        System.out.println("Output: " + strongPinChanges("Aa1234!")); // Output: 0
    }
}

/* 
    The SecurePinPolicy program evaluates a given PIN code and calculates the minimum number of 
    changes needed to make it "strong" according to a set of security rules. A strong PIN must be
    between 6 and 20 characters long, include at least one lowercase letter, one uppercase letter,
    and one digit, and must not contain three or more repeating characters in a row. The program 
    first scans the PIN to identify which character types are missing and detects sequences
    of repeated characters that are three or more in length. If the PIN is shorter than 6 
    characters, the required changes are the maximum between the number of missing charactertypes and the number 
    of characters needed to reach length 6. For PINs with lengths between 6 and 20, 
    the program calculates the number of replacements needed to break up repeating sequences and ensures all 
    character types are present. If the PIN is longer than 20 characters, deletions are necessary, and the program 
    strategically deletes characters within repeating sequences to minimize the number of 
    replacements required. Finally, it returns the sum of deletions and the maximum of missing 
    character types or replacements to produce the minimal number of total changes required to make
    the PIN secure. The provided examples illustrate how the function handles different cases, 
    including short, moderate, and already strong PINs.
*/
