/**
 * An implementation of the AutoCompleteInterface using a DLB Trie.
 */

 import java.util.ArrayList;

 public class AutoComplete implements AutoCompleteInterface {
public static void main (String [] args){
    AutoComplete ac = new AutoComplete();
    ac.add("CS1501");
    ac.add("CS1502");
    ac.add("CS1503");
    ac.add("CS0447");
    ac.add("CS0441");
    ac.add("CS0445");
    ac.add("CS0449");
    ac.add("CS155");
    ArrayList<String> predictions = ac.retrievePredictions();
    ac.printarraylist(predictions);
  } 
  

  private DLBNode root; //root of the DLB Trie
  private StringBuilder currentPrefix; //running prefix
  private DLBNode currentNode; //current DLBNode
  //TODO: Add more instance variables as needed  
  



  
  //===========================================================================================
  //===========================================================================================



  @Override
  public boolean add(String word) {
    // TODO Auto-generated method stub
    if (word == null || word.length() == 0) {
      throw new IllegalArgumentException("Must type in a word to add to the dictionary!");
    }

    currentPrefix = new StringBuilder();
    currentNode = root;

    //for the first word added to the dictionary

    if (root == null){
      //System.out.println("Adding the first word to the dictionary");
      root = new DLBNode(word.charAt(0));

      currentNode = root;
      currentNode.size++;
      currentPrefix.append(word.charAt(0));
      for (int i = 1; i < word.length(); i++){
        currentNode.child = new DLBNode(word.charAt(i));
        //System.out.println("Adding the letter " + word.charAt(i));
        currentNode.child.parent = currentNode;
        currentNode = currentNode.child;
        currentNode.size++;
        currentPrefix.append(word.charAt(i));
      }
      currentNode.isWord = true;
      //System.out.println("The word " + currentPrefix + " has been added to the dictionary");
      currentNode = null;
      currentPrefix = new StringBuilder();
      return true;
    }

    if (isInTable(word, currentNode)){
      //System.out.println("The word is already in the table");
      //if word is in the table already, return false
      return false;
    }



    //now we know the word is not fully in the table, and the table is not empty, so we will add the word

    //goal here is to write/find the ith letter on the ith iteration of the loop (up until last letter obviously)

    for (int i = 0; i < word.length(); i++){
      //System.out.println("Testing the " + i + "th letter");
      if (findInRow(word.charAt(i), currentNode)){

        while (currentNode.data != word.charAt(i)){
          currentNode = currentNode.nextSibling;
        }
        // if we find the character in the row, move to the child node, which will be written in as null or simply moved to
        if (i != word.length() - 1 && currentNode.child == null){
          currentNode.child = new DLBNode('\0');
          currentNode.child.parent = currentNode;
          currentNode.size++;
          currentNode = currentNode.child;
        }
        else{
          currentNode.size++;
          currentNode = currentNode.child;
        }

      }
      else{
        System.out.println("Didn't find the character " + word.charAt(i) + " in the row " + currentNode.data);
        //if we don't find the character in the row, add it to the row, and move to the child node 
        //(which will not already exist given its a new node)
        

        //this could be a row with only the null character, or it could have a few characters 

        //if this is a row with only the null character, write to that null node and write another null child node
        if (currentNode.data == '\0'){
          currentNode.data = word.charAt(i);
          if (word.length() - 1 != i){
            currentNode.child = new DLBNode('\0');
            currentNode.child.parent = currentNode;
            currentNode.size++;
            currentNode = currentNode.child;
          }
        }

        //if this is not a null row, add the character to the row and move to the child node
        else{
          while (currentNode.nextSibling != null){
            currentNode = currentNode.nextSibling;
          }
          currentNode.nextSibling = new DLBNode(word.charAt(i));
          currentNode.nextSibling.previousSibling = currentNode;
          currentNode = currentNode.nextSibling;
          if (!(i == word.length() - 1)){
            currentNode.size++;
          }
          
          if (i != word.length() - 1){
            currentNode.child = new DLBNode('\0');
            currentNode.child.parent = currentNode;
            currentNode = currentNode.child;
          }
        }

        if (i == word.length() - 1){
          currentNode.size++;
          currentNode.isWord = true;
        }
    }
  }
  currentPrefix = new StringBuilder();
  currentNode = null;

  printTrie();
  return true;
}

   //===========================================================================================
    //===========================================================================================

  public boolean isInTable(String word, DLBNode currentNode){

    // need to check if the word is in the table
    // check first row for first letter
    // second row for second
    //etc etc

    for (int i = 0; i < word.length(); i++){
      if (findInRow(word.charAt(i), currentNode)){
        currentNode = currentNode.child;
      }
      else{
        return false;
      }
    }

    return true; 
  }

  public boolean findInRow(char letter, DLBNode currentNode){
    
    while (currentNode!= null){
      System.out.println("looking at node " + currentNode.data + " for letter " + letter);
      if (currentNode.data == letter){
        return true;
      }
      currentNode = currentNode.nextSibling;
  }

  return false;
}



  /**
   * Appends a character to the running prefix in O(alphabet size) time. 
   * This method doesn't modify the dictionary.
   * @param c: the character to append
   * @return true if the running prefix after appending c is a prefix to a word 
   * in the dictionary and false otherwise
   */
  @Override
  public boolean advance(char c) {
    

//if starting the traversal, also making sure current node is not null
    if (currentNode == null){
      currentNode = root;
      //search for the letter in the row of the current node
      if (findInRow(c, currentNode)){
        currentPrefix.append(c);
        return true;
      }
      return false;
    }

    if (currentNode.data == '\0'){
      return false;
    }

    if (currentNode.child == null){
      currentNode = new DLBNode('\0');
      currentPrefix.append(c);
      return false;
    }
    
    currentNode = currentNode.child;
    System.out.println("moving to " + currentNode.data);

    if (findInRow(c, currentNode)){
      //if you found the letter, append it to prefix
      currentPrefix.append(c);
      // if there is a child, move to it, and the next advance call will return false if it doesn't have a child (as currentNode.child ==null)
      return true;
    }

    currentPrefix.append(c);
    currentNode = new DLBNode('\0');
    System.out.println("moving to " + currentNode.data);

    
    return false;
  }


    

  @Override
  public void retreat() {
    if (currentPrefix.length() == 0){
      throw new IllegalStateException("The running prefix is the empty string");
    }

    if (currentNode == null){
      currentPrefix = new StringBuilder();
      return;
    }

    //return back to beginning state
    if (currentNode.parent == null){
      currentPrefix = new StringBuilder();
      currentNode = null;
      return;
    }

    //in the case where a user advances to a node that doesn't exist, and then retreats, the current node will be null
    //just do nothing, as the user is at a point not in the tree
    // they can reset this through the reset method
    if (currentNode.data == '\0'){
      return;
    }

    currentPrefix.deleteCharAt(currentPrefix.length() - 1);
    currentNode = currentNode.parent;




  }

  @Override
  public void reset() {
    currentPrefix = new StringBuilder();
    currentNode = null;
  }

  @Override
  public boolean isWord() {
    if (currentNode.isWord){
      return true;
    }
    return false;
  }

  @Override 
  public void add() {
    add(currentPrefix.toString()); //adds the current prefix to the dictionary
  }

  @Override
  public int getNumberOfPredictions() { 
    int count = currentNode.size;
    return count;
}


  @Override
  public String retrievePrediction() {
    //every tree needs to end with a complete word with this implementation, so just traverse through children until get to end
    if (currentNode.data == '\0'){
      return null;
    }

    if (currentNode == null){
      currentNode = root;
    }


    if (currentNode.isWord){
      return currentPrefix.toString();
    }
    
    StringBuilder prediction = new StringBuilder();
    prediction.append(currentPrefix);
    while (currentNode.child != null){
      currentNode = currentNode.child;
      prediction.append(currentNode.data);
    }
    return prediction.toString();
  }

  @Override
  public ArrayList<String> retrievePredictions() {
    //aaaaaaaahhhhhh 

    ArrayList<String> predictions = new ArrayList<String>();
    StringBuilder prediction = new StringBuilder();
    StringBuilder siblingpredictions = new StringBuilder();


    if (currentNode == null){
      currentNode = root;
    }

    if (currentNode.data == '\0'){
      return null;
    }

    if (currentNode.isWord){
      predictions.add(currentPrefix.toString());
    }
  //algorithm brainstorm
  // need to iterate through every child of the current node
  //go through every node of the current row
  // for each node, go to child and then through the entire row again
  //etc etc 

  //if the current node is a word, add it to the list of predictions

  retrievePredictionsHelper(currentNode, predictions, prediction, siblingpredictions);

  


    return predictions;

  }  

  public void retrievePredictionsHelper(DLBNode currentNode, ArrayList<String> predictions, StringBuilder prediction, StringBuilder siblingpredictions){
    if (currentNode == null){
      return;
    }

    //dont know if i need this really, but precautionary
    if (currentNode.data == '\0'){
      return;
    }

    if (currentNode.isWord){
      prediction.append(currentNode.data);
      predictions.add(prediction.toString());
      prediction.deleteCharAt(prediction.length() - 1);
    }

    if (currentNode.child != null){
      prediction.append(currentNode.data);
      retrievePredictionsHelper(currentNode.child, predictions, prediction, siblingpredictions);
      prediction.deleteCharAt(prediction.length() - 1);
    }

    if (currentNode.nextSibling != null){
      siblingpredictions.append(currentNode.data);
      retrievePredictionsHelper(currentNode.nextSibling, predictions, prediction, siblingpredictions);
      siblingpredictions.deleteCharAt(siblingpredictions.length() - 1);
    }
  }



public void printarraylist(ArrayList<String> list){
  for (int i = 0; i < list.size(); i++){
    System.out.println(list.get(i));
  }
}

  @Override
  public boolean delete(String word) {
    // TODO Auto-generated method stub

    



    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }  

   //The DLBNode class
   private class DLBNode{
    private char data; //letter inside the node
    private int size;  //number of words in the subtrie rooted at node
    private boolean isWord; //true if the node is at the end of a word
    private DLBNode nextSibling; //doubly-linked list of siblings
    private DLBNode previousSibling;
    private DLBNode child; // child reference
    private DLBNode parent; //parent reference

    private DLBNode(char data){ //constructor
        this.data = data;
        size = 0;
        isWord = false;
    }
  }

  /* ==============================
   * Helper methods for debugging
   * ==============================
   */

  //Prints the nodes in a DLB Trie for debugging. The letter inside each node is followed by an asterisk if
  //the node's isWord flag is set. The size of each node is printed between parentheses.
  //Siblings are printed with the same indentation, whereas child nodes are printed with a deeper
  //indentation than their parents.
  public void printTrie(){
    System.out.println("==================== START: DLB Trie ====================");
    printTrie(root, 0);
    System.out.println("==================== END: DLB Trie ====================");
  }

  //a helper method for printTrie
  private void printTrie(DLBNode node, int depth){
    if(node != null){
      for(int i=0; i<depth; i++){
        System.out.print(" ");
      }
      System.out.print(node.data);
      if(node.isWord){
        System.out.print(" *");
      }
      System.out.println(" (" + node.size + ")");
      printTrie(node.child, depth+1);
      printTrie(node.nextSibling, depth);
    }
  }
}

