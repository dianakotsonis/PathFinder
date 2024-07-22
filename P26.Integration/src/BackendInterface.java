import java.io.File;
import java.util.ArrayList;

public interface BackendInterface {
    /**
    public FileReader(){
        GraphADT graph = new GraphADT();
    }
    */

    /**
     * Reads data from the specified file.
     */
    public Boolean readData(File file);

    /**
     * Finds the shortest path from a start to a destination in the dataset.
     * @param source String of the building path started at.
     * @param destination String of building path ends at.
     * @return list containing the buildings included in the path.
     */
    public ArrayList<String> findShortestPath(String source, String destination);

    /**
     * Finds statistics about the dataset that includes the number of nodes (buildings),
     * the number of edges, and the total walking time (sum of weights) for all edges in the graph
     *
     * @return a String containing this data.
     */
    public ArrayList<Double> pathData();
}
