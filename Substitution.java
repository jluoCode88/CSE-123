// Jessica Luo
// 1/22/2025
// CSE 123
// Programming Assignment 0: Ciphers
// TA: Benoit Lek
// This class represents an encryption algorithm, where each input 
// character is assigned to a unique output character using encoding. 
// This class provides methods to set the encoding, as well
// as to encrypt and decrypt. 
// This class extends Cipher.
import java.util.*;


public class Substitution extends Cipher {
   private String encoding;

   // Behavior: 
   //   - Creates a new Substitution based on the given encoding
   // Parameters:
   //   - encoding: shows the output character 
   //     corresponding to whatever input character 
   //     is in the same relative position within the encodable range
   // Exceptions: 
   //   - If encoding is null, its length doesn't match the number
   //     of characters within the encodable range, 
   //     contains a duplicate character, or contains a 
   //     character that falls outside the encodable range,
   //     IllegalArgumentException will be thrown
   public Substitution(String encoding) {
      setEncoding(encoding);
   }
   
   // Behavior: 
   //   - Creates a new Substitution with an empty encoding.
   public Substitution() {
      this.encoding = "";
   }

   // Behavior: 
   //   - Updates the encoding to the provided encoding
   // Parameters:
   //   - encoding: shows the output character 
   //     corresponding to whatever input character 
   //     is in the same relative position within the encodable range
   // Exceptions: 
   //   - If encoding is null, its length doesn't match the number
   //     of characters within the encodable range, 
   //     contains a duplicate character, or contains a 
   //     character that falls outside the encodable range,
   //     IllegalArgumentException will be thrown
   public void setEncoding(String encoding) {
      if(!this.isValid(encoding)) {
         throw new IllegalArgumentException();
      }
      this.encoding = encoding;
   }

   // Behavior: 
   //   -  Applies this Cipher's encryption scheme to input
   // Parameters:
   //   - input: the string to be encrypted. Should not be null and 
   //     all its characters should be within the encodable range.
   // Returns: 
    //   - String: the result after encrypting
   // Exceptions: 
   //   - If encoding is empty, 
   //     IllegalStateException will be thrown
   //   - If input is null, 
   //     IllegalArgumentException will be thrown
   public String encrypt(String input) {
      if(encoding.isEmpty()) {
         throw new IllegalStateException();
      }
      if (input == null) {
         throw new IllegalArgumentException();
      }
      String encrypted = "";
      for (int i = 0; i < input.length(); i++) {
         char c = input.charAt(i);
         int index = (int)c;
         index = index - MIN_CHAR;
         encrypted += encoding.charAt(index);
      }
      return encrypted;
   }
    
   // Behavior: 
   //   -  Applies the inverse of this Cipher's encryption scheme to input
   // Parameters:
   //   - input: the string to be decrypted. Should not be null and 
   //     all its characters should be within the encodable range.
   // Returns: 
    //   - String: the result after decrypting
   // Exceptions: 
   //   - If encoding is empty, 
   //     IllegalStateException will be thrown
   //   - If input is null, 
   //     IllegalArgumentException will be thrown
   public String decrypt(String input){
      if(encoding.isEmpty()) {
         throw new IllegalStateException();
      }
      if (input == null) {
         throw new IllegalArgumentException();
      }
      String decrypted = "";
      for (int i = 0; i < input.length(); i++) {
         char c = input.charAt(i);
         int index = encoding.indexOf(c);
         index = index + MIN_CHAR;
         decrypted += (char)index;
      }
      return decrypted;
   }

   // Behavior: 
   //   - checks if s is null, its length doesn't match the number
   //     of characters within the encodable range, 
   //     contains a duplicate character, or contains a 
   //     character that falls outside the encodable range
   // Parameters:
   //   - s: String we are checking
   // Returns:
   //   - boolean: false if any of the cases above occur, true otherwise
   private boolean isValid(String s) {
      boolean valid = true;
      if(s == null || Cipher.TOTAL_CHARS != s.length()) {
         valid = false;
      }
      else {
         Set<Character> check = new HashSet<>();
         for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < Cipher.MIN_CHAR || c > Cipher.MAX_CHAR) {
               valid = false;
            }
            check.add(c);
         }
         if (check.size() != s.length()) {
            valid = false;
         }
      }
      return valid;
   }
}
