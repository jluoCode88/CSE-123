import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class Testing {
    private Repository repo1;
    private Repository repo2;

    // Occurs before each of the individual test cases
    // (creates new repos and resets commit ids)
    @BeforeEach
    public void setUp() {
        repo1 = new Repository("repo1");
        repo2 = new Repository("repo2");
        Repository.Commit.resetIds();
    }

    @Test
    @DisplayName("Front case")
    public void testSynchronizeFrontCase() throws InterruptedException {
        commitAll(repo1, new String[]{"One", "Two"});
        commitAll(repo2, new String[]{"Three", "Four", "Five"});
        assertEquals(2, repo1.getRepoSize());
        assertEquals(3, repo2.getRepoSize());
        repo1.synchronize(repo2);
        assertEquals(5, repo1.getRepoSize());
        assertEquals(0, repo2.getRepoSize());
        testHistory(repo1, 5, new String[]{"One", "Two", "Three", "Four", "Five"});
    }

    @Test
    @DisplayName("Middle case")
    public void testSynchronizeMiddleCase() throws InterruptedException {
        repo1.commit("One");
        repo2.commit("Two");
        repo1.commit("Three");
        repo2.commit("Four");
        repo1.commit("Five");
        assertEquals(3, repo1.getRepoSize());
        assertEquals(2, repo2.getRepoSize());
        repo1.synchronize(repo2);
        assertEquals(5, repo1.getRepoSize());
        assertEquals(0, repo2.getRepoSize());
        testHistory(repo1, 5, new String[]{"One", "Two", "Three", "Four", "Five"});
    }
    @Test
    @DisplayName("End case")
    public void testSynchronizeEndCase() throws InterruptedException {
        commitAll(repo1, new String[]{"One", "Two", "Three"});
        commitAll(repo2, new String[]{"Four", "Five"});
        assertEquals(3, repo1.getRepoSize());
        assertEquals(2, repo2.getRepoSize());
        repo2.synchronize(repo1);
        assertEquals(0, repo1.getRepoSize());
        assertEquals(5, repo2.getRepoSize());
        testHistory(repo2, 5, new String[]{"One", "Two", "Three", "Four", "Five"});
    }

    @Test
    @DisplayName("Empty case")
    public void testSynchronizeEmptyCase() throws InterruptedException {
        // Tests when this Repository is empty
        commitAll(repo1, new String[]{});
        commitAll(repo2, new String[]{"One", "Two", "Three", "Four"});
        assertEquals(0, repo1.getRepoSize());
        assertEquals(4, repo2.getRepoSize());
        repo1.synchronize(repo2);
        assertEquals(4, repo1.getRepoSize());
        assertEquals(0, repo2.getRepoSize());
        testHistory(repo1, 4, new String[]{"One", "Two", "Three", "Four"});
        // Tests when other Repository is empty
        repo1.synchronize(repo2);
        assertEquals(4, repo1.getRepoSize());
        assertEquals(0, repo2.getRepoSize());
        testHistory(repo1, 4, new String[]{"One", "Two", "Three", "Four"});
    }
    
    /////////////////////////////////////////////////////////////////////////////////
    // PROVIDED HELPER METHODS (You don't have to use these if you don't want to!) //
    /////////////////////////////////////////////////////////////////////////////////

    // Commits all of the provided messages into the provided repo, making sure timestamps
    // are correctly sequential (no ties). If used, make sure to include
    //      'throws InterruptedException'
    // much like we do with 'throws FileNotFoundException'. 
    // repo and messages should be non-null.
    // Example useage:
    //
    // repo1:
    //      head -> null
    // To commit the messages "one", "two", "three", "four"
    //      commitAll(repo1, new String[]{"one", "two", "three", "four"})
    // This results in the following after picture
    // repo1:
    //      head -> "four" -> "three" -> "two" -> "one" -> null
    //
    // YOU DO NOT NEED TO UNDERSTAND HOW THIS METHOD WORKS TO USE IT! (this is why documentation
    // is important!)
    private void commitAll(Repository repo, String[] messages) throws InterruptedException {
        // Commit all of the provided messages
        for (String message : messages) {
            int size = repo.getRepoSize();
            repo.commit(message);
            
            // Make sure exactly one commit was added to the repo
            assertEquals(size + 1, repo.getRepoSize(),
                         String.format("Size not correctly updated after commiting message [%s]",
                                       message));

            // Sleep to guarantee that all commits have different time stamps
            Thread.sleep(2);
        }
    }

    // Makes sure the given repositories history is correct up to 'n' commits, checking against
    // all commits made in order. repo and allCommits should be non-null.
    // Example useage:
    //
    // repo1:
    //      head -> "four" -> "three" -> "two" -> "one" -> null
    //      (Commits made in the order ["one", "two", "three", "four"])
    // To test the getHistory() method up to n=3 commits this can be done with:
    //      testHistory(repo1, 3, new String[]{"one", "two", "three", "four"})
    // Similarly, to test getHistory() up to n=4 commits you'd use:
    //      testHistory(repo1, 4, new String[]{"one", "two", "three", "four"})
    //
    // YOU DO NOT NEED TO UNDERSTAND HOW THIS METHOD WORKS TO USE IT! (this is why documentation
    // is important!)
    private void testHistory(Repository repo, int n, String[] allCommits) {
        int totalCommits = repo.getRepoSize();
        assertTrue(n <= totalCommits,
                   String.format("Provided n [%d] too big. Only [%d] commits",
                                 n, totalCommits));
        
        String[] nCommits = repo.getHistory(n).split("\n");
        
        assertTrue(nCommits.length <= n,
                   String.format("getHistory(n) returned more than n [%d] commits", n));
        assertTrue(nCommits.length <= allCommits.length,
                   String.format("Not enough expected commits to check against. " +
                                 "Expected at least [%d]. Actual [%d]",
                                 n, allCommits.length));
        
        for (int i = 0; i < n; i++) {
            String commit = nCommits[i];

            // Old commit messages/ids are on the left and the more recent commit messages/ids are
            // on the right so need to traverse from right to left
            int backwardsIndex = totalCommits - 1 - i;
            String commitMessage = allCommits[backwardsIndex];

            assertTrue(commit.contains(commitMessage),
                       String.format("Commit [%s] doesn't contain expected message [%s]",
                                     commit, commitMessage));
            assertTrue(commit.contains("" + backwardsIndex),
                       String.format("Commit [%s] doesn't contain expected id [%d]",
                                     commit, backwardsIndex));
        }
    }
}
