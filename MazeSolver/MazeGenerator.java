package MazeSolver;

import java.util.*;

public class MazeGenerator {
    private static final int WALL = 1;
    private static final int PATH = 0;

    public static int[][] generateMaze(int rows, int cols) {
        int[][] maze = new int[rows][cols];
        for (int[] row : maze) Arrays.fill(row, WALL);

        // Step 1: Create a guaranteed path from start to end
        createGuaranteedPath(maze, rows, cols);

        // Step 2: Carve out more random paths to look like a maze
        carveRandomPaths(maze, rows, cols, 0.4); // 40% of cells made PATH

        return maze;
    }

    private static void createGuaranteedPath(int[][] maze, int rows, int cols) {
        int x = 0, y = 0;
        maze[x][y] = PATH;

        Random rand = new Random();

        while (x != rows - 1 || y != cols - 1) {
            if (rand.nextBoolean()) {
                if (x < rows - 1) x++;
            } else {
                if (y < cols - 1) y++;
            }
            maze[x][y] = PATH;
        }
    }

    private static void carveRandomPaths(int[][] maze, int rows, int cols, double probability) {
        Random rand = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == WALL && rand.nextDouble() < probability) {
                    maze[i][j] = PATH;
                }
            }
        }
    }
}
