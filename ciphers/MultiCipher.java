// Jessica Luo
// 1/22/2025
// CSE 123
// Programming Assignment 0: Ciphers
// TA: Benoit Lek
// This class represents an encryption scheme where we pass the original input
// through a given list of ciphers one at a time, using the previous cipher's
// output as the input to the next.
// This class has its own methods to encrypt and decrypt.
// This class extends Substitution.
import java.util.*;

public class MultiCipher extends Substitution {
   private List<Cipher> ciphers;

   // Behavior: 
   //   - Creates a new MultiCipher based on the given
   //     list of ciphers
   // Parameters:
   //   - ciphers: list of ciphers
   // Exceptions: 
   //   - If ciphers is null,
   //     IllegalArgumentException will be thrown
   public MultiCipher(List<Cipher> ciphers) {
      super();
      if (ciphers == null) {
         throw new IllegalArgumentException();
      }
      this.ciphers = ciphers;
   }

   // Behavior: 
   //   -  Applies each Cipher to input in the order given by ciphers, with 
   //      the previous cipher's output acting as the new input
   // Parameters:
   //   - input: the string to be encrypted. Should not be null and 
   //     all its characters should be within the encodable range.
   // Returns: 
   //   - String: the result after encrypting
   // Exceptions: 
   //   - If input is null, 
   //     IllegalArgumentException will be thrown   
   public String encrypt(String input) {
      if (input == null) {
         throw new IllegalArgumentException();
      }
      String encrypted = input;
      for (int i = 0; i < ciphers.size(); i++) {
         Cipher c = ciphers.get(i);
         encrypted = c.encrypt(encrypted);
      }
      return encrypted;
   }
        
   // Behavior: 
   //   -  Applies the inverse of each Cipher in the inverse order given by
   //      ciphers, with the previous cipher's output acting as the new input
   // Parameters:
   //   - input: the string to be decrypted. Should not be null and 
   //     all its characters should be within the encodable range.
   // Returns: 
   //  - String: the result after decrypting
   // Exceptions: 
   //   - If input is null, 
   //     IllegalArgumentException will be thrown
   public String decrypt(String input){
      if (input == null) {
         throw new IllegalArgumentException();
      }
      String decrypted = input;
      for (int i = ciphers.size() - 1; i >= 0; i--) {
         Cipher c = ciphers.get(i);
         decrypted = c.decrypt(decrypted);
      }
      return decrypted;
   }
}
   

