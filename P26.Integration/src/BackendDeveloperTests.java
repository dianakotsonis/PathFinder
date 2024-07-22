import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

public class BackendDeveloperTests {
    @Test
    public void test1(){
        DijkstraGraph<String,Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        backend.readData(new File("test1.dot"));
        Assertions.assertEquals(false,backend.readData(new File("test1.dot")));
    }
    @Test
    public void test2(){
        DijkstraGraph<String,Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        backend.readData(new File("campus.dot"));
    }
    @Test
    public void test3(){
        DijkstraGraph<String,Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        backend.readData(new File("campus.dot"));
        ArrayList<String> path = backend.findShortestPath("Memorial Union","Computer Sciences and Statistics");
        Assertions.assertEquals(path.get(0), "[Memorial Union, Radio Hall, Education Building, South Hall, Law Building, X01, Luther Memorial Church, Noland Hall, Meiklejohn House, Computer Sciences and Statistics]");
        Assertions.assertEquals(path.get(path.size()-1), "Total Time: 1302.2");
    }
    @Test
    public void test4(){
        DijkstraGraph<String,Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        backend.readData(new File("campus.dot"));
        ArrayList<Double> statistics = backend.pathData();
        ArrayList<Double> expected = new ArrayList<>();
        expected.add(160.0);
        expected.add(400.0);
        expected.add(55337.749999999985);
        for(int i=0;i<statistics.size();i++){
            Assertions.assertEquals(expected.get(i),statistics.get(i));
        }
    }
    @Test
    public void test5(){
        DijkstraGraph<String,Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        backend.readData(new File("campus.dot"));
        ArrayList<Double> pathCosts = backend.getWalkingTimes("Memorial Union","Computer Sciences and Statistics");
        ArrayList<Double> expected = new ArrayList<>();
        expected.add(176.7);
        expected.add(113.0);
        expected.add(187.6);
        expected.add(112.80000000000001);
        expected.add(174.7);
        expected.add(65.5);
        expected.add(183.50000000000003);
        expected.add(124.19999999999999);
        for(int i=0;i<pathCosts.size()-1;i++){
            Assertions.assertEquals(pathCosts.get(i),expected.get(i));
        }
    }

}
