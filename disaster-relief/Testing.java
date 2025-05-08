import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class Testing {

   @Test
    @DisplayName("STUDENT TEST - Case #1")
    public void firstCaseTest() {
      List<Region> sites = new ArrayList<>();
      Path expectedPath = new Path();
      
      Region regionOne = new Region("Region #1", 500);
      Region regionTwo = new Region("Region #2", 700);
      Region regionThree = new Region("Region #3", 900);
      Region regionFour = new Region("Region #4", 400);
      Region regionFive = new Region("Region #5", 300);
      Region regionSix = new Region("Region #6", 800);
   
      regionOne.addConnection(regionTwo, 2000);
      regionOne.addConnection(regionFour, 1500);
      regionOne.addConnection(regionFive, 1800);
      regionTwo.addConnection(regionOne, 2000);
      regionTwo.addConnection(regionThree, 1500);
      regionTwo.addConnection(regionFour, 500);
      regionTwo.addConnection(regionFive, 700);
      regionThree.addConnection(regionTwo, 1500);
      regionFour.addConnection(regionOne, 1500);
      regionFour.addConnection(regionTwo, 500);
      regionFour.addConnection(regionFive, 1400);
      regionFour.addConnection(regionSix, 200);
      regionFive.addConnection(regionOne, 1800);
      regionFive.addConnection(regionTwo, 700);
      regionFive.addConnection(regionFour, 1400);
      regionSix.addConnection(regionFour, 200);
   
      sites.add(regionOne); 
      sites.add(regionTwo);  
      sites.add(regionThree); 
      sites.add(regionFour); 
      sites.add(regionFive);  
      sites.add(regionSix); 
   
      expectedPath = expectedPath.extend(regionOne);
      expectedPath = expectedPath.extend(regionFour);
      expectedPath = expectedPath.extend(regionFive);
      expectedPath = expectedPath.extend(regionTwo);
      expectedPath = expectedPath.extend(regionThree);
   
      Path actualPath = Client.findPath(sites);
   
      assertEquals(expectedPath, actualPath);
   
   }

   @Test
    @DisplayName("STUDENT TEST - Case #2")
    public void secondCaseTest() {
      List<Region> sites = new ArrayList<>();
      Path expectedPath = new Path();

      Region regionOne = new Region("Region #1", 1200);
      Region regionTwo = new Region("Region #2", 9000);
      Region regionThree = new Region("Region #3", 4500);
      Region regionFour = new Region("Region #4", 4600);
      Region regionFive = new Region("Region #5", 1300);
      Region regionSix = new Region("Region #6", 7800);
      Region regionSeven = new Region("Region #7", 2400);
   
      regionOne.addConnection(regionTwo, 2900);
      regionOne.addConnection(regionFour, 2400);
      regionTwo.addConnection(regionOne, 2900);
      regionTwo.addConnection(regionThree, 1600);
      regionTwo.addConnection(regionFour, 3100);
      regionTwo.addConnection(regionFive, 700);
      regionThree.addConnection(regionTwo, 1600);
      regionThree.addConnection(regionFive, 900);
      regionFour.addConnection(regionOne, 2400);
      regionFour.addConnection(regionTwo, 1300);
      regionFour.addConnection(regionSix, 1700);
      regionFour.addConnection(regionSeven, 1200);
      regionFive.addConnection(regionTwo, 3100);
      regionFive.addConnection(regionThree, 900);
      regionSix.addConnection(regionFour, 1700);
      regionSix.addConnection(regionSeven, 600);
      regionSeven.addConnection(regionFour, 1200);
      regionSeven.addConnection(regionSix, 600);
   
      sites.add(regionOne); 
      sites.add(regionTwo);  
      sites.add(regionThree); 
      sites.add(regionFour); 
      sites.add(regionFive);  
      sites.add(regionSix); 
      sites.add(regionSeven); 
   
      expectedPath = expectedPath.extend(regionOne);
      expectedPath = expectedPath.extend(regionTwo);
      expectedPath = expectedPath.extend(regionFour);
      expectedPath = expectedPath.extend(regionSeven);
      expectedPath = expectedPath.extend(regionSix);
   
      Path actualPath = Client.findPath(sites);
   
      assertEquals(expectedPath, actualPath);
   }

   @Test
    @DisplayName("STUDENT TEST - DIY")
    public void diyTest() {
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
   
      expectedPath = expectedPath.extend(regionOne);
      expectedPath = expectedPath.extend(regionFive);
      expectedPath = expectedPath.extend(regionThree);
      expectedPath = expectedPath.extend(regionTwo);
      expectedPath = expectedPath.extend(regionFour);
   
   
      Path actualPath = Client.findPath(sites);
   
      assertEquals(expectedPath, actualPath);
   }
}
