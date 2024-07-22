import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class tests the Frontend class's submenus, ensuring that they print out the correct
 * statements based on valid and invalid user input.
 * 
 * @author dianakotsonis
 */
public class FrontendDeveloperTests {

  /**
   * This test confirms that the loadFile() method works correctly by ensuring the correct message
   * is printed for valid user input
   */
  @Test
  public void testLoadFile() {
    // Confirm that the test prints out the correct message for valid user input
    {
      try {
        // Instantiate TextUITester class with the valid user input
        TextUITester frontendText = new TextUITester("example.dot");
        // Instantiate objects to create a frontend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        Scanner userInput = new Scanner(System.in);
        Frontend frontend = new Frontend(backend, userInput);
        // Run the desired frontend method
        frontend.loadFile("");
        // Check the output of the frontend method, ensure its the initial message for loadFile()
        String output = frontendText.checkOutput();
        assertTrue(output.startsWith("Please enter the file path for the navigation data file:"),
            "Starting message for loadFile() method is incorrect");
        assertTrue(output.contains(
            "All files should be in a DOT format, and the file path must be given as well, I.E"),
            "Message for loadFile() is incorrect");
        assertTrue(output.contains("/users/folder/example.dot"),
            "Message for loadFile() is incorrect");
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown when it was not supposed to");
      }
    }

  }

  /**
   * This method tests edge case inputs for the loadFile() method, and ensure that the correct
   * message is printed out. Invalid inputs do not have a .dot at the end. Files read unsucessfully
   * print out a "fail" statement.
   */
  @Test
  public void testLoadFileEdgeCases() {
    // 1. Confirm that the test prints an incorrect statement when the file input is invalid (not a
    // dot file)
    {
      try {
        // Instantiate TextUITester class with the invalid user input
        TextUITester frontendText = new TextUITester("example");
        // Instantiate objects to create a frontend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        Scanner userInput = new Scanner(System.in);
        Frontend frontend = new Frontend(backend, userInput);
        // Run the desired frontend method
        frontend.loadFile("");
        // Check the output of the frontend method, ensure its the initial message for loadFile()
        String output = frontendText.checkOutput();
        assertTrue(output.contains("Please try again with a .dot formatted file"),
            "loadFile() did not print the correct statement for incorrect input");
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown for loadFile() when it was not supposed to");
      }
    }
    // 2. Confirm that when a file input is valid, ensure the file message is printed out after
    // calling the Backend for a file that is in valid format, but is unsuccessful in reading in
    // from the Backend
    {
      try {
        // Instantiate TextUITester class with the valid user input
        TextUITester frontendText = new TextUITester("user/example.dot\nm");
        // Instantiate objects to create a frontend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        Scanner userInput = new Scanner(System.in);
        Frontend frontend = new Frontend(backend, userInput);
        // Run the desired frontend method
        frontend.loadFile("");
        // Check the output of the frontend method, ensure its the initial message for loadFile()
        // and main menu()
        String output = frontendText.checkOutput();
        assertTrue(output.startsWith("Please enter the file path for the navigation data file:"),
            "Starting message for loadFile() method is incorrect");
        assertTrue(output.contains("File not read successfully. Please try again."),
            "File message not printed for loadFile()");
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown when it was not supposed to");
      }
    }
  }

  /**
   * This test confirms that the showDatasetStatistics() method outputs the desired message for the
   * number of buildings (nodes), the number of edges, and the total walking time (sum of all edge
   * weights) in the graph.
   */
  @Test
  public void testDatasetStatistics() {
    // 1. Ensure the message for this method is correct
    {
      try {
        // Instantiate TextUITester class "" user input (because for this test, we don't check user
        // input
        TextUITester frontendText = new TextUITester("");
        // Instantiate objects to create a frontend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        Scanner userInput = new Scanner(System.in);
        Frontend frontend = new Frontend(backend, userInput);
        // Run the desired frontend method
        frontend.showDatasetStatistics();
        // Check the output of the frontend method, ensure its the initial message for
        // showDatasetStatistics()
        String output = frontendText.checkOutput();
        assertTrue(output.startsWith("Number of Buildings:"),
            "Starting message for showDatasetStatistics() method is incorrect");
        assertTrue(output.contains("Number of edges:"),
            "Message for showDataSetStatistics() is incorrect");
        assertTrue(output.contains("Total Walking Time:"),
            "Message for showDataSetStatistics() is incorrect");
        assertTrue(output.contains("Total Walking Time:"), "Press m to return to main menu");
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown when it was not supposed to");
      }
    }
    // 2. Ensure that even with input, the message still prints out for the method
    {
      try {
        // Instantiate TextUITester class "b" user input (should not return to main menu)
        TextUITester frontendText = new TextUITester("b");
        // Instantiate objects to create a frontend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        Scanner userInput = new Scanner(System.in);
        Frontend frontend = new Frontend(backend, userInput);
        // Run the desired frontend method
        frontend.showDatasetStatistics();
        // Check the output of the frontend method, ensure its the initial message for
        // showDatasetStatistics() and NOT main menu
        String output = frontendText.checkOutput();
        assertTrue(output.startsWith("Number of Buildings:"),
            "Starting message for showDatasetStatistics() method is incorrect");
        assertTrue(!(output.contains("Main menu")),
            "Main menu isn't returned to with valid input for showDatasetStatistics()");
        assertTrue(!(output.contains("Input number to open:")),
            "Main menu isn't returned to with valid input for showDatasetStatistics()");
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown when it was not supposed to");
      }
    }
  }

  /**
   * This test confirms that the findShortestPath() method outputs the correct data (the shortest
   * path between those buildings, including all buildings on the way, the estimated walking time
   * for each segment, and the total time it takes to walk the path.
   */
  @Test
  public void testShortestPath() {
    // 1. Test that the message for findShortestPath() method is correct
    {
      try {
        // Instantiate TextUITester class with valid user input
        TextUITester frontendText = new TextUITester("Van Vleck,Van Hise");
        // Instantiate objects to create a frontend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        Scanner userInput = new Scanner(System.in);
        Frontend frontend = new Frontend(backend, userInput);
        // Run the desired frontend method
        frontend.findShortestPath("", "");
        // Check the output of the frontend method, ensure its the initial message for
        // findShortestPath()
        String output = frontendText.checkOutput();
        assertTrue(
            output.contains(
                "Please enter the start building name and end building name, separated by a comma"),
            "message for findShortestPath() method is incorrect");
        // assertTrue((output.contains("Van Hise Hall")), "Message for findShortestPath() is
        // incorrect");
        // assertTrue(output.contains("Radio Hall"), "message for findShortestpath() is incorrect");
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown when it was not supposed to");
      }
    }
  }

  /**
   * This method tests for additional edge cases in the methods findShortestpath() method. This
   * includes building names not found in the file, and user input not separated by a comma
   */
  @Test
  public void testshortestPathEdgeCases() {
    // 1. Test that if the building names are not found in the file, the correct statement is
    // printed
    {
      try {
        // Instantiate TextUITester class with invalid user input
        TextUITester frontendText = new TextUITester("Van Vbeck,Van Bhise");
        // Instantiate objects to create a frontend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        Scanner userInput = new Scanner(System.in);
        Frontend frontend = new Frontend(backend, userInput);
        // Run the desired frontend method
        frontend.findShortestPath("", "");
        // Check the output of the frontend method, ensure an exception is thrown
        String output = frontendText.checkOutput();
        assertTrue(output.contains("The building names were not found in the file. Try again"),
            "Incorrect statement for findShortestPath()");
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown incorrectly for findShortestPath()");
      }
    }
    // 2. Test that if the user does not have a comma separating the bulidings, the correct
    // statement is printed
    {
      try {
        // Instantiate TextUITester class with invalid user input
        TextUITester frontendText = new TextUITester("Van Vleck Van Hise");
        // Instantiate objects to create a frontend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        Scanner userInput = new Scanner(System.in);
        Frontend frontend = new Frontend(backend, userInput);
        // Run the desired frontend method
        frontend.findShortestPath("", "");
        // Check the output of the frontend method, ensure an exception is thrown
        String output = frontendText.checkOutput();
        assertTrue(output.contains("Please separate by commas and try again"),
            "Incorrect statement for no commas in findShortestPath()");
      } catch (IllegalArgumentException e) {
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown incorrectly for findShortestPath()");
      }
    }
  }

  /**
   * This integration test checks that the readFile() method from the backend returns the correct
   * value (true or false) to the frontend based on the file input by the user.
   */
  @Test
  public void testIntegration1() {
    // 1. Confirm that readFile() returns true to the frontend if the file exists
    {
      try {
        // Instantiate the TextUITester with a user input that results in a valid .dot file
        TextUITester frontendText = new TextUITester("campus.dot");
        // Instantiate objects to create a frontend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        Scanner userInput = new Scanner(System.in);
        Frontend frontend = new Frontend(backend, userInput);
        // Call the frontend.loadFile() method, ensure that the readData() backend method returns
        // true based on what the frontend outputs
        frontend.loadFile("");
        // Check the output of loadFile and ensure it is the output that returns when backend's
        // readData() method returns true
        String output = frontendText.checkOutput();
        assertTrue(output.contains("File read successfully."),
            "Backend did not readData() successfully");
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown when it was not supposed to");
      }
    }
    // 2. Confirm that readFile() returns false to the frontend if the file does not exist
    {
      try {
        // Instantiate the TextUITester with a user input that results in an .dot file not found
        TextUITester frontendText = new TextUITester("exmple.dot");
        // Instantiate objects to create a frontend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        Scanner userInput = new Scanner(System.in);
        Frontend frontend = new Frontend(backend, userInput);
        // Call the frontend.loadFile() method, ensure that the readData() backend method returns
        // false based on what the frontend outputs
        frontend.loadFile("");
        String output = frontendText.checkOutput();
        assertTrue(output.contains("File not read successfully. Please try again."),
            "Backend did not return false for readData() when it should have");
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown when it was not supposed to");
      }
    }
  }

  /**
   * This tests that the backend's findShortestPath() and pathData() methods return the correct
   * information to the frontend.
   */
  @Test
  public void testIntegration2() {
    // Create a graph used by the backend for both method tests (insert the desired nodes and edges)
    // GraphADT<String, Integer> graph = new DijkstraGraph<String, Integer>();
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
    graph.insertNode("building1");
    graph.insertNode("building2");
    graph.insertNode("building3");
    graph.insertNode("building4");
    graph.insertEdge("building1", "building2", 1.0);
    graph.insertEdge("building2", "building3", 2.0);
    graph.insertEdge("building3", "building4", 3.0);
    graph.insertEdge("building4", "building1", 4.0);
    graph.insertEdge("building1", "building4", 10.0);
    graph.insertEdge("building1", "building3", 2.0);

    // 1a. Test that the findShortestPath() method returns the correct values by checking the
    // frontend's output
    {
      try {
        // Instantiate the user input with the start and end building for findShortestPath()
        TextUITester frontendText = new TextUITester("building1,building4");
        // Pass this graph into the Backend object, and use it to make the frontend object used for
        // testing
        Backend backend = new Backend(graph);
        Scanner userInput = new Scanner(System.in);
        Frontend frontend = new Frontend(backend, userInput);
        // Call the corresponding frontend method, save its output
        frontend.findShortestPath("", "");
        String output = frontendText.checkOutput();
        // Confirm that what is output corresponds to the correct shortest path:
        assertTrue(output.contains("building1"), "shortestPath data is incorrect");
        assertTrue(output.contains("building3"), "shortestPath data is incorrect");
        assertTrue(output.contains("building4"), "shortestPath data is incorrect");
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown when it wasn't supposed to");
      }
    }
    // 1b. Test that the findShortestPath() returns the correct value for another shortest path from
    // the graph
    {
      try {
        // Instantiate the user input with the start and end building for findShortestPath()
        TextUITester frontendText = new TextUITester("building1,building3");
        // Pass this graph into the Backend object, and use it to make the frontend object used for
        // testing
        Backend backend = new Backend(graph);
        Scanner userInput = new Scanner(System.in);
        Frontend frontend = new Frontend(backend, userInput);
        // Call the corresponding frontend method, save its output
        frontend.findShortestPath("", "");
        String output = frontendText.checkOutput();
        // Confirm that what is output corresponds to the correct shortest path:
        assertTrue(output.contains("building1"), "shortestPath data is incorrect");
        assertTrue(output.contains("building3"), "shortestPath data is incorrect");
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown when it wasn't supposed to");
      }
    }
    // 2. Test that pathData() returns the correct values from checking the frontend's output
    {
      try {
        // Instantiate the user input with nothing (since this method doesn't require any inputs)
        TextUITester frontendText = new TextUITester("building1,building4");
        // Pass this graph into the Backend object, and use it to make the frontend object used for
        // testing
        Backend backend = new Backend(graph);
        backend.totalTime = 22.0;
        Scanner userInput = new Scanner(System.in);
        Frontend frontend = new Frontend(backend, userInput);
        // Call the corresponding frontend method, save its output
        frontend.findShortestPath("building1", "building4");
        frontend.showDatasetStatistics();
        String output = frontendText.checkOutput();
        // Confirm what is output corresponds to the correct shortest path:
        // Note: Because of the Backend method dividing the edges and time by 2, the correct values
        // become 3 (6/2) and 11 (22/2) for the total number of edges and time
        assertTrue(output.contains("Buildings: 4"), "pathData() return value is incorect");
        assertTrue(output.contains("edges: 3"), "pathData() return value is incorect");
        assertTrue(output.contains("Time: 11"), "pathData() return value is incorect");
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown when it wasn't supposed to");
      }
    }
  }

  /**
   * For my first partner test, I confirmed that their implementation works for a small graph that
   * is not provided from the campus.dot file
   */
  @Test
  public void testPartnerCode1() {
    // Create a simple graph that the backend will use for its methods
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
    graph.insertNode("node1");
    graph.insertNode("node2");
    graph.insertNode("node3");
    graph.insertEdge("node1", "node2", 10.0);
    graph.insertEdge("node2", "node3", 2.0);
    graph.insertEdge("node1", "node3", 20.0);


    // 1. Confirm the getShortestPath() method prints the correct results for a path in this graph
    {
      // Create a backend object with this graph
      Backend backend = new Backend(graph);
      // Call the getShortestPath() method, confirm it returns the correct array contents
      ArrayList<String> returnedList = backend.findShortestPath("node1", "node3");
      // Confirm the returned list contains the correct order of nodes and correct total cost
      assertEquals(returnedList.get(0), "[node1, node2, node3]",
          "Backend did not return correct path from node1 to node3");
      assertEquals(returnedList.get(1), "Total Time: 12.0",
          "Backend did not return correct total time from node1 to node3");
    }
    // 2. Confirm the getWalkingTimes() method returns the correct information
    {
      // Create a backend object with this graph
      Backend backend = new Backend(graph);
      // Call the getWalkingTimes() method, confirm it returns the correct array contents
      ArrayList<Double> returnedList = backend.getWalkingTimes("node1", "node3");
      // Create an expected list
      ArrayList<Double> expectedList = new ArrayList<Double>();
      expectedList.add(10.0);
      expectedList.add(2.0);
      // Check that both lists have the same contents
      for (int i = 0; i < expectedList.size(); i++) {
        assertEquals(expectedList.get(i), returnedList.get(i),
            "Contents of the array from getWalkingTimes() are not equal");
      }
    }
  }

  /**
   * Since my partner's backend test file only confirmed that one path was implemented correctly, I
   * am going to confirm that another path works with the backend as well by testing the path
   * between Van Hise Hall and Radio Hall, and confirming that all methods output the correct data
   */
  @Test
  public void testPartnerCode2() {
    // 1. Confirm that the findShortestPath() method returns the correct information for the pathway
    // between Van Hise Hall and Radio Hall
    {
      // Create a backend object
      DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
      Backend backend = new Backend(graph);
      // Load in the desird data file
      backend.readData(new File("campus.dot"));
      // Call the getShortestPath() method, confirm it returns the correct array contents
      ArrayList<String> returnedList = backend.findShortestPath("Van Hise Hall", "Radio Hall");
      // Confirm that the returned list contains the correct order of buildings in the shortest path
      assertEquals(returnedList.get(0),
          "[Van Hise Hall, Carillon Tower, Bascom Hall, Education Building, Radio Hall]",
          "returned list did not contain correct path between Van Hise Hall and Radio Hall");
      // Confirm that the total walking time in the array is correct
      assertEquals(returnedList.get(returnedList.size() - 1), "Total Time: 602.7");
    }
    // 2. Confirm that the getWalkingTimes() method returns the correct information for the pathway
    // between Van Hise Hall and Radio Hall
    {
      // Create a backend object
      DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
      Backend backend = new Backend(graph);
      // Load in the desird data file
      backend.readData(new File("campus.dot"));
      // Call the getWalkingTimes() method, confirm it returns the correct array contents
      ArrayList<Double> returnedList = backend.getWalkingTimes("Van Hise Hall", "Radio Hall");
      // Create an expected list
      ArrayList<Double> expectedList = new ArrayList<Double>();
      expectedList.add(171.9);
      expectedList.add(103.0);
      expectedList.add(214.80000000000004);
      expectedList.add(113.00000000000001);
      // Check that both lists have the same contents
      for (int i = 0; i < expectedList.size(); i++) {
        assertEquals(expectedList.get(i), returnedList.get(i),
            "Contents of the array from getWalkingTimes() are not equal");
      }
    }
  }

  /**
   * This partner test confirms that exceptions are handled by the backend. For the readData()
   * method, it confirms that the correct return value is thrown and that no exceptions are thrown
   * for invalid files. For the other methods, it confirms that they handle null parameters by
   * outputting a specific message to the users.
   */
  @Test
  public void testPartnerCode3() {
    // 1a. Test that findShortestPath() throws NullPointerExceptions with a message specific to the
    // graph
    {
      try {
        // Create a backend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        // Call the findShortestPath() method with null parameters
        backend.findShortestPath(null, null);
        assertTrue(false, "null pointer exception not thrown when it was supposed to");
      } catch (NullPointerException e) {
        String stack = e.toString();
        // Confirm that the NullPointerException has a message that is specific for the graph
        assertTrue(stack.contains("null keys not allowed"),
            "Non-specific message is used for findShortestPath NullPointerException");
      } catch (Exception e) {
        assertTrue(false, "Incorrect exception was thrown");
      }
    }
    // 1b. Test that the getShortestPath() method throws NullPointerExceptions with a message
    // specific to the graph
    {
      try {
        // Create a backend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        // Call the findShortestPath() method with null parameters
        backend.getShortestPath(null, null);
        assertTrue(false, "null pointer exception not thrown when it was supposed to");
      } catch (NullPointerException e) {
        String stack = e.toString();
        // Confirm that the NullPointerException has a message that is specific for the graph
        assertTrue(stack.contains("null keys not allowed"),
            "Non-specific message is used for findShortestPath NullPointerException");
      } catch (Exception e) {
        assertTrue(false, "Incorrect exception was thrown");
      }
    }
    // 1c. Test that the getWalkingTimes() method throws NullPointerExceptions with a message
    // specific to the graph
    {
      try {
        // Create a backend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        // Call the findShortestPath() method with null parameters
        backend.getWalkingTimes(null, null);
        assertTrue(false, "null pointer exception not thrown when it was supposed to");
      } catch (NullPointerException e) {
        // Confirm that the NullPointerException has a message that is specific for the graph
        String stack = e.toString();
        assertTrue(stack.contains("null keys not allowed"),
            "Non-specific message is used for findShortestPath NullPointerException");
      } catch (Exception e) {
        assertTrue(false, "Incorrect exception was thrown");
      }
    }
    // 1d. Test that the getTotalTime() method throws NullPointerExceptions with a message specific
    // to the graph
    {
      try {
        // Create a backend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        // Call the findShortestPath() method with null parameters
        backend.getTotalTime(null, null);
        assertTrue(false, "null pointer exception not thrown when it was supposed to");
      } catch (NullPointerException e) {
        String stack = e.toString();
        // Confirm that the NullPointerException has a message that is specific for the graph
        assertTrue(stack.contains("null keys not allowed"),
            "Non-specific message is used for findShortestPath NullPointerException");
      } catch (Exception e) {
        assertTrue(false, "Incorrect exception was thrown");
      }
    }
    // 2. Confirm that the readData() method can properly handle invalid Files input (by confirming
    // it returns false
    {
      try {
        // Create a backend object
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
        Backend backend = new Backend(graph);
        // Confirm that the readData() method returns false if the file does not exist
        assertTrue(!backend.readData(new File("notRealFile")),
            "backend did not return false for readData() when it was supposed to");
      } catch (Exception e) {
        assertTrue(false, "Exception was thrown when it was not supposed to");
      }
    }
  }
}
