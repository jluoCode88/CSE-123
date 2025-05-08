import java.util.*;

public class Client {
   private static final Random RAND = new Random();

   public static void main(String[] args) throws Exception {
        // List<Region> scenario = createRandomScenario(10, 10, 100, 1000, 100000);
      List<Region> scenario = createSimpleScenario();
      System.out.println(scenario);
        
      Path allocation = findPath(scenario);
      if (allocation != null) {
         printResult(allocation);
      } else {
         System.out.println("No valid path found. :-(");
      }
   }

   // Behavior: 
   //   - This method takes a list of Regions and returns the best path through them.
   //     The best path is the one that helps the most people,
   //     if there are multiple paths that
   //     result in the most people being helped, the best path is the one 
   //     that has the lowest cost.
   //     The path will start with the first region in sites.
   //     If sites is empty, the method will return null. If
   //     site is null, the method will throw an IllegalArgumentException
   // Parameter:
   //   - sites: List of Regions
   // Returns:
   //   - Path: the best path
   // Exceptions:
   //   - Throws an IllegalArgumentException if sites is null 
   public static Path findPath(List<Region> sites) {
      if (sites == null) {
         throw new IllegalArgumentException();
      }
      if (sites.isEmpty()) {
         return null;
      }
      Region start = sites.get(0);
      sites.remove(start); 
      return findPath(start, sites, new Path().extend(start), null);
   }
    
   // Behavior: 
   //   - This method takes a list of Regions and returns the best path through them.
   //     The method looks at the region we are currently in and explores every
   //     possible region we can reach from there,
   //     updating bestPath if our currPath is better then bestPath. 
   //     The best path is the one that helps the most people, if there are multiple paths that
   //     result in the most people being helped, the
   //     best path is the one that has the lowest cost.
   // Parameter:
   //   - current: current Region we are in
   //   - sites: List of Regions
   //   - currPath: Our path up to this point
   //   - bestPath: Best path we have found so far
   // Returns:
   //   - Path: the best path
   private static Path findPath(Region current, List<Region> sites, Path currPath, Path bestPath) {
      bestPath = updatesBestPath(currPath, bestPath);
      for (int i = 0; i < sites.size(); i++) {
         Region potentialNext = sites.remove(i);
         if (current.canReach(potentialNext)) {
            currPath = currPath.extend(potentialNext);
            bestPath = findPath(potentialNext, sites, currPath, bestPath);
            currPath = currPath.removeEnd();
         }
         sites.add(i, potentialNext);
      }
      return bestPath;
   }
   // Behavior: 
   //   - Checks if currPath is better then bestPath. 
   //     The best path is the one that helps the most people, if there are multiple paths that
   //     result in the most people being helped, the best path
   //     is the one that has the lowest cost.
   // Parameter:
   //   - currPath: Our current Path
   //   - bestPath: Best path we have found so far, Path we are comparing it to
   // Returns:
   //   - Path: the better path
   private static Path updatesBestPath(Path currPath, Path bestPath) {
      if (bestPath == null || currPath.totalPeople() > bestPath.totalPeople() || 
                (currPath.totalPeople() == bestPath.totalPeople() && 
                        currPath.totalCost() < bestPath.totalCost())) {
         bestPath = currPath;
      }
      return bestPath;
   }

    ///////////////////////////////////////////////////////////////////////////
    // PROVIDED HELPER METHODS - **DO NOT MODIFY ANYTHING BELOW THIS LINE!** //
    ///////////////////////////////////////////////////////////////////////////
    
    /**
    * Prints each path in the provided set. Useful for getting a quick overview
    * of all path currently in the system.
    * @param paths Set of paths to print
    */
   public static void printPaths(Set<Path> paths) {
      System.out.println("All Allocations:");
      for (Path a : paths) {
         System.out.println("  " + a);
      }
   }

    /**
    * Prints details about a specific path result, including the total people
    * helped and total cost.
    * @param path The path to print
    */
   public static void printResult(Path path) {
      System.out.println("Result: ");
      List<Region> regions = path.getRegions();
      System.out.print(regions.get(0).getName());
      for (int i = 1; i < regions.size(); i++) {
         System.out.print(" --($" + regions.get(i - 1).getCostTo(regions.get(i)) + ")-> " + regions.get(i).getName());
      }
      System.out.println();
      System.out.println("  People helped: " + path.totalPeople());
      System.out.printf("  Cost: $%.2f\n", path.totalCost());
   }

    /**
    * Creates a scenario with numRegions regions by randomly choosing the population 
    * and travel costs for each region.
    * @param numRegions Number of regions to create
    * @param minPop Minimum population per region
    * @param maxPop Maximum population per region
    * @param minCost Minimum cost of travel between regions
    * @param maxCost Maximum cost of travel between regions
    * @return A list of randomly generated regions
    */
   public static List<Region> createRandomScenario(int numRegions, int minPop, int maxPop,
                                                    double minCost, double maxCost) {
      List<Region> result = new ArrayList<>();
   
        // ranomly create regions
      for (int i = 0; i < numRegions; i++) {
            // int pop = RAND.nextInt(minPop, maxPop + 1);
         int pop = RAND.nextInt(maxPop - minPop + 1) + minPop;
         result.add(new Region("Region #" + i, pop));
      }
   
        // randomly create connections between regions
      for (int i = 0; i < numRegions; i++) {
            // int numConnections = RAND.nextInt(1, numLocs - i);
         Region site = result.get(i);
         for (int j = i + 1; j < numRegions; j++) {
                // flip a coin to decide whether or not to add each connection
            if (RAND.nextBoolean()) {
               Region other = result.get(j);
                    // double cost = round2(RAND.nextDouble(minCostPer, maxCostPer));
               double cost = round2(RAND.nextDouble() * (maxCost - minCost) + maxCost);
               site.addConnection(other, cost);
               other.addConnection(site, cost);
            }
         }
      }
   
      return result;
   }

    /**
    * Manually creates a simple list of regions to represent a known scenario.
    * @return A simple list of regions
    */
   public static List<Region> createSimpleScenario() {
      List<Region> sites = new ArrayList<>();
      Path expectedPath = new Path();
      Region regionOne = new Region("Region #1", 342);
      Region regionTwo = new Region("Region #2", 423);
      Region regionThree = new Region("Region #3", 523);
      Region regionFour = new Region("Region #4", 425);
      Region regionFive = new Region("Region #5", 275);
   
      regionOne.addConnection(regionTwo, 2473);
      regionOne.addConnection(regionThree, 5423);
      regionOne.addConnection(regionFour, 1275);
      regionOne.addConnection(regionFive, 967);
      regionTwo.addConnection(regionOne, 2473);
      regionTwo.addConnection(regionThree, 643);
      regionTwo.addConnection(regionFour, 1646);
      regionTwo.addConnection(regionFive, 3423);
      regionThree.addConnection(regionOne, 5423);
      regionThree.addConnection(regionTwo, 643);
      regionThree.addConnection(regionFour, 2342);
      regionThree.addConnection(regionFive, 543);
      regionFour.addConnection(regionOne, 1275);
      regionFour.addConnection(regionTwo, 1646);
      regionFour.addConnection(regionThree, 2342);
      regionFour.addConnection(regionFive, 2007);
      regionFive.addConnection(regionOne, 967);
      regionFive.addConnection(regionTwo, 3423);
      regionFive.addConnection(regionThree, 543);
      regionFive.addConnection(regionFour, 2007);
   
      sites.add(regionOne); 
      sites.add(regionTwo);  
      sites.add(regionThree); 
      sites.add(regionFour); 
      sites.add(regionFive);  
   
      return sites;
   }   

    /**
    * Rounds a number to two decimal places.
    * @param num The number to round
    * @return The number rounded to two decimal places
    */
   private static double round2(double num) {
      return Math.round(num * 100) / 100.0;
   }
}
