// Name: Diana Kotsonis
// Email: dakotsonis@wisc.edu
// Group and Team: E18
// Group TA: Lakshika Rathi
// Lecturer: Gary Dahl
// Notes to Grader: N/A

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.PriorityQueue;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class extends the BaseGraph data structure with additional methods for computing the total
 * cost and list of node data along the shortest path connecting a provided starting to ending
 * nodes. This class makes use of Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType, EdgeType>
    implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode contains data about one
   * specific path between the start node and another node in the graph. The final node in this path
   * is stored in its node field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referened by the predecessor field (this field is
   * null within the SearchNode containing the starting node in its node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost SearchNode has the
   * highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node; // Final node in this path
    public double cost; // Total cost of this path
    public SearchNode predecessor; // Predecessor field of final node

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost)
        return +1;
      if (cost < other.cost)
        return -1;
      return 0;
    }
  }

  /**
   * Constructor that sets the map that the graph uses.
   * 
   * @param map the map that the graph uses to map a data object to the node object it is stored in
   */
  public DijkstraGraph(MapADT<NodeType, Node> map) {
    super(map);
  }

  /**
   * This helper method creates a network of SearchNodes while computing the shortest path between
   * the provided start and end locations. The SearchNode that is returned by this method is
   * represents the end of the shortest path that is found: it's cost is the cost of that shortest
   * path, and the nodes linked together through predecessor references represent all of the nodes
   * along that shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found or when either start or
   *                                end data do not correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {

    if (!nodes.containsKey(start) || !nodes.containsKey(end)) {
      throw new NoSuchElementException();
    }
    Node startNode = nodes.get(start); // The Node object for the start data
    Node nodeToRetrieve = null; // The key used to find the SearchNode to return from the map
    SearchNode duplicateNode = null; // The node used to check if the successor's node is already in
    // the Priority Queue

    // Create a PriorityQueue that stores all potential nodes to visit (and is sorted based on cost)
    PriorityQueue<SearchNode> priorityList = new PriorityQueue<SearchNode>();
    // Create a placeholder map that stores all nodes already visited (which are stored in
    // SearchNodes)
    PlaceholderMap<Node, SearchNode> map = new PlaceholderMap<Node, SearchNode>();

    // If the start NodeType isn't null, add its Node to the priorityList. Otherwise, throw an
    // exception
    if (start != null) {
      SearchNode searchStart = new SearchNode(startNode, 0.0, null);
      priorityList.add(searchStart);
    } else {
      throw new NoSuchElementException("Start Nodetype is null");
    }
    // Throw an exception if the end NodeType is null
    if (end == null) {
      throw new NoSuchElementException("End NodeType is null");
    }

    // Iterate through the priorityList until it is empty
    while (!(priorityList.isEmpty() || priorityList.size() == 0)) {
      // Remove the next SearchNode in PriorityList, add it to PlaceholderMap (if PlaceholderMap
      // doesn't already contain it). If it is already in the map, continue to the next condition
      SearchNode removedNode = priorityList.remove();
      if (!(map.containsKey(removedNode.node))) {
        map.put(removedNode.node, removedNode);
        if (removedNode.node.data.equals(end)) {
          // If the removedNode = the desired end value, save its value in the nodeToRetrieve object
          nodeToRetrieve = removedNode.node;
        }
      } else {
        continue;
      }
      // Add all successors of the removedNode to the priorityList
      if (removedNode.node.edgesLeaving != null || removedNode.node.edgesLeaving.size() == 0) {
        for (int i = 0; i < removedNode.node.edgesLeaving.size(); i++) {
          // Save the successor's node and cost
          Node successorNode = removedNode.node.edgesLeaving.get(i).successor;
          Edge cost = removedNode.node.edgesLeaving.get(i);
          Double doubleCost = cost.data.doubleValue() + removedNode.cost;
          // Create a SearchNode object with the node and cost data
          SearchNode successorSearchNode = new SearchNode(successorNode, doubleCost, removedNode);
          // Iterate through the priorityList to check if the successorNode is already in the queue
          Iterator<SearchNode> iterator = priorityList.iterator();
          while (iterator.hasNext()) {
            SearchNode nextNode = iterator.next();
            duplicateNode = null;
            // If a duplicate Node is found, check if the successorNode's cost is less than the
            // already inserted node's cost.
            if (nextNode.node.data.equals(successorSearchNode.node.data)) {
              duplicateNode = nextNode;
              if (nextNode.cost > successorSearchNode.cost) {
                // If the successorNode's cost is less than the already inserted node's cost, remove
                // the duplicateNode and add the successorNode to the priorityList. If it is greater
                // than, continue without inserting anything new into the priorityList
                priorityList.remove(nextNode);
                priorityList.add(successorSearchNode);
              }
              break;
            }
          }
          // If the duplicateNode hasn't been initailzed from the iterator loop, add the successor
          // SearchNode into the priorityList
          if (duplicateNode == null) {
            priorityList.add(successorSearchNode);
          }
        }
      }
    }
    // If the end node was never found in the map, throw an exception
    if (nodeToRetrieve == null) {
      throw new NoSuchElementException("There is no way to connect start and end node");
    }
    // Otherwise, find and return the end node from the map
    return map.get(nodeToRetrieve);
  }

  /**
   * Returns the list of data values from nodes along the shortest path from the node with the
   * provided start value through the node with the provided end value. This list of data values
   * starts with the start value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shorteset path. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
    // Save an instance of the returned SearchNode from the computeShortestPath() helper method
    SearchNode returnedSearchNode = computeShortestPath(start, end);
    LinkedList<NodeType> toReturn = new LinkedList<NodeType>(); // The list that contains the path
    // data to return
    SearchNode current = returnedSearchNode;
    int size = 1; // The number of nodes in the SearchNode predecessor chain. It is initialized to 1
    // to account for the current SearchNode
    while (current.predecessor != null) {
      // update the current SearchNode to its predecessor and increment the size of the chain until
      // there are no more predecessors.
      current = current.predecessor;
      size++;
    }
    for (int i = size; i > 0; i--) {
      // Starting with the end SearchNode, add (to the front of the list) the next SearchNode in the
      // chain of predecessors
      toReturn.addFirst(returnedSearchNode.node.data);
      returnedSearchNode = returnedSearchNode.predecessor;
    }
    return toReturn;
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest path freom the node
   * containing the start data to the node containing the end data. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
  public double shortestPathCost(NodeType start, NodeType end) {
    // Save an instance of the returned SearchNode from the computeShortestPath() helper method
    SearchNode returnedNode = computeShortestPath(start, end);
    double totalCost = returnedNode.cost;
    // Return the SearchNode's total cost
    return totalCost;
  }

  /**
   * This test method uses the example traced through in lecture, and confirms that the results of
   * the computeShortestPath() method return the same path we computed in lecture. Specifically, we
   * test the path from the node D to the node I in the graph from lecture
   */
  @Test
  public void test1() {
    {
      // 1. Create a Graph identical to the example in lecture
      DijkstraGraph<NodeType, EdgeType> graph = new DijkstraGraph<>(new PlaceholderMap<>());

      // Create nodes for the correct shortest pathway: DAHI
      String stringA = new String("a");
      NodeType nodeA = (NodeType) stringA;
      Node a = new Node(nodeA);
      String stringH = new String("h");
      NodeType nodeH = (NodeType) stringH;
      Node h = new Node(nodeH);
      String stringD = new String("d");
      NodeType nodeD = (NodeType) stringD;
      Node d = new Node(nodeD);
      String stringI = new String("i");
      NodeType nodeI = (NodeType) stringI;
      Node i = new Node(nodeI);

      // Create NodeType objects for other nodes in the graph
      String stringB = new String("b");
      NodeType nodeB = (NodeType) stringB;
      String stringM = new String("m");
      NodeType nodeM = (NodeType) stringM;

      String stringF = new String("f");
      NodeType nodeF = (NodeType) stringF;
      String stringG = new String("g");
      NodeType nodeG = (NodeType) stringG;

      String stringL = new String("l");
      NodeType nodeL = (NodeType) stringL;
      String stringE = new String("e");
      NodeType nodeE = (NodeType) stringE;

      // Create EdgeType objects for edges that connect the nodes in the graph
      Integer intAB = 1;
      EdgeType edgeAB = (EdgeType) intAB;
      Integer intAM = 5;
      EdgeType edgeAM = (EdgeType) intAM;
      Integer intBM = 3;
      EdgeType edgeBM = (EdgeType) intBM;

      Integer intAH = 8;
      EdgeType edgeAH = (EdgeType) intAH;
      Integer intHB = 6;
      EdgeType edgeHB = (EdgeType) intHB;
      Integer intME = 3;
      EdgeType edgeME = (EdgeType) intME;

      Integer intMF = 4;
      EdgeType edgeMF = (EdgeType) intMF;
      Integer intFG = 9;
      EdgeType edgeFG = (EdgeType) intFG;
      Integer intGL = 7;
      EdgeType edgeGL = (EdgeType) intGL;

      Integer intDG = 2;
      EdgeType edgeDG = (EdgeType) intDG;
      Integer intDA = 7;
      EdgeType edgeDA = (EdgeType) intDA;
      Integer intID = 1;
      EdgeType edgeID = (EdgeType) intID;

      Integer intIL = 5;
      EdgeType edgeIL = (EdgeType) intIL;
      Integer intHI = 2;
      EdgeType edgeHI = (EdgeType) intHI;
      Integer intIH = 2;
      EdgeType edgeIH = (EdgeType) intIH;

      // Insert the NodeType and EdgeType objects into the graph
      graph.insertNode(nodeA);
      graph.insertNode(nodeB);
      graph.insertNode(nodeD);
      graph.insertNode(nodeE);
      graph.insertNode(nodeF);
      graph.insertNode(nodeG);
      graph.insertNode(nodeH);
      graph.insertNode(nodeI);
      graph.insertNode(nodeL);
      graph.insertNode(nodeM);

      graph.insertEdge(nodeA, nodeB, edgeAB);
      graph.insertEdge(nodeA, nodeM, edgeAM);
      graph.insertEdge(nodeA, nodeH, edgeAH);
      graph.insertEdge(nodeB, nodeM, edgeBM);
      graph.insertEdge(nodeD, nodeA, edgeDA);
      graph.insertEdge(nodeD, nodeG, edgeDG);
      graph.insertEdge(nodeF, nodeG, edgeFG);
      graph.insertEdge(nodeG, nodeL, edgeGL);
      graph.insertEdge(nodeH, nodeB, edgeHB);
      graph.insertEdge(nodeH, nodeI, edgeHI);
      graph.insertEdge(nodeI, nodeH, edgeIH);
      graph.insertEdge(nodeI, nodeD, edgeID);
      graph.insertEdge(nodeI, nodeL, edgeIL);
      graph.insertEdge(nodeM, nodeE, edgeME);
      graph.insertEdge(nodeM, nodeF, edgeMF);

      // 2. Create an expected SearchNode object that contains the expected Cost, end node, and
      // predecessor for the shortest path from D to I
      double expectedCost1 = 0;
      SearchNode expected1 = new SearchNode(d, expectedCost1, null);
      double expectedCost2 = 7;
      SearchNode expected2 = new SearchNode(a, expectedCost2, expected1);
      double expectedCost3 = 15;
      SearchNode expected3 = new SearchNode(h, expectedCost3, expected2);
      double expectedCost = 17;
      SearchNode expected = new SearchNode(i, expectedCost, expected3);

      // 3. Call the computeShortestPath() method and store its result in a SearchNode object
      SearchNode actual = graph.computeShortestPath(nodeD, nodeI);

      // 4. Compare the two SearchNodes and confirm that they are equal (and that they have the same
      // chain of predecessor nodes)
      while (actual.predecessor != null) {
        assertEquals(actual.node.data, expected.node.data,
            "The expected and actual SearchNode returned for D to I's path is incorrect");
        actual = actual.predecessor;
        expected = expected.predecessor;
      }
    }
  }

  /**
   * This test uses the same graph as test1(), but now considers the shortest path from A to M. It
   * checks the cost and sequence of data along the shortest path between these nodes, and confirms
   * that they are correct.
   */
  @Test
  public void test2() {
    {
      // 1. Create a Graph identical to the example in lecture
      DijkstraGraph<NodeType, EdgeType> graph = new DijkstraGraph<>(new PlaceholderMap<>());

      // Create NodeType objects for nodes in the graph
      String stringA = new String("a");
      NodeType nodeA = (NodeType) stringA;
      String stringH = new String("h");
      NodeType nodeH = (NodeType) stringH;
      String stringD = new String("d");
      NodeType nodeD = (NodeType) stringD;
      String stringI = new String("i");
      NodeType nodeI = (NodeType) stringI;

      String stringB = new String("b");
      NodeType nodeB = (NodeType) stringB;
      String stringM = new String("m");
      NodeType nodeM = (NodeType) stringM;

      String stringF = new String("f");
      NodeType nodeF = (NodeType) stringF;
      String stringG = new String("g");
      NodeType nodeG = (NodeType) stringG;

      String stringL = new String("l");
      NodeType nodeL = (NodeType) stringL;
      String stringE = new String("e");
      NodeType nodeE = (NodeType) stringE;

      // Create EdgeType objects for edges that connect the nodes in the graph
      Integer intAB = 1;
      EdgeType edgeAB = (EdgeType) intAB;
      Integer intAM = 5;
      EdgeType edgeAM = (EdgeType) intAM;
      Integer intBM = 3;
      EdgeType edgeBM = (EdgeType) intBM;

      Integer intAH = 8;
      EdgeType edgeAH = (EdgeType) intAH;
      Integer intHB = 6;
      EdgeType edgeHB = (EdgeType) intHB;
      Integer intME = 3;
      EdgeType edgeME = (EdgeType) intME;

      Integer intMF = 4;
      EdgeType edgeMF = (EdgeType) intMF;
      Integer intFG = 9;
      EdgeType edgeFG = (EdgeType) intFG;
      Integer intGL = 7;
      EdgeType edgeGL = (EdgeType) intGL;

      Integer intDG = 2;
      EdgeType edgeDG = (EdgeType) intDG;
      Integer intDA = 7;
      EdgeType edgeDA = (EdgeType) intDA;
      Integer intID = 1;
      EdgeType edgeID = (EdgeType) intID;

      Integer intIL = 5;
      EdgeType edgeIL = (EdgeType) intIL;
      Integer intHI = 2;
      EdgeType edgeHI = (EdgeType) intHI;
      Integer intIH = 2;
      EdgeType edgeIH = (EdgeType) intIH;

      // Insert the NodeType and EdgeType objects into the graph
      graph.insertNode(nodeA);
      graph.insertNode(nodeB);
      graph.insertNode(nodeD);
      graph.insertNode(nodeE);
      graph.insertNode(nodeF);
      graph.insertNode(nodeG);
      graph.insertNode(nodeH);
      graph.insertNode(nodeI);
      graph.insertNode(nodeL);
      graph.insertNode(nodeM);

      graph.insertEdge(nodeA, nodeB, edgeAB);
      graph.insertEdge(nodeA, nodeM, edgeAM);
      graph.insertEdge(nodeA, nodeH, edgeAH);
      graph.insertEdge(nodeB, nodeM, edgeBM);
      graph.insertEdge(nodeD, nodeA, edgeDA);
      graph.insertEdge(nodeD, nodeG, edgeDG);
      graph.insertEdge(nodeF, nodeG, edgeFG);
      graph.insertEdge(nodeG, nodeL, edgeGL);
      graph.insertEdge(nodeH, nodeB, edgeHB);
      graph.insertEdge(nodeH, nodeI, edgeHI);
      graph.insertEdge(nodeI, nodeH, edgeIH);
      graph.insertEdge(nodeI, nodeD, edgeID);
      graph.insertEdge(nodeI, nodeL, edgeIL);
      graph.insertEdge(nodeM, nodeE, edgeME);
      graph.insertEdge(nodeM, nodeF, edgeMF);

      // 2. Check the cost of the data from A to G using shortestPathCost() method
      double expected = 13.0; // The expected cost should be 13;
      double actual = graph.shortestPathCost(nodeA, nodeG);
      assertEquals(expected, actual, "shortestPath cost from A to G is incorrect");

      // 3. Check the sequence of data from A to G using the shortestPathData() method

      // Create an LinkedList with the expected nodes you go through when finding the shortestPath
      // from A to G
      LinkedList<NodeType> expectedList = new LinkedList<NodeType>();
      expectedList.add(nodeA);
      expectedList.add(nodeH);
      expectedList.add(nodeI);
      expectedList.add(nodeD);
      expectedList.add(nodeG);

      // Call shortestPathData and store its return in a LinkedList
      List<NodeType> list = graph.shortestPathData(nodeA, nodeG);
      LinkedList<NodeType> actualList = (LinkedList<NodeType>) list;

      // Confirm that the lists are equal
      for (int j = 0; j < expectedList.size(); j++) {
        assertEquals(actualList.get(j), expectedList.get(j),
            "The shortestPathData() from A to G is incorrect");
      }
    }
  }

  /**
   * This method checks the behavior when the nodes we are finding a path for exists in the graph,
   * but there is no sequence of directed edges that connects them from start to end. In this case,
   * the methods throw a NullPointerException. In this test, we try to find the path from nodeF to
   * nodeM (which is an impossible path)
   */
  @Test
  public void test3() {
    {
      // 1. Create a Graph identical to the example in lecture
      DijkstraGraph<NodeType, EdgeType> graph = new DijkstraGraph<>(new PlaceholderMap<>());

      // Create NodeType objects for nodes in the graph
      String stringA = new String("a");
      NodeType nodeA = (NodeType) stringA;
      String stringH = new String("h");
      NodeType nodeH = (NodeType) stringH;
      String stringD = new String("d");
      NodeType nodeD = (NodeType) stringD;
      String stringI = new String("i");
      NodeType nodeI = (NodeType) stringI;

      String stringB = new String("b");
      NodeType nodeB = (NodeType) stringB;
      String stringM = new String("m");
      NodeType nodeM = (NodeType) stringM;

      String stringF = new String("f");
      NodeType nodeF = (NodeType) stringF;
      String stringG = new String("g");
      NodeType nodeG = (NodeType) stringG;

      String stringL = new String("l");
      NodeType nodeL = (NodeType) stringL;
      String stringE = new String("e");
      NodeType nodeE = (NodeType) stringE;

      // Create EdgeType objects for edges that connect the nodes in the graph
      Integer intAB = 1;
      EdgeType edgeAB = (EdgeType) intAB;
      Integer intAM = 5;
      EdgeType edgeAM = (EdgeType) intAM;
      Integer intBM = 3;
      EdgeType edgeBM = (EdgeType) intBM;

      Integer intAH = 8;
      EdgeType edgeAH = (EdgeType) intAH;
      Integer intHB = 6;
      EdgeType edgeHB = (EdgeType) intHB;
      Integer intME = 3;
      EdgeType edgeME = (EdgeType) intME;

      Integer intMF = 4;
      EdgeType edgeMF = (EdgeType) intMF;
      Integer intFG = 9;
      EdgeType edgeFG = (EdgeType) intFG;
      Integer intGL = 7;
      EdgeType edgeGL = (EdgeType) intGL;

      Integer intDG = 2;
      EdgeType edgeDG = (EdgeType) intDG;
      Integer intDA = 7;
      EdgeType edgeDA = (EdgeType) intDA;
      Integer intID = 1;
      EdgeType edgeID = (EdgeType) intID;

      Integer intIL = 5;
      EdgeType edgeIL = (EdgeType) intIL;
      Integer intHI = 2;
      EdgeType edgeHI = (EdgeType) intHI;
      Integer intIH = 2;
      EdgeType edgeIH = (EdgeType) intIH;

      // Insert the NodeType and EdgeType objects into the graph
      graph.insertNode(nodeA);
      graph.insertNode(nodeB);
      graph.insertNode(nodeD);
      graph.insertNode(nodeE);
      graph.insertNode(nodeF);
      graph.insertNode(nodeG);
      graph.insertNode(nodeH);
      graph.insertNode(nodeI);
      graph.insertNode(nodeL);
      graph.insertNode(nodeM);

      graph.insertEdge(nodeA, nodeB, edgeAB);
      graph.insertEdge(nodeA, nodeM, edgeAM);
      graph.insertEdge(nodeA, nodeH, edgeAH);
      graph.insertEdge(nodeB, nodeM, edgeBM);
      graph.insertEdge(nodeD, nodeA, edgeDA);
      graph.insertEdge(nodeD, nodeG, edgeDG);
      graph.insertEdge(nodeF, nodeG, edgeFG);
      graph.insertEdge(nodeG, nodeL, edgeGL);
      graph.insertEdge(nodeH, nodeB, edgeHB);
      graph.insertEdge(nodeH, nodeI, edgeHI);
      graph.insertEdge(nodeI, nodeH, edgeIH);
      graph.insertEdge(nodeI, nodeD, edgeID);
      graph.insertEdge(nodeI, nodeL, edgeIL);
      graph.insertEdge(nodeM, nodeE, edgeME);
      graph.insertEdge(nodeM, nodeF, edgeMF);

      // 2. Try calling the computeShortestPath() method with node F to nodeM, ensure it throws the
      // correct exception
      try {
        SearchNode actual = computeShortestPath(nodeF, nodeM);
        assertTrue(false,
            "computeShortestPath method doesn't throw exception when the path between nodes is impossible");
      } catch (NoSuchElementException exception) {
      } catch (Exception exception) {
        assertTrue(false, "computeShortestPath method throws incorrect exception");
      }

      // 3. Call the shortestPathData() method with node F to nodeM, ensure it throws the correct
      // exception
      try {
        List<NodeType> actualList = shortestPathData(nodeF, nodeM);
        assertTrue(false,
            "shortestPathData() method doesn't throw exception when the path between nodes is impossible");
      } catch (NoSuchElementException exception) {
      } catch (Exception exception) {
        assertTrue(false, "shortestPathData() method throws incorrect exception");
      }

      // 4. Call the shortestPathCost() method with nodeF to nodeM, ensure it throws the correct
      // exception
      try {
        Double actualCost = shortestPathCost(nodeF, nodeM);
        assertTrue(false,
            "shortestPathCost() method doesn't throw exception when the path between nodes is impossible");
      } catch (NoSuchElementException exception) {
      } catch (Exception exception) {
        assertTrue(false, "shortestPathCost() method throws incorrect exception");
      }
      
      // 5. Call the computeShortestPath() method with a node that doesn't exist in the graph,
      // ensure it throws the correct exception
      try {
        String stringZ = new String("z");
        NodeType nodeZ = (NodeType) stringZ;
        SearchNode actual = computeShortestPath(nodeZ, nodeA);
        assertTrue(false,
            "computeShortestPath method doesn't throw exception when the path between nodes is impossible");
      } catch (NoSuchElementException exception) {
      } catch (Exception e) {
        assertTrue(false, "computeShortestPath method throws incorrect exception");
      }
    }
  }
}


