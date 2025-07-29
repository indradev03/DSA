package MazeSolver;

import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel {
    private final int rows = 20;
    private final int cols = 20;
    private final int cellSize = 30;

    private int[][] maze;
    private MazeSolver solver;

    private Point start = new Point(0, 0);
    private Point end = new Point(rows - 1, cols - 1);

    private MazeCanvas mazeCanvas; // <-- this is key!

    public MazePanel() {
        maze = MazeGenerator.generateMaze(rows, cols);
        solver = new MazeSolver(maze, start, end);

        mazeCanvas = new MazeCanvas();

        JButton dfsButton = new JButton("Solve with DFS");
        dfsButton.addActionListener(e -> {
            solver.solveDFS();
            mazeCanvas.repaint(); // not this.repaint()
        });

        JButton bfsButton = new JButton("Solve with BFS");
        bfsButton.addActionListener(e -> {
            solver.solveBFS();
            mazeCanvas.repaint(); // same
        });

        JButton generateButton = new JButton("Generate New Maze");
        generateButton.addActionListener(e -> {
            maze = MazeGenerator.generateMaze(rows, cols);
            solver = new MazeSolver(maze, start, end);
            mazeCanvas.setSolver(solver);
            mazeCanvas.repaint(); // update display
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(dfsButton);
        buttonPanel.add(bfsButton);
        buttonPanel.add(generateButton);

        setLayout(new BorderLayout());
        add(mazeCanvas, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Canvas to draw maze
    class MazeCanvas extends JPanel {
        public MazeCanvas() {
            setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        }

        public void setSolver(MazeSolver newSolver) {
            solver = newSolver;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            solver.draw(g, cellSize);
        }
    }
}
