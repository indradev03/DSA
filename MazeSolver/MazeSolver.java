package MazeSolver;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

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
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw maze grid
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[i].length; j++) {
                    if (maze[i][j] == 1) {
                        g2.setColor(new Color(40, 40, 40)); // dark gray walls
                    } else {
                        g2.setColor(new Color(230, 230, 230)); // light path
                    }
                    g2.fillRect(j * size, i * size, size, size);
                }
            }

            // Optional: Draw visited cells (light blue overlay)
            g2.setColor(new Color(150, 200, 255, 100));
            for (int i = 0; i < visited.length; i++) {
                for (int j = 0; j < visited[i].length; j++) {
                    if (visited[i][j]) {
                        g2.fillRect(j * size, i * size, size, size);
                    }
                }
            }

            // Draw path (cyan with rounded effect)
            g2.setColor(new Color(0, 255, 255, 180));
            for (Point p : path) {
                g2.fillRoundRect(p.y * size + size / 4, p.x * size + size / 4, size / 2, size / 2, 10, 10);
            }

            // Draw grid lines (optional)
            g2.setColor(new Color(180, 180, 180));
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[i].length; j++) {
                    g2.drawRect(j * size, i * size, size, size);
                }
            }

            // Draw start (green) and end (red)
            g2.setColor(new Color(0, 200, 0)); // green
            g2.fillOval(start.y * size + size / 4, start.x * size + size / 4, size / 2, size / 2);

            g2.setColor(new Color(220, 30, 30)); // red
            g2.fillOval(end.y * size + size / 4, end.x * size + size / 4, size / 2, size / 2);
        }

    }
    

