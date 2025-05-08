// Jessica Luo
// 2/10/2025
// CSE 123
// Programming Assignment 1: Mini-Git
// TA: Benoit Lek
// This class represents a Repository, it stores the name of the Repository and 
// and the head Commit, which is the most recent one. 
// This class provides methods to get the size of the Repository
// as well as the ID of the head Repository, it also provides methods to 
// get a String representation of the Repository,
// check whether it contains a commit with the given ID, get the most recent 
// Commits(number specified by user), add a Commit, remove a commit with the given ID,
// and synchronize two Repositories.
import java.util.*;
import java.text.SimpleDateFormat;

public class Repository {
   private Commit head;
   private String name;

  // Behavior: 
  //   - Creates a new empty Repository with the given name
  // Parameter:
  //   - name: name we are giving the Repository
  // Exceptions:
  //   - Throws an IllegalArgumentException if name is null or empty
   public Repository(String name) {
      if(name == null || name.isEmpty()) {
         throw new IllegalArgumentException();
      }
      this.name = name;
   }

   // Behavior: 
   //   - returns the ID of the head Commit
   // Returns:
   //   - String: ID of the head Commit, 
   //     returns null if the head repository is null
   public String getRepoHead() {
      if (head != null) {
         return head.id;
      }
      return null;
   }

   // Behavior: 
   //   - returns the number of Commits in the repository
   // Returns:
   //   - int: number of Commits
   public int getRepoSize() {
      int size = 0;
      Commit curr = head;
      while (curr != null) {
         size++;
         curr = curr.past;
      }
      return size;
   }
            
   // Behavior: 
   //   - Returns a String representation of the Repository in the
   //     format <name> - Current head: <head>,
   //     if there are no Commits, returns it in the format <name> - No commits
   // Returns:
   //   - String: The String representation
   public String toString() {
      String result = "";
      result += name + " - ";
      if (head == null) {
         result += "No commits"; 
      }
      else {
         result += "Current head: " + head.toString();   
      }
      return result;
   }

   // Behavior: 
   //   - Return whether a Commit with ID targetId is in the repository or not.
   // Parameter:
   //   - targetId: ID we are looking for
   // Returns:
   //   - boolean: true if a Commit with ID targetId is in the Repository, false if not.
   // Exceptions:
   //   - Throws an IllegalArgumentException if targetId is null
   public boolean contains(String targetId) {
      if (targetId == null) {
         throw new IllegalArgumentException();
      }
      Commit curr = head;
      while (curr != null) {
         if (curr.id.equals(targetId)) {
            return true;
         }
         curr = curr.past;
      }
      return false;
   }

   // Behavior: 
   //   - Return a String representation of the most recent n commits in the 
   //     Repository (with the most recent one first),
   //     if n is greater than the size of the Repository, it will return them all.
   // Parameter:
   //   - n: the number of Commits
   // Returns:
   //   - String : a string consisting of the
   //     String representations of the most recent n commits
   // Exceptions:
   //   - Throws an IllegalArgumentException if n is non-positive
   public String getHistory(int n) {
      if (n <= 0) {
         throw new IllegalArgumentException();
      }
      String result = "";
      int counter = 0;
      Commit curr = head;
      while (curr != null && counter < n) {
         result += curr.toString() + "\n";
         curr = curr.past;
         counter++;
      }
      return result;
   }

   // Behavior: 
   //   - Creates a new Commit with the given message and 
   //     adds it to the front of this Repository, preserving the history behind it
   // Parameter:
   //   - message: the message we are giving the Commit
   // Returns:
   //   - String: ID of the new Commit
   // Exceptions:
   //   - Throws an IllegalArgumentException if message is null
   public String commit(String message) {
      if (message == null) {
         throw new IllegalArgumentException();
      }
      head = new Commit(message, head);
      return head.id;
   }

   // Behavior: 
   //   - Removes the Commit with the given 
   //     targetId(if present), maintaining the rest of the history
   // Parameter:
   //   - targetId: ID of the Commit we are removing
   // Returns:
   //   - boolean: true if the Commit with ID targetId was successfully 
   //     dropped, false if not(there was no Commit with a matching ID)
   // Exceptions:
   //   - Throws an IllegalArgumentException if targetId is null
   public boolean drop(String targetId) {
       if(this.contains((targetId))) {
         if(this.head.id.equals(targetId)) {
            this.head = this.head.past;
            return true;
         } else {
            Commit curr = head;
            while (curr.past != null) {
               if(curr.past.id.equals(targetId)) {
                  curr.past = curr.past.past;
                  return true;
               }
               curr = curr.past;
            }
         }
      }
   return false;
   }
            
   // Behavior: 
   //   - Takes all the commits in the other Repository and moves them into this repository, 
   //     combining the histories of the two repositories such that chronological 
   //     order is preserved(Commits are ordered from most recent to least recent).
   //     If other is empty, this repository will remain unchanged. 
   //     If this repository is empty, all commits in other
   //     will still be moved into this repository.
   // Parameter:
   //   - other: the Repository we are moving Commits from
   // Exceptions:
   //   - Throws an IllegalArgumentException if other is null
   public void synchronize(Repository other) {
      if (other == null) {
         throw new IllegalArgumentException();
      }
      if (head == null) {
         head = other.head;
         other.head = null;
      } else {
         while(other.head != null && other.head.timeStamp > head.timeStamp) {
            Commit temp = other.head;
            other.head = other.head.past;
            temp.past = head;
            head = temp;
         }
         if (head != null) {
            Commit curr = head;
            while (curr.past != null && other.head != null) {
               if(other.head.timeStamp > curr.past.timeStamp) {
                  Commit temp = other.head;
                  other.head = other.head.past;
                  temp.past = curr.past;
                  curr.past = temp;
               }
               curr = curr.past;
            }
            if (curr.past == null) {
               curr.past = other.head;
               other.head = null;
            }
         }
      }
   }

    /**
     * DO NOT MODIFY
     * A class that represents a single commit in the repository.
     * Commits are characterized by an identifier, a commit message,
     * and the time that the commit was made. A commit also stores
     * a reference to the immediately previous commit if it exists.
     *
     * Staff Note: You may notice that the comments in this 
     * class openly mention the fields of the class. This is fine 
     * because the fields of the Commit class are public. In general, 
     * be careful about revealing implementation details!
     */
    public static class Commit {

        private static int currentCommitID;

        /**
         * The time, in milliseconds, at which this commit was created.
         */
        public final long timeStamp;

        /**
         * A unique identifier for this commit.
         */
        public final String id;

        /**
         * A message describing the changes made in this commit.
         */
        public final String message;

        /**
         * A reference to the previous commit, if it exists. Otherwise, null.
         */
        public Commit past;

        /**
         * Constructs a commit object. The unique identifier and timestamp
         * are automatically generated.
         * @param message A message describing the changes made in this commit. Should be non-null.
         * @param past A reference to the commit made immediately before this
         *             commit.
         */
        public Commit(String message, Commit past) {
            this.id = "" + currentCommitID++;
            this.message = message;
            this.timeStamp = System.currentTimeMillis();
            this.past = past;
        }

        /**
         * Constructs a commit object with no previous commit. The unique
         * identifier and timestamp are automatically generated.
         * @param message A message describing the changes made in this commit. Should be non-null.
         */
        public Commit(String message) {
            this(message, null);
        }

        /**
         * Returns a string representation of this commit. The string
         * representation consists of this commit's unique identifier,
         * timestamp, and message, in the following form:
         *      "[identifier] at [timestamp]: [message]"
         * @return The string representation of this collection.
         */
        @Override
        public String toString() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(timeStamp);

            return id + " at " + formatter.format(date) + ": " + message;
        }

        /**
        * Resets the IDs of the commit nodes such that they reset to 0.
        * Primarily for testing purposes.
        */
        public static void resetIds() {
            Commit.currentCommitID = 0;
        }
    }
}
