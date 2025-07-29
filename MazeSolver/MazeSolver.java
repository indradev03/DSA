package MazeSolver;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MazeSolver {
    private final int[][] maze;
    private final boolean[][] visited;
    private final List<Point> path = new ArrayList<>();

    private final Point start, end;
    private final int rows, cols;

    public MazeSolver(int[][] maze, Point start, Point end) {
        this.maze = maze;
        this.start = start;
        this.end = end;
        this.rows = maze.length;
        this.cols = maze[0].length;
        this.visited = new boolean[rows][cols];
    }

    public void solveDFS() {
        path.clear();
        for (boolean[] row : visited) Arrays.fill(row, false);

        boolean success = dfs(start.x, start.y);
        if (!success) {
            System.out.println("DFS: No path found.");
        } else {
            System.out.println("DFS: Path found with length " + path.size());
        }
    }

    private boolean dfs(int x, int y) {
        if (!inBounds(x, y) || maze[x][y] == 1 || visited[x][y]) return false;

        visited[x][y] = true;
        path.add(new Point(x, y));

        if (x == end.x && y == end.y) return true;

        int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        for (int[] d : dirs) {
            if (dfs(x + d[0], y + d[1])) return true;
        }

        path.remove(path.size() - 1); // backtrack
        return false;
    }

    public void solveBFS() {
        path.clear();
        for (boolean[] row : visited) Arrays.fill(row, false);

        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> parentMap = new HashMap<>();

        queue.add(start);
        visited[start.x][start.y] = true;

        boolean found = false;

        while (!queue.isEmpty()) {
            Point curr = queue.poll();
            if (curr.equals(end)) {
                found = true;
                break;
            }

            int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};
            for (int[] d : dirs) {
                int nx = curr.x + d[0], ny = curr.y + d[1];
                if (inBounds(nx, ny) && maze[nx][ny] == 0 && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    Point next = new Point(nx, ny);
                    parentMap.put(next, curr);
                    queue.add(next);
                }
            }
        }

        if (!found) {
            System.out.println("BFS: No path found.");
            return;
        }

        // Reconstruct path
        Point curr = end;
        while (curr != null && parentMap.containsKey(curr)) {
            path.add(0, curr);
            curr = parentMap.get(curr);
        }
        path.add(0, start); // include start node
        System.out.println("BFS: Path found with length " + path.size());
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < rows && y < cols;
    }

    public List<Point> getPath() {
        return path;
    }

    public void draw(Graphics g, int size) {
        // Draw maze grid
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                Color color = (maze[i][j] == 1) ? Color.BLACK : Color.WHITE;
                g.setColor(color);
                g.fillRect(j * size, i * size, size, size);
                g.setColor(Color.GRAY);
                g.drawRect(j * size, i * size, size, size);
            }
        }

        // Draw start (green) and end (red)
        g.setColor(Color.GREEN);
        g.fillRect(start.y * size, start.x * size, size, size);
        g.setColor(Color.RED);
        g.fillRect(end.y * size, end.x * size, size, size);

        // Draw path (cyan)
        g.setColor(Color.CYAN);
        for (Point p : path) {
            g.fillRect(p.y * size + size / 4, p.x * size + size / 4, size / 2, size / 2);
        }
    }
}
