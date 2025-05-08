// Jessica Luo
// 1/22/2025
// CSE 123
// Programming Assignment 0: Ciphers
// TA: Benoit Lek
// This class represents an encryption scheme where the encoding 
// is given by shifting all encodable characters to the left
// shift times, moving the value at the front to the end each time. 
// This class has access to methods to set the encoding, as well
// as to encrypt and decrypt. 
// This class extends Substitution.
import java.util.*;

public class CaesarShift extends Substitution {
     
   // Behavior: 
   //   - Creates a new CaesarShift based on the given shift
   // Parameters:
   //   - shift: amount of times we are shifting to the left
   // Exceptions: 
   //   - If shift is less than or equal to 0,
   //     IllegalArgumentException will be thrown
   public CaesarShift(int shift) {
      super();
      if (shift <= 0) {
         throw new IllegalArgumentException();
      }
      Queue<Character> order = new LinkedList<>();
      for (int i = MIN_CHAR; i <= MAX_CHAR; i++) {
         char c = (char)i;
         order.add(c);
      }
      for (int i = 0; i < shift; i++) {
         order.add(order.remove());
      }
      String s = "";
      int size = order.size();
      for (int i = 0; i < size; i++) {
         s = s + order.remove();
      }
      super.setEncoding(s);
   }
}
   

