import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystemNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Backend implements BackendInterface, ShortestPath {
    public static GraphADT<String,Double> graph;
    double totalTime = 0.0;
    public Backend(DijkstraGraph<String, Double> graph) {
        this.graph = graph;
    }
    public Boolean readData(File file) {
        try (Scanner scanner = new Scanner(file)) {
            Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*--\\s*\"([^\"]+)\"\\s*\\[seconds=(\\d+\\.?\\d*)\\]");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("seconds")) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String node1 = matcher.group(1);
                        String node2 = matcher.group(2);
                        double seconds = Double.parseDouble(matcher.group(3));
                        if (!graph.containsNode(node1)) {
                            graph.insertNode(node1);
                        }
                        if (!graph.containsNode(node2)) {
                            graph.insertNode(node2);
                        }
                        if (!graph.containsEdge(node1, node2)) {
                            graph.insertEdge(node1, node2, seconds);
                        } else {
                            graph.insertEdge(node2, node1, seconds);
                        }
                        totalTime += seconds;
                    }
                }
            }
        }catch (FileSystemNotFoundException | FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public ArrayList<String> findShortestPath(String source, String destination) {
        ArrayList<String> pathDatas = new ArrayList<>();
        pathDatas.add(graph.shortestPathData(source,destination).toString());
        pathDatas.add("Total Time: "+graph.shortestPathCost(source,destination));
        return pathDatas;
    }

    public ArrayList<Double> pathData() {
        ArrayList<Double> pathDatas = new ArrayList<>();
        pathDatas.add((double) graph.getNodeCount());
        pathDatas.add((double) graph.getEdgeCount()/2);
        pathDatas.add(totalTime/2);
        return pathDatas;
    }

    public ArrayList<String> getShortestPath(String source, String destination) {
        ArrayList<String> pathDatas = new ArrayList<>();
        pathDatas.add(graph.shortestPathData(source,destination).toString());
        return pathDatas;
    }

    public ArrayList<Double> getWalkingTimes(String source, String destination) {
        ArrayList<Double> pathCost = new ArrayList<>();
        List<String> pathDatas = graph.shortestPathData(source, destination);
        for(int i=0;i<pathDatas.size()-1;i++){
            pathCost.add(graph.getEdge(pathDatas.get(i),pathDatas.get(i+1)));
        }
        return pathCost;
    }

    public double getTotalTime(String source, String destination) {
        return graph.shortestPathCost(source,destination);
    }
}
