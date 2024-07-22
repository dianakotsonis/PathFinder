import java.util.ArrayList;

public interface ShortestPath {
    /**
     * This method returns a list of the buildings along the shortest path from one place to
     * another.
     * @param source String of the building path started at.
     * @param destination String of building path ends at.
     * @return the list of buildings as Strings
     */
    public ArrayList<String> getShortestPath(String source, String destination);

    /**
     * Returns a list of the walking times from building to building along the shortest path.
     * @param source String of the building path started at.
     * @param destination String of building path ends at.
     * @return a list of double walking times in seconds from building to building.
     */
    public ArrayList<Double>getWalkingTimes(String source, String destination);

    /**
     * Total time it takes to walk from one building to another.
     * @param source String of the building path started at.
     * @param destination String of building path ends at.
     * @return total time in seconds.
     */
    public double getTotalTime(String source,String destination);


}
