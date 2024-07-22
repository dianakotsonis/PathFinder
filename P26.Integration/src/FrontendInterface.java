import java.util.Scanner;

public interface FrontendInterface {
    /**
     * Passes and accepts a reference to the backend and a java.util.Scanner instance to read user input
     */
    //public FrontendInterface(Backend backend, Scanner userInput);

    /**
     * Main command loop for the user
     */
    public void startApp();

    /**
     * This method displays the main menu of the Path fINDER app in a loop and calls                     
     * helper methods(or submenus) based on user input. It also handles exceptions should they be thrown.  
     */
    public void mainMenu();

    /**
     * Command to specify and load a data file
     */
    public void loadFile(String filename);

    /**
     * a command to show statistics about the dataset that includes the number of buildings (nodes), 
     * the number of edges, and the total walking time (sum of all edge weights) in the graph
     */
    public void showDatasetStatistics();

    /**
     * a command that asks the user for a start and destination building, then lists the shortest path between those buildings, 
     * including all buildings on the way, the estimated walking time for each segment, and the total time it takes to walk the path, and
     */
    public void findShortestPath(String start, String destination);

    /**
     * Command to exit the app
     */
    public void exitApp();
}
