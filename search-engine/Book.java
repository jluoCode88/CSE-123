// Jessica Luo
// 1/13/2025
// CSE 123
// Creative Project 0: Search Engine
// TA: Benoit Lek
// This class represents a book that stores the title, authors, ratings, and content.
// This class provides methods to get information about the book, including 
// number of ratings and the books average rating, as well as methods
// to add a rating, get a String representation of the book,
// and compare this book to another book. 
// This class implements Media interface and Comparable<Book> interface.

import java.util.*;

public class Book implements Media, Comparable<Book> {
   private String title;
   private List<String> authors;
   private List<Integer> ratings;
   private List<String> contentList;

   // Behavior: 
   //   - Creates a new Book based on the given title, 
   //     authors, and content
   // Parameters:
   //   - title: the title of the book
   //   - authors: authors of the book
   //   - content: a Scanner containing the contents of the book
   public Book(String title, List<String> authors, Scanner content) {
      this.title = title;
      this.authors = authors;
      this.ratings = new ArrayList<>();
      contentList = new ArrayList<>();
      while (content.hasNext()) {
         contentList.add(content.next());
      }
   }

   // Behavior: 
   //   - This method returns the title
   // Returns:
   //   - String: the title
   public String getTitle() {
      return title;
   }

   // Behavior: 
   //   - This method returns the authors
   // Returns:
   //   - List<String>: list of authors
   public List<String> getArtists() {
      return authors;
   }

   // Behavior: 
   //   - This method adds a given rating to the book
   // Parameters:
   //   - score: the rating we are adding
   public void addRating(int score) {
      ratings.add(score);
   }

   // Behavior: 
   //   - This method returns the number of ratings
   // Returns:
   //   - int: the number of ratings
   public int getNumRatings() {
      return ratings.size();
   }

   // Behavior: 
   //   - This method returns the book's average rating
   // Returns:
   //   - double: the average rating,   
   //     if this book has no ratings, returns 0.
   public double getAverageRating() {
      double average = 0.0;
      if (!ratings.isEmpty()) {
         for (Integer rating : ratings) {
            average += rating;
         }
         average = average / this.getNumRatings();
      }
      return average;
   }

   // Behavior: 
   //   - This method returns the book's content
   // Returns:
   //   - List<String>: the book's content as a list
   public List<String> getContent() {
      return contentList;
   }

   // Behavior: 
   //   - This method returns the String representation of the book,
   //     if this book has no ratings, the String returned will not 
   //     include rating information
   // Returns:
   //   - String: the String representation of the book
   public String toString() {
      String result = title + " by " + authors;
      if(!ratings.isEmpty()) {
         double rounded = Math.round(this.getAverageRating() * 100.0) / 100.0;
         result = result + ": " + rounded + " (" + this.getNumRatings() + " ratings)";
      }
      return result;
   }

   // Behavior: 
   //   - This method compares this book to the other book 
   //     based on the two books average rating, it returns 
   //     a positive, zero, or negative number based on which book is better.
   // Parameters:
   //   - other: book we are comparing against
   // Returns:
   //   - int: -1 if this book is better than other book, 0
   //   if the books are equal, and 1 if this book is worse then other book
   public int compareTo(Book other) {
      if (this.getAverageRating() > other.getAverageRating()) {
         return -1;
      } else if (this.getAverageRating() == other.getAverageRating()) {
         return 0;
      } else {
         return 1;
      }
   }
}
