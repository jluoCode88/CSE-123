// Jessica Luo
// 1/22/2025
// CSE 123
// Programming Assignment 0: Ciphers
// TA: Benoit Lek
// This class represents an encryption scheme where the encoding 
// is given by placing a key at the front, with the rest of the encodable range, minus
// the characters in the key, following normally.
// This class has access to methods to set the encoding, as well
// as to encrypt and decrypt. 
// This class extends Substitution.
import java.util.*;

public class CaesarKey extends Substitution {
       
   // Behavior: 
   //   - Creates a new CaesarKey based on the given key
   // Parameters:
   //   - key: key we are placing at the front of encoding
   // Exceptions: 
   //   - If key is null, empty, contains a duplicate character, or contains
   //     a character that falls outside the encodable range,
   //     IllegalArgumentException will be thrown
   public CaesarKey(String key) {
      super();
      if(key == null || key.isEmpty()) {
         throw new IllegalArgumentException();
      }
      Set<Character> check = new HashSet<>();
      for (int i = 0; i < key.length(); i++) {
         char c = key.charAt(i);
         if (c < Cipher.MIN_CHAR || c > Cipher.MAX_CHAR) {
            throw new IllegalArgumentException();
         }
         check.add(c);
      }
      if (check.size() != key.length()) {
         throw new IllegalArgumentException();
      }
      List<Character> order = new ArrayList<>();
      for (int i = MIN_CHAR; i <= MAX_CHAR; i++) {
         char character = (char)i;
         order.add(character);
      }
      String s = "";
      for (int i = 0; i < key.length(); i++) {
         int location = order.indexOf(key.charAt(i));
         s = s + order.remove(location);
      }
      int size = order.size();
      for (int i = 0; i < size; i++) {
         s = s + order.remove(0);
      }
      super.setEncoding(s);
   }
}
   

