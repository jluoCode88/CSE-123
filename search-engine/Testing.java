import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class Testing {

    @Test
    @DisplayName("Book string, list constructor")
    public void testBookStringList() {
        Book book = new Book("Title", List.of("Author 1", "Author 2"), new Scanner("Content"));

        // Change the following to test that getTitle returns "Title"
        assertEquals("Title", book.getTitle());

        // Change the following to test that getArtists returns a list
        // containing "Author"
        assertEquals(List.of("Author 1", "Author 2"), book.getArtists());

        // Change the following to test that toString returns the correctly
        // String representation
        assertEquals("Title by [Author 1, Author 2]", book.toString());

        // Change the following to test that getContent returns a list containing "Content"
        assertEquals(List.of("Content"), book.getContent());
    }

    @Test
    @DisplayName("getNumRatings")
    public void testNumRatings() {
        Book book = new Book("Title", List.of("Author"), new Scanner("Content"));
        
        assertEquals(0, book.getNumRatings());

        book.addRating(1);
        // TODO: Test that getNumRatings returns 1
        assertEquals(1, book.getNumRatings());

        book.addRating(1);
        // TODO: Test that getNumRatings returns 2
        assertEquals(2, book.getNumRatings());
        
    }

    @Test
    @DisplayName("getAvgRating")
    public void testAvgRatings() {
        Book book = new Book("Title", List.of("Author"), new Scanner("Content"));
        // TODO: Test that getAvgRating returns 0
        assertEquals(0, book.getAverageRating());
        

        book.addRating(4);
        // TODO: Test that getAverageRating returns 4
        assertEquals(4, book.getAverageRating());


        book.addRating(5);
        // TODO: Test that getAvgRating returns 4.5
        assertEquals(4.5, book.getAverageRating());

        
    }

    @Test
    @DisplayName("createIndex tests")
    public void testInvertedIndex() {
        Book mistborn = new Book("Mistborn", List.of("Brandon Sanderson"),
                                 new Scanner("Epic fantasy worldbuildling content"));
        Book farenheit = new Book("Farenheit 451", List.of("Ray Bradbury"),
                                  new Scanner("Realistic \"sci-fi\" content"));
        Book hobbit = new Book("The Hobbit", List.of("J.R.R. Tolkein"),
                               new Scanner("Epic fantasy quest content"));
        
        List<Media> books = List.of(mistborn, farenheit, hobbit);
        Map<String, Set<Media>> index = SearchClient.createIndex(books);
        
        // Finish the following statements by using assertTrue/assertFalse to test that the 
        // quotes around sci-fi aren't ignored. Then uncomment it!
        assertFalse(index.containsKey("sci-fi"));
        assertTrue(index.containsKey("\"sci-fi\""));

        // Change the following line such that following test passes
        // (what original books include 'fantasy'?)
        Set<Book> expected = Set.of(mistborn, hobbit);
        assertEquals(expected, index.get("fantasy"));
    }
}
