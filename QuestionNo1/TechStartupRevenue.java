package QuestionNo1;
import java.util.*;

public class TechStartupRevenue {

    // Helper class to represent each project with its investment and expected revenue
    static class Project {
        int investment;
        int revenue;

        public Project(int investment, int revenue) {
            this.investment = investment;
            this.revenue = revenue;
        }
    }

    // Function to maximize capital after at most k project executions
    public static int maximizeCapital(int k, int c, int[] revenues, int[] investments) {
        int n = revenues.length;

        // Step 1: Pair up investments and revenues into Project objects
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            projects.add(new Project(investments[i], revenues[i]));
        }

        // Step 2: Sort projects by the minimum investment required (ascending)
        projects.sort(Comparator.comparingInt(p -> p.investment));

        // Step 3: Use a max-heap to always choose the most profitable affordable project
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        int i = 0; // Pointer for project list

        // Step 4: Execute up to k projects
        for (int j = 0; j < k; j++) {
            // Push all projects that can be afforded with current capital to the heap
            while (i < n && projects.get(i).investment <= c) {
                maxHeap.offer(projects.get(i).revenue);
                i++;
            }

            // If no project is affordable, exit early
            if (maxHeap.isEmpty()) break;

            // Execute the most profitable available project
            c += maxHeap.poll();
        }

        // Step 5: Return the final capital
        return c;
    }

    // Main method to test the algorithm
    public static void main(String[] args) {
        // Example 1
        int[] revenues1 = {2, 5, 8};
        int[] investments1 = {0, 2, 3};
        System.out.println("Max Capital: " + 
            maximizeCapital(2, 0, revenues1, investments1)); // Output: 7

        // Example 2
        int[] revenues2 = {3, 6, 10};
        int[] investments2 = {1, 3, 5};
        System.out.println("Max Capital: " + 
            maximizeCapital(3, 1, revenues2, investments2)); // Output: 19
    }
}
    /* The TechStartupRevenue program is designed to help a tech startup 
    maximize its capital by executing a limited number of profitable projects.
    Each project requires a certain investment and yields a specific revenue. The
    goal is to choose at most k projects that offer the highest possible return, given 
    the startup's current capital c. To solve this problem, the code first pairs each project’s investment 
    and revenue into Project objects and stores them in a list. This list is then sorted in ascending order 
    based on investment requirements, so that the cheapest projects come first. 
    The core logic uses a greedy strategy with a max-heap (priority queue), where
    for each of the k rounds, the algorithm pushes all projects that are affordable
    with the current capital into the heap. This heap allows quick access to the most profitable
    project that can currently be executed. The project with the highest revenue is selected and its
    revenue is added to the capital. This process repeats up to k times or until no further projects can be afforded. 
    Finally, the program returns the maximum capital achieved after the project executions. 
    The approach is efficient and ensures that at each step, the most profitable option within the
    current budget is selected to maximize overall gain. */