import java.util.ArrayList;
/**
 * An interface for an AutoComplete dictionary that provides word suggestions for an 
 * auto-complete system by maintaining a running prefix
 */

public interface AutoCompleteInterface {

  /**
   * Adds a word to the dictionary in O(alphabet size*word.length()) time
   * @param word the String to be added to the dictionary
   * @return true if add is successful, false if word already exists
   * @throws IllegalArgumentException if word is the empty string or null
   */
    public boolean add(String word);

  /**
   * Appends a character to the running prefix in O(alphabet size) time. 
   * This method doesn't modify the dictionary.
   * @param c: the character to append
   * @return true if the running prefix after appending c is a prefix to a word 
   * in the dictionary and false otherwise
   */
    public boolean advance(char c);

  /**
   * Removes the last character from the running prefix in O(alphabet size) 
   * time. This method doesn't modify the dictionary.
   * @throws IllegalStateException if the running prefix is the empty string
   */
    public void retreat();

  /**
   * Resets the current prefix to the empty string in O(1) time
   */
    public void reset();
    
  /**
   * Checks if the running prefix is a word in the dictionary in O(1) time.
   * @return true if the running prefix is a word in the dictionary and false
   * otherwise.
   */
    public boolean isWord();

  /**
   * Adds the running prefix as a word to the dictionary (if not already a word)
   * The running time is O(alphabet size*length of the running prefix). 
   */
    public void add();

  /** 
   * Retrieves the number of dictionary words that start with the running prefix in O(1) time.
   * @return the number of dictionary words that start with the running 
   * prefix (including the running prefix if it is a word).
   */
    public int getNumberOfPredictions();
  
  /**
   * Retrieves one dictionary word that starts with the running prefix. The running time is 
   * O(length of the returned word)
   * @return a String or null if no predictions exist for the running prefix
   */
    public String retrievePrediction();

   /**
   * Retrieves a lexicographically sorted list of all dictionary words that start with the running prefix. The running time is 
   * O(length of the returned words)
   * @return an ArrayList<String> of sorted word predictions or null if no predictions exist for the running prefix
   */
    public ArrayList<String> retrievePredictions();

  /**
   * EXTRA CREDIT: Deletes a word from the dictionary in O(alphabet size*word.length()) time.
   * @param word the String to be deleted from the dictionary
   * @return true if delete is successful, false if word doesn't exist
   * @throws IllegalArgumentException if word is the empty string or null
   */
    public boolean delete(String word);

}
