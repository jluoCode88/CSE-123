// Jessica Luo
// 2/18/2025
// CSE 123
// Creative Project 2: Mondrian Art
// TA: Benoit Lek
// This class can generate random artwork in the style of Mondrian.
// It has two methods, one for a Basic Mondrian and one for a Complex Mondrian, that 
// you can use to generate your art. Both take in a canvas, and fill it 
// with color rectangles, Basic Modrian colors the rectangles either red, cyan, yellow, or white 
// randomly, while Complex Mondrian colors the rectangles based on location, 
// where rectangles in the upper left half are more likely to be red and rectangles 
// in the lower right half are more likely to be yellow.
import java.util.*;
import java.awt.*;

public class Mondrian {
   private static final int MIN_CANVAS_DIMENSION = 300;
   private static final int MIN_SUBREGION_DIMENSION = 10;
   private static final Color[] REGULAR = new Color[]{Color.CYAN, Color.RED, Color.WHITE, 
         Color.YELLOW, Color.CYAN, Color.RED, Color.WHITE, Color.YELLOW};
   private static final Color[] MORE_YELLOW = new Color[]{Color.YELLOW, Color.YELLOW, Color.YELLOW,
         Color.YELLOW, Color.YELLOW, Color.RED, Color.WHITE, Color.CYAN};
   private static final Color[] MORE_RED = new Color[]{Color.RED, Color.RED, Color.RED, 
         Color.RED, Color.RED, Color.CYAN, Color.WHITE, Color.YELLOW};

  // Behavior: 
  //   -  Generates random artwork in the style of Mondrian by spliting
  //      pixels into rectangles and colors the rectangles either red, cyan, yellow, or white 
  // Parameter:
  //   - pixels: 2D array canvas we are working on
  // Exceptions:
  //   -  If the given pixels is null or the
  //      length or width of pixels is less than MIN_CANVAS_DIMENSION,
  //      IllegalArgumentException is thrown
  public void paintBasicMondrian(Color[][] pixels) {
      check(pixels);
      paintMondrian(pixels, 0, pixels.length - 1, 0, pixels[0].length - 1, true);
  }
     
  // Behavior: 
  //   -  Generates random artwork in the style of Mondrian by spliting
  //      pixels into rectangles and coloring the rectangles based on location, 
  //      where rectangles in the upper left half are more likely to be red and rectangles 
  //      in the lower right half are more likely to be yellow.
  // Parameter:
  //   - pixels: 2D array canvas we are working on
  // Exceptions:
  //   -  If the given pixels is null or the
  //      length or width of pixels is less than MIN_CANVAS_DIMENSION,
  //      IllegalArgumentException is thrown
  public void paintComplexMondrian(Color[][] pixels) {
      check(pixels);
      paintMondrian(pixels, 0, pixels.length - 1, 0, pixels[0].length - 1, false);
  }

  // Behavior: 
  //   -  splits the pixels into rectangles based on the following rule: 
  //      if the region being considered 
  //      is at least 1/4 the height of the full canvas and at least 1/4 the 
  //      width of the full canvas, 
  //      split it into four smaller regions by randomsly choosing one vertical and
  //      one horizontal dividing line.
  //      If the region being considered is at least 1/4 the height of the full canvas, split it
  //      it into two smaller regions by randomly choosing a horizontal dividing line. 
  //      If the region being considered is at least 1/4 the width of the full canvas, split
  //      it into two smaller regions by randomly choosing a vertical dividing.
  //      If the region being considered is smaller than 1/4 the height of the full canvas
  //      and smaller than 1/4 the width of the full canvas, do not split the region.
  //      In addition, all resulting subregions have dimensions of 
  //      at least MIN_SUBREGION_DIMENSION.
  //      Then it colors the rectangles randomly if its for a Basic Mondrian (either red, yellow,
  //      cyan, or white), or based on location if its for a Complex Mondrian 
  //      (rectangles in the upper left
  //      half are more likely to be red and rectangles in the lower right
  //      half are more likely to be yellow)
  // Parameter:
  //   - pixels: 2D array canvas we are working on
  //   - yStart: Starting y-coord of region we are working on
  //   - yEnd: Ending y-coord of region we are working on
  //   - xStart: Starting x-coord of region we are working on
  //   - xEnd: Ending x-coord of region we are working on
  //   - isBasic: true if its for a basic Modrian false if not
  private void paintMondrian(Color[][] pixels, int yStart, int yEnd, int xStart,
         int xEnd, boolean isBasic) {
      if (yEnd - yStart >= pixels.length / 4 && xEnd - xStart >= pixels[0].length / 4) {
         int randY = getValidRand(yStart, yEnd);
         int randX = getValidRand(xStart, xEnd);
         paintMondrian(pixels, yStart, randY, xStart, randX, isBasic);
         paintMondrian(pixels, randY, yEnd, xStart, randX, isBasic);
         paintMondrian(pixels, yStart, randY, randX, xEnd, isBasic);
         paintMondrian(pixels, randY, yEnd, randX, xEnd, isBasic);
      }
      else if (yEnd - yStart >= pixels.length / 4) {
         int randY = getValidRand(yStart, yEnd);
         paintMondrian(pixels, yStart, randY, xStart, xEnd, isBasic);
         paintMondrian(pixels, randY, yEnd, xStart, xEnd, isBasic);
      }
      else if (xEnd - xStart >= pixels[0].length / 4) {
         int randX = getValidRand(xStart, xEnd);
         while (randX - xStart < MIN_SUBREGION_DIMENSION || 
               xEnd - randX < MIN_SUBREGION_DIMENSION) {
            randX = xStart + (int)(Math.random() * (xEnd - xStart));
         }
         paintMondrian(pixels, yStart, yEnd, xStart, randX, isBasic);
         paintMondrian(pixels,yStart, yEnd, randX, xEnd, isBasic);
      }
      else {
         int randColor = (int)(Math.random() * 8);
         Color color;
         int middle = getMiddle(yStart, yEnd, xStart, xEnd);
         int divideLine = getMiddle(0, pixels.length - 1, 0, pixels[0].length - 1); 
         // all the pixels on the dividing diagonal line have the same sum
         if (isBasic || middle == divideLine) {
            color = REGULAR[randColor];
         }
         else if (middle > divideLine) {
            color = MORE_YELLOW[randColor];
         } 
         else {
            color = MORE_RED[randColor];
         }
         fill(pixels, yStart, yEnd, xStart, xEnd, color);
      }
   }
  
   // Behavior: 
   //   -  fills the region given by the x and y coordinates a given color
   // Parameter:
   //   - pixels: 2D array canvas we are working on
   //   - yStart: Starting y-coord of the region we are filling (exculsive)
   //   - yEnd: Ending y-coord of the region we are filling (exculsive)
   //   - xStart: Starting x-coord of the region we are filling (exculsive)
   //   - xEnd: Ending x-coord of the region we are filling (exculsive)
   private void fill(Color[][] pixels, int yStart, int yEnd,
         int xStart, int xEnd, Color color) {
      for(int i = yStart + 1; i < yEnd; i++) {
         for(int j = xStart + 1; j < xEnd; j++) {
            pixels[i][j] = color;
         }
      }
   }

   // Behavior: 
   //   - Finds the middle pixel and returns the sum of its x and y coords
   // Parameter:
   //   - yStart: Starting y-coord of region
   //   - yEnd: Ending y-coord of region
   //   - xStart: Starting x-coord of region
   //   - xEnd: Ending x-coord of region
   // Return:
   //   - int: the sum of the middle pixels x and y coords 
   private int getMiddle(int yStart, int yEnd, int xStart, int xEnd) {
      return ((xEnd + xStart)/2) + (yEnd + yStart/2);
   }

   // Behavior: 
   //   - Randomly chooses a number in between start and end 
   //     to split the rectangle that still leaves the resulting
   //     dimensions at least MIN_SUBREGION_DIMENSION pixels long
   // Parameter:
   //   - start: Starting coordinate
   //   - end: Ending coordinate
   // Return:
   //   - int: random number that meets requirements
   private int getValidRand(int start, int end) {
      int rand = -1;
      while (rand - start < MIN_SUBREGION_DIMENSION || end - rand < MIN_SUBREGION_DIMENSION) {
         rand = start + (int)(Math.random() * (end - start));
      }
      return rand;
   }

   // Behavior: 
   //   -  Throws an IllegalArgumentException if the given pixels is null or
   //      the length or width of pixels is less than MIN_CANVAS_DIMENSION
   // Parameter:
   //   - pixels: 2D array canvas we are checking
   // Exceptions:
   //   -  If the given pixels is null or the
   //      length or width of pixels is less than MIN_CANVAS_DIMENSION,
   //      IllegalArgumentException is thrown
   private void check(Color[][] pixels) {
      if (pixels == null || pixels.length < MIN_CANVAS_DIMENSION || 
            pixels[0].length < MIN_CANVAS_DIMENSION) {
         throw new IllegalArgumentException();
      }
   }
}
