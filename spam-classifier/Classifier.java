// Jessica Luo
// 3/5/2025
// CSE 123
// Programming Assignment 3: Spam Classifier
// TA: Benoit Lek
// This class represents a Classifier that predicts a label given some text-based data,
// It has two constructors, one builds a Classifier based on file input. The second constructor
// builds a Classifier from input data and corresponding labels. This 
// class has a method to classify a input and return a 
// label, and a method to save the Classifier to a file.
import java.io.*;
import java.util.*;

public class Classifier {
   private ClassifierNode overallRoot;

   // Behavior: 
   //   - Creates a Classifier based on file input
   //     The tree will be built using pre-order traversal.
   //     Every Desision node should have two lines of input, 
   //     one for the feature preceded by "Feature: ",
   //     and one for threshold preceded by "Threshold: ". For label nodes, 
   //     it should only feature the label. 
   //     It should be in the same order and format as created by the save method.
   // Parameter:
   //   - input: Scanner to read file input
   // Exceptions:
   //   - Throws an IllegalArgumentException if input is null
   public Classifier(Scanner input) {
      if (input == null) {
         throw new IllegalArgumentException();
      }
      overallRoot = classifierHelper(input);
   }

   // Behavior: 
   //   - Creates a Classifier based on file input
   //     The tree will be built using pre-order traversal
   //     Every intermediary node should have two lines of input, 
   //     one for the feature preceded by "Feature: ",
   //     and one for threshold preceded by "Threshold: ". For leaf nodes, 
   //     it should only feature the label. 
   //     It should be in the same order and format as created by the save method.
   // Parameter:
   //   - input: Scanner to read user input
   // Returns:
   //   - ClassifierNode: node we built given the current line(s) of user input
   private ClassifierNode classifierHelper(Scanner input) {
      if (!input.hasNextLine()) {
         return null;
      } 
      else {
         String line = input.nextLine();
         if (line.startsWith("Feature:")) {
            String feature = line.substring((line.indexOf(" ") + 1));
            line = input.nextLine();
            double threshold = Double.parseDouble(line.substring((line.indexOf(" ") + 1)));
            ClassifierNode node = new ClassifierNode(feature, threshold, 
                  classifierHelper(input), classifierHelper(input));
            return node;
         }
         else {
            return new ClassifierNode(line, null);
         }
      }
   }

   // Behavior: 
   //   - Creates a Classifier from the input data and corresponding labels
   // Parameter:
   //   - data: given list of TextBlock data
   //   - labels: given list of labels
   // Exceptions:
   //   - Throws an IllegalArgumentException if data or labels is null,
   //     if data and labels are not the same size, or if data or labels is empty
   public Classifier(List<TextBlock> data, List<String> labels) {
      if (data == null || labels == null || data.size() != labels.size() ||
            data.isEmpty() || labels.isEmpty()) {
         throw new IllegalArgumentException();
      }
      overallRoot = new ClassifierNode(labels.get(0), data.get(0));
      for (int i = 1; i < labels.size(); i++) {
         overallRoot = classifierHelper(overallRoot, data.get(i), labels.get(i));
      }
   }

   // Behavior: 
   //   - Creates a Classifier from the input data and corresponding labels.
   //     We traverse through our tree until we reach a leaf node. If the node's label matches the
   //     given label, we do nothing. If the labels don't match, we create a new decision 
   //     node and place it where the original leaf node currently is, placing
   //     the original leaf node and current input as subtrees.
   // Parameter:
   //   - node: node we are currently checking
   //   - data: given TextBlock data
   //   - labels: given label
   // Returns:
   //   - ClassifierNode: node we are returning (either the orginial node or a 
   //     new node based on if the labels match)
   private ClassifierNode classifierHelper(ClassifierNode node, TextBlock data, String label) {
      if(node.left == null && node.right == null) {
         if (node.label.equals(label)) {
            return node;
         }
         else {
            String feature = data.findBiggestDifference(node.data);
            double threshold = midpoint(data.get(feature), node.data.get(feature));
            ClassifierNode decisionNode;
            if (data.get(feature) < threshold) {
               decisionNode = new ClassifierNode(feature, threshold, 
                     new ClassifierNode(label, data), node);
            }
            else {
               decisionNode = new ClassifierNode(feature, threshold, node, 
                     new ClassifierNode(label, data));
            }
            return decisionNode;
         }
      }
      else if (data.get(node.feature) < node.threshold) {
         node.left = classifierHelper(node.left, data, label);
      } else {
         node.right = classifierHelper(node.right, data, label);
      }
      return node;
   }

   // Behavior: 
   //   - This method takes an input and returns the label this Classifier predicts
   // Parameter:
   //   - input: TextBlock input we are classifying
   // Returns:
   //   - String: the label this classifier predicts
   // Exceptions:
   //   - Throws an IllegalArgumentException if input is null 
   public String classify(TextBlock input) {
      if (input == null) {
         throw new IllegalArgumentException(); 
      }
      return classify(overallRoot, input);
   }

   // Behavior: 
   //   - This method compares a input(more specifically the corresponding word
   //     probability for the feature given by node) to the threshold given by node, 
   //     if it is less than the threshold, it explores the left subtree, if not
   //     it explores the right subtree
   // Parameter:
   //   - input: TextBlock input we are classifying
   //   - node: node we are currently comparing against
   // Returns:
   //   - String: the label this classifier predicts
   private String classify(ClassifierNode node, TextBlock input) {  
      if (node.left == null && node.right == null) {
         return node.label;
      }
      else if (input.get(node.feature) < node.threshold) {
         return classify(node.left, input);
      } else {
         return classify(node.right, input);
      }
   }
   
   // Behavior: 
   //   - Saves this Classifier to the given PrintStream using a pre-order traversal.
   //     Every Decision node will print two lines of data, one for feature
   //     preceded by "Feature: " and one for threshold preceded by "Threshold: ". For Label
   //     nodes, it will only print the label.
   //     It will be in the same order and format as used by the Scanner constructor.     
   // Parameter:
   //   - output: PrintStream we are printing to
   // Exceptions:
   //   - Throws an IllegalArgumentException if output is null
   public void save(PrintStream output) {
      if (output == null) {
         throw new IllegalArgumentException();
      }
      save(overallRoot, output);
   }

   // Behavior: 
   //   - Saves this Classifier to the given PrintStream, using a pre-order treversal.
   //     Every intermediary node will print two lines of data, one for feature
   //     preceded by "Feature: " and one for threshold preceded by "Threshold: ". For leaf 
   //     nodes, it will only print the label.
   //     It will be in the same order and format as used by the Scanner constructor.   
   // Parameter:
   //   - output: PrintStream we are printing to
   //   - node: node we are currently printing(aka saving)
   private void save(ClassifierNode node, PrintStream output) {
      if (node.left == null && node.right == null) {
         output.println(node.label);
      } else {
         output.println("Feature: " + node.feature);
         output.println("Threshold: " + node.threshold);
         save(node.left, output);
         save(node.right, output);
      }
   }


   // This class represents a single node in our Classifier, it can be either a 
   // decision node(feature) or a label node(label). This class has two contructors,
   // one constructor to construct a Desision Node, and one to construct a Label Node.
   private static class ClassifierNode {
      public final String feature;
      public final double threshold;
      public final String label;
      public final TextBlock data;
      public ClassifierNode left;
      public ClassifierNode right;
   
      // Behavior: 
      //   - Creates a Label ClassifierNode using the given label and data
      // Parameter:
      //   - label: given label for this node
      //   - data: given data TextBlock for this node
      public ClassifierNode(String label, TextBlock data) {
         this.feature = null;
         this.threshold = 0.0;
         this.label = label;
         this.data = data;
         this.left = null;
         this.right = null;
      }
   
       // Behavior: 
      //   - Creates a Desision ClassifierNode using the given feature and threshold
      // Parameter:
      //   - feature: given feature for this node
      //   - threshold: given threshold for this node
      //   - left: this node's left child
      //   - right: this node's right child
      public ClassifierNode(String feature, double threshold, 
            ClassifierNode left, ClassifierNode right) {
         this.feature = feature;
         this.threshold = threshold;
         this.label = null;
         this.data = null;
         this.left = left;
         this.right = right;
      }
   } 

   ////////////////////////////////////////////////////////////////////
   // PROVIDED METHODS - **DO NOT MODIFY ANYTHING BELOW THIS LINE!** //
   ////////////////////////////////////////////////////////////////////

   // Helper method to calcualte the midpoint of two provided doubles.
   private static double midpoint(double one, double two) {
      return Math.min(one, two) + (Math.abs(one - two) / 2.0);
   }    

   // Behavior: Calculates the accuracy of this model on provided Lists of 
   //           testing 'data' and corresponding 'labels'. The label for a 
   //           datapoint at an index within 'data' should be found at the 
   //           same index within 'labels'.
   // Exceptions: IllegalArgumentException if the number of datapoints doesn't match the number 
   //             of provided labels
   // Returns: a map storing the classification accuracy for each of the encountered labels when
   //          classifying
   // Parameters: data - the list of TextBlock objects to classify. Should be non-null.
   //             labels - the list of expected labels for each TextBlock object. 
   //             Should be non-null.
   public Map<String, Double> calculateAccuracy(List<TextBlock> data, List<String> labels) {
      // Check to make sure the lists have the same size (each datapoint has an expected label)
      if (data.size() != labels.size()) {
         throw new IllegalArgumentException(
                String.format("Length of provided data [%d] doesn't match provided labels [%d]",
                              data.size(), labels.size()));
      }
      
      // Create our total and correct maps for average calculation
      Map<String, Integer> labelToTotal = new HashMap<>();
      Map<String, Double> labelToCorrect = new HashMap<>();
      labelToTotal.put("Overall", 0);
      labelToCorrect.put("Overall", 0.0);
      
      for (int i = 0; i < data.size(); i++) {
         String result = classify(data.get(i));
         String label = labels.get(i);
      
         // Increment totals depending on resultant label
         labelToTotal.put(label, labelToTotal.getOrDefault(label, 0) + 1);
         labelToTotal.put("Overall", labelToTotal.get("Overall") + 1);
         if (result.equals(label)) {
            labelToCorrect.put(result, labelToCorrect.getOrDefault(result, 0.0) + 1);
            labelToCorrect.put("Overall", labelToCorrect.get("Overall") + 1);
         }
      }
   
      // Turn totals into accuracy percentage
      for (String label : labelToCorrect.keySet()) {
         labelToCorrect.put(label, labelToCorrect.get(label) / labelToTotal.get(label));
      }
      return labelToCorrect;
   }
}

