/////// Jessica Luo
// 1/13/2025
// CSE 123
// Creative Project 0: Search Engine
// TA: Benoit Le

import java.io.*;
import java.util.*;

// This class allows users to find and rate books within BOOK_DIRECTORY
// containing certain terms
public class SearchClient {
    public static final String BOOK_DIRECTORY = "./books";
    private static final Random RAND = new Random();

    // Some class constants you can play around with to give random ratings to the uploaded books!
    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;
    public static final int MIN_NUM_RATINGS = 1;
    public static final int MAX_NUM_RATINGS = 100;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        List<Media> media = new ArrayList<>(loadBooks());

        Map<String, Set<Media>> index = createIndex(media);

        System.out.println("Welcome to the CSE 123 Search Engine!");
        String command = "";
        while (!command.equalsIgnoreCase("quit")) {
            System.out.println("What would you like to do? [Search, Rate, Quit]");
            System.out.print("> ");
            command = console.nextLine();

            if (command.equalsIgnoreCase("search")) {
                searchQuery(console, media, index);
            } else if (command.equalsIgnoreCase("rate")) {
                addRating(console, media);
            } else if (!command.equalsIgnoreCase("quit")) {
                System.out.println("Invalid command, please try again.");
            }
        }
        System.out.println("See you next time!");
    }

   // Behavior: 
   //   - This method maps the content in each media 
   //     to all the media that contains that piece of content,
   //     this method considers each piece of content case-insensitively 
   // Parameters:
   //   - documents: List of media
   // Returns:
   //   - Map<String, Set<Media>>: maps each piece of content 
   //     to all the media that contain it, the pieces of 
   //     content are sorted alphabetically
    public static Map<String, Set<Media>> createIndex(List<Media> documents) {
      Map<String, Set<Media>> indexMap = new TreeMap<>();
      for(Media curr : documents) {
         for (String word : curr.getContent()) {
            word = word.toLowerCase();
            if (!indexMap.containsKey(word)) {
               indexMap.put(word, new HashSet<Media>());
            }
            indexMap.get(word).add(curr);
         }
      }
      return indexMap;
    }

   // Behavior: 
   //   - This method compiles all the media whose
   //     content contains at least one word of the query
   // Parameters:
   //   - index: maps each piece of content to all the media that contain it
   //   - query: the user's query
   // Returns:
   //   - Set<Media>: TreeSet of all the media that is related to the query
    public static Set<Media> search(Map<String, Set<Media>> index, String query) {
        Set<Media> results = new TreeSet<>();
        Scanner line = new Scanner(query);
        while(line.hasNext()) {
            String word = line.next();
            if(index.containsKey(word)) {
                results.addAll(index.get(word));
            }
        }
        return results;
    }
    
    // Allows the user to search a specific query using the provided 'index' to find appropraite
    //  Media entries.
    //
    // Parameters:
    //   console - the Scanner to get user input from. Should be non-null
    //   index - an inverted index mapping terms to the Set of media containing those terms.
    //           Should be non-null
    public static void searchQuery(Scanner console, List<Media> documents, 
                                Map<String, Set<Media>> index) {
        System.out.println("Enter query:");
        System.out.print("> ");
        String query = console.nextLine();

        Set<Media> result = search(index, query);
        
        if (result.isEmpty()) {
            System.out.println("\tNo results!");
        } else {
            for (Media m : result) {
                System.out.println("\t" + m.toString());
            }
        }
    }

    // Allows the user to add a rating to one of the options wthin 'media'
    //
    // Parameters:
    //   console - the Scanner to get user input from. Should be non-null.
    //   media - list of all media options loaded into the search engine. Should be non-null.
    public static void addRating(Scanner console, List<Media> media) {
        for (int i = 0; i < media.size(); i++) {
            System.out.println("\t" + i + ": " + media.get(i).toString());
        }
        System.out.println("What would you like to rate (enter index)?");
        System.out.print("> ");
        int choice = Integer.parseInt(console.nextLine());
        if (choice < 0 || choice >= media.size()) {
            System.out.println("Invalid choice");
        } else {
            System.out.println("Rating [" + media.get(choice).getTitle() + "]");
            System.out.println("What rating would you give?");
            System.out.print("> ");
            int rating = Integer.parseInt(console.nextLine());
            media.get(choice).addRating(rating);
        }
    }

    // Loads all books from BOOK_DIRECTORY. Assumes that each book starts with two lines -
    //      "Title: " which is followed by the book's title
    //      "Author: " which is followed by the book's author
    //
    // Returns:
    //   A list of all book objects corresponding to the ones located in BOOK_DIRECTORY
    public static List<Media> loadBooks() throws FileNotFoundException {
        List<Media> ret = new ArrayList<>();
        
        File dir = new File(BOOK_DIRECTORY);
        for (File f : dir.listFiles()) {
            Scanner sc = new Scanner(f, "utf-8");
            String title = sc.nextLine().substring("Title: ".length());
            List<String> author = List.of(sc.nextLine().substring("Author: ".length()));

            Media book = new Book(title, author, sc);

            // Adds random ratings to 'book' based on the class constants. 
            // Feel free to comment this out.
            int minRating = RAND.nextInt(MIN_RATING, MAX_RATING + 1);
            addRatings(minRating, Math.min(MAX_RATING, RAND.nextInt(minRating, MAX_RATING + 1)),
                        RAND.nextInt(MIN_NUM_RATINGS, MAX_NUM_RATINGS), book);
            ret.add(book);
        }

        return ret;
    }

    // Adds ratings to the provided media numRatings amount of times. Each rating is a random int
    // between minRating and maxRating (inclusive).
    private static void addRatings(int minRating, int maxRating, int numRatings, Media media) {
        for (int i = 0; i < numRatings; i++) {
            media.addRating(RAND.nextInt(minRating, maxRating + 1));
        }
    }
}
