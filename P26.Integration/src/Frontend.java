import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.InputMismatchException;

/**
 * This class implements the Frontend of the UW Pathfinder Application. It engages with the user to
 * read in the desired file, and get information about the buildings, paths, and walktime from the
 * file.
 * 
 * @author diana kotsonis
 */
public class Frontend implements FrontendInterface {
  private Backend backend; // The instance of the backend's class
  private Scanner userInput; // Where user input comes from
  private boolean run; // To determine when to close the application (turned to false by exitApp())

  /**
   * This Constructor initializes a reference to the backend and to the scanner object. It also 
   * initializes the boolean run to true
   * 
   * @param backend   an instance of the Backend class
   * @param userInput The scanner instance where user input is read from
   */
  public Frontend(Backend backend, Scanner userInput) {
    this.backend = backend;
    this.userInput = userInput;
    this.run = true;
  }

  /**
   * This main method initializes a frontend object and calls the startApp() method to start the
   * application
   * 
   * @param args unused
   */
  public static void main(String[] args) {
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>(new PlaceholderMap<>());
    Scanner scanner = new Scanner(System.in);
    Backend backend = new Backend(graph);
    // Create an instance of the frontend to call the startApp() method
    Frontend frontend = new Frontend(backend, scanner);
    frontend.startApp();
  }

  /**
   * Main command loop for the user. Calls the mainMenu() loop.
   */
  @Override
  public void startApp() {
    while (run == true) {
      System.out.println("Welcome to UW Path Finder");
      mainMenu();
    }
    if (run == false) {
      userInput.close();
    }
  }

  /**
   * This method displays the main menu of the Path finder app in a loop and calls helper methods(or
   * submenus) based on user input. It also handles exceptions should they be thrown.
   * 
   * @throws IllegalArgumentException if the number entered by user is not in the specified range
   * @throws InputMismatchException   if user input is not an integer value
   */
  @Override
  public void mainMenu() {
    int choice = 1; // Stores the value chosen by the user

    // Print out the main menu options, then try to read in user input
    do {
      System.out.println("Main menu");
      System.out.println("Input number to open");
      System.out.println("1) Input Navigation data");
      System.out.println("2) Show Navigation Statistics");
      System.out.println("3) Input Start and Destination Building");
      System.out.println("4) Exit");

      try {
        if (userInput.hasNext()) {
          choice = userInput.nextInt();
          // If the user chooses 1, go to the loadFile() submenu
          if (choice == 1) {
            userInput.nextLine();
            loadFile("");
            userInput.nextLine();
          }
          // If the user chooses 2, go to the showDatasetStatistics() submenu
          if (choice == 2) {
            userInput.nextLine();
            showDatasetStatistics();
          }
          // If the user chooses 3, go to the findShortestPath() submenu
          if (choice == 3) {
            userInput.nextLine();
            findShortestPath("", "");
          }
          // If the user chooses 4, exit the application
          if (choice == 4) {
            exitApp();
            break;
          }
          // If the user chooses a value not within this range, throw an IllegalArgumentException
          if (choice > 4 || choice < 1) {
            throw new IllegalArgumentException();
          }
        }
      } catch (IllegalArgumentException e) {
        // If a user chooses a value not within the range, print out a message explaining what was
        // incorrect and continue to the finally branch
        System.out.println("Please enter a valid number between 1 and 4");
        userInput.nextLine();
        continue;
      } catch (InputMismatchException e) {
        // If a user inputs a non-integer value, print out a message explaining what was incorrect
        // and continue to the finally branch
        System.out.println("Please enter a number");
        userInput.nextLine();
        continue;
      } finally {
        // Once finished with either catching an exception or returning from a submenu, ask the user
        // to either return to main menu or exit the application.
        if (run == false) {
          return;
        }
        System.out.println("Press m to return to main menu, otherwise press any button to exit");
        if (userInput.hasNext()) {
          String line = userInput.nextLine();
          if (line.contains("m")) {
            continue;
          } else {
            System.out.println("m was not pressed, exiting the application");
            exitApp();
            break;
          }
        }
      }
    } while (choice >= 1 && choice <= 4);
  }

  /**
   * Command to specify and load a data file. Prompts the user to input a file in .dot format with
   * it's file path. If the user does not load in the information correcltly, or the file reading
   * fails, return to mainMenu().
   */
  @Override
  public void loadFile(String filename) {
    // Ask for a filename (real path) from the user (in .dot format)
    System.out.println("Please enter the file path for the navigation data file:");
    System.out.println(
        "All files should be in a DOT format, and the file path must be given as well, I.E");
    System.out.println("/users/folder/example.dot");

    // If the user inputs a filename, read it in as a file and pass it to the backend
    File file = null;
    while (userInput.hasNext()) {
      try {
        String stringName = userInput.next();
        if (!(stringName.endsWith(".dot"))) {
          // If the filename does not end with .dot, return to main menu and prompt the user to try
          // again
          System.out.println("Please try again with a .dot formatted file");
          return;
        }

        // Pass in the fileName to the file object
        file = new File(stringName);
        // If the backend reads the data successfully, let the user know and return
        if (backend.readData(file)) {
          System.out.println("File read successfully.");
          return;
        } else {
          // If the backend is unnsuccessful, let the user know and return to mainMenu to try again
          System.out.println("File not read successfully. Please try again.");
          return;
        }
      } catch (Exception e) {
        System.out.println("File reading failed");
        return;
      }
    }
  }

  /**
   * Prints out the statistics about the navigation data set, including the number of buildings,
   * number of edges, and the total walking time.
   */
  @Override
  public void showDatasetStatistics() {
    // Save the returned ArrayList from the backend into an ArrayList object
    ArrayList<Double> pathData = backend.pathData();
    // Extract the number of nodes, edges, and total cost from this ArrayList
    Double numNodes = pathData.get(0);
    Double numEdges = pathData.get(1);
    Double totalCost = pathData.get(2);
    // Output the result and return to mainMenu
    System.out.println("Number of Buildings: " + numNodes);
    System.out.println("Number of edges: " + numEdges);
    System.out.println("Total Walking Time: " + totalCost);
    return;
  }

  /**
   * Prints out the instructions and menu asking the user for the start building and destination
   * building. Once the user has input data correctly, it prints out the shortest path from the
   * start building to the destination, including all buildings on the way, the estimated walk time
   * for each segment, and the total walk time.
   */
  @Override
  public void findShortestPath(String start, String destination) {
    System.out.println(
        "Please enter the start building name and end building name, separated by a comma (with no other commas or spacing)");
    System.out.println("For example: \"Van Vleck,Van Hise\"");
    String startBuilding = "";
    String endBuilding = "";
    while (userInput.hasNext()) {
      // Save the user input in a string object
      String buildings = userInput.nextLine();
      // If the string object does not contain a comman, prompt the user to try again and return to
      // main menu
      if (!buildings.contains(",")) {
        System.out.println("Please separate by commas and try again");
        return;
      }
      // Otherwise, split the string by the comma, and store each building in a string object
      String[] list = buildings.split(",");
      startBuilding = list[0];
      endBuilding = list[1];
      // Call the backend to find the shortest path between them
      ArrayList<String> pathData = null;
      ArrayList<Double> walkingData = null;
      try {
        pathData = backend.findShortestPath(startBuilding, endBuilding);
        walkingData = backend.getWalkingTimes(startBuilding, endBuilding);
      }
      catch (Exception e) {
        System.out.println("The building names were not found in the file. Try again");
        return;
      }
      // If the building names aren't found, the pathData returns null
      if (pathData == null) {
        System.out.println("The building names were not found in the file. Try again");
        return;
      }
      String[] pathDataSplit = pathData.get(0).split(",");
      System.out.println("The shortest path: ");
      // Print out the data in each element of the arrayLists
      for (int i = 0; i < pathDataSplit.length; i++) {
        if (i == 0) {
          System.out.println(pathDataSplit[i]);
        }
        else {
          System.out.println(pathDataSplit[i] + " Walk Time: " + walkingData.get(i-1));
        }
      }
      System.out.println(pathData.get(1));
      return;
    }
  }

  /**
   * Command to exit the app
   */
  @Override
  public void exitApp() {
    System.out.println("Thank you for using UW Path Finder");
    this.run = false;
    startApp();
  }
}
