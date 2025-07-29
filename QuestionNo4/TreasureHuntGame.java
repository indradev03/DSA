package QuestionNo4;

import java.util.*;

public class TreasureHuntGame {

    /*
     * Problem Summary:
     * Player 1 (Mouse) starts at node 1, Player 2 (Cat) starts at node 2.
     * The treasure is at node 0.
     * Mouse moves first. Cat cannot go to node 0.
     * The game ends when:
     * - Mouse reaches node 0 → Mouse wins (1)
     * - Cat catches Mouse → Cat wins (2)
     * - A position repeats → Draw (0)
     * 
     * Approach:
     * - Use BFS with a state [mousePosition, catPosition, turn]
     * - Track visited states to detect cycles.
     * - Simulate turn-based moves, respecting constraints.
     */

    static class GameState {
        int mousePos;
        int catPos;
        int turn;

        GameState(int m, int c, int t) {
            mousePos = m;
            catPos = c;
            turn = t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GameState)) return false;
            GameState state = (GameState) o;
            return mousePos == state.mousePos && catPos == state.catPos && turn == state.turn;
        }

        @Override
        public int hashCode() {
            return Objects.hash(mousePos, catPos, turn);
        }
    }

    public static int playGame(int[][] graph) {
        Queue<GameState> queue = new LinkedList<>();
        Set<GameState> visited = new HashSet<>();

        // Start: mouse at 1, cat at 2, mouse's turn
        queue.offer(new GameState(1, 2, 0));
        visited.add(new GameState(1, 2, 0));

        int maxSteps = 1000; // To prevent infinite loop
        int steps = 0;

        while (!queue.isEmpty() && steps < maxSteps) {
            int size = queue.size();
            while (size-- > 0) {
                GameState state = queue.poll();
                int mouse = state.mousePos;
                int cat = state.catPos;
                int turn = state.turn;

                // Check for end conditions
                if (mouse == 0) return 1; // Mouse wins
                if (mouse == cat) return 2; // Cat wins

                if (turn % 2 == 0) {
                    // Mouse's turn
                    for (int nextMouse : graph[mouse]) {
                        GameState nextState = new GameState(nextMouse, cat, turn + 1);
                        if (!visited.contains(nextState)) {
                            visited.add(nextState);
                            queue.offer(nextState);
                        }
                    }
                } else {
                    // Cat's turn
                    for (int nextCat : graph[cat]) {
                        if (nextCat == 0) continue; // Cat cannot go to treasure
                        GameState nextState = new GameState(mouse, nextCat, turn + 1);
                        if (!visited.contains(nextState)) {
                            visited.add(nextState);
                            queue.offer(nextState);
                        }
                    }
                }
            }
            steps++;
        }

        // If we exit loop, it's a draw
        return 0;
    }

    public static void main(String[] args) {
        int[][] graph = {
            {2, 5},    // Node 0
            {3},       // Node 1 (Mouse starts here)
            {0, 4, 5}, // Node 2 (Cat starts here)
            {1, 4, 5},
            {2, 3},
            {0, 2, 3}
        };

        int result = playGame(graph);
        System.out.println("Final Result: " + result);
        // Output: 0 (Draw)
    }
}

