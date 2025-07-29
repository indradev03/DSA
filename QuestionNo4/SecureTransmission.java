package QuestionNo4;

import java.util.*;

public class SecureTransmission {

    /*
     * Problem Summary:
     * You're given a network of offices (nodes) connected by communication links (edges)
     * where each link has a signal strength.
     * You must check if a secure message can be transmitted from one office to another
     * without using any link with strength >= maxStrength.
     *
     * Approach:
     * - Build an undirected graph using adjacency list.
     * - Each edge stores a pair of (neighbor, strength).
     * - For canTransmit, run a DFS or BFS starting from the sender node, only visiting neighbors
     *   where the link strength is strictly less than maxStrength.
     * - If the receiver is reachable, return true; otherwise, return false.
     */

    private Map<Integer, List<int[]>> graph;

    // Constructor to build the graph
    public SecureTransmission(int n, int[][] links) {
        graph = new HashMap<>();

        for (int i = 0; i < n; i++) {
            graph.put(i, new ArrayList<>());
        }

        // Each link: [a, b, strength]
        for (int[] link : links) {
            int a = link[0], b = link[1], strength = link[2];
            graph.get(a).add(new int[]{b, strength});
            graph.get(b).add(new int[]{a, strength}); // Undirected
        }
    }

    // DFS to check if receiver can be reached from sender under strength constraint
    public boolean canTransmit(int sender, int receiver, int maxStrength) {
        Set<Integer> visited = new HashSet<>();
        return dfs(sender, receiver, maxStrength, visited);
    }

    // Recursive DFS helper
    private boolean dfs(int current, int target, int maxStrength, Set<Integer> visited) {
        if (current == target) return true;
        visited.add(current);

        for (int[] neighbor : graph.get(current)) {
            int next = neighbor[0];
            int strength = neighbor[1];

            // Only explore if not visited and strength < maxStrength
            if (strength < maxStrength && !visited.contains(next)) {
                if (dfs(next, target, maxStrength, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    // Main method to test the class with sample data
    public static void main(String[] args) {
        SecureTransmission st = new SecureTransmission(6, new int[][]{
            {0, 2, 4},
            {2, 3, 1},
            {2, 1, 3},
            {4, 5, 5}
        });

        System.out.println(st.canTransmit(2, 3, 2)); // true
        System.out.println(st.canTransmit(1, 3, 3)); // false
        System.out.println(st.canTransmit(2, 0, 3)); // true (via 3)
        System.out.println(st.canTransmit(0, 5, 6)); // false
    }
}
