/*
 *This is a simple AI program. It works by creating statistics from 
 *the users input, creating words, and checking them against a word list.
 */
package aiapplication;

import static java.lang.Math.random;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

/*
 * @author Debrah Wyatt
 */
public class StoreResults {
    
    //This method will return the Ai's word length
    public static int AiWordLength(int avg, int max) {

        int topRank = max - avg + 1; //Top Rank
        int[] rank = new int[max]; //min to max word range
        int totalRank = 0;
        int[] rankPercent = new int[max];

        for (int i = 0; i < max; i++) {
            rank[i] = topRank - Math.abs(avg - i - 1); //associates the word size with its rank.
            totalRank += rank[i];        //Finds the sum of all ranks
        }

        //Converts rank into % likelyhood
        for (int i = 0; i < max; i++) {
            rankPercent[i] = rank[i] * 100 / totalRank;
        }

        //Stacks the percentages to add up to 100%
        int[] percentStack = new int[max];
        percentStack[0] = rankPercent[0];
        for (int i = 1; i < max; i++) {
            percentStack[i] = percentStack[i - 1] + rankPercent[i];
        }

        //Randomizer to pick the word size based on probability
        Random random = new Random();
        int pickWordSize = random.nextInt(100);
        for (int i = 0; i < max; i++) {
            if (pickWordSize <= percentStack[i]) {
                return (i + 1);
            }
        }
        return avg;
    }

    //Returns a character based on probability
    public static char AiCharStats(int[] count) {
        int rank = 0;
        //rank = the sum of every element in the count array.
        for (int i = 0; i < 26; i++) {
            rank = rank + count[i];
        }
        //Array creates a percent rank for each letter
        int[] rankPercent = new int[26];
        for (int i = 0; i < 26; i++) {
            rankPercent[i] = count[i] * 100 / rank;
        }

        //Stacks the percentages to add up to 100%
        int[] percentStack = new int[26];
        percentStack[0] = rankPercent[0];
        for (int i = 1; i < 26; i++) {
            percentStack[i] = percentStack[i - 1] + rankPercent[i];
        }

        //Randomizer to pick the letter based on probability
        Random random = new Random();
        int pickChar = random.nextInt(100);
        for (int i = 0; i < 26; i++) {
            if (pickChar <= percentStack[i]) {
                char c = (char) (i + 97); //Converts int to char
                return (c);
            }
        }
        return 99;//BUG-Normally would return the most likly Char
    }

    //This method creates a string of words
    public static String AiString(int aiStringLength, String input, int avgWordLength, int maxWord, int[] numberOfEachLetter) throws IOException {
            String aiString="";

        //Creates the ai string
        while (aiStringLength < input.length()) {

            String aiWord;
            String wordCheck = "";
            int aiWordLength = AiWordLength(avgWordLength, maxWord);

            //Creates a word
            int thisWordLength = AiWordLength(avgWordLength, maxWord);
            do {
                char[] charArray = new char[thisWordLength];
                for (int i = 0; i < thisWordLength; i++) {
                    charArray[i] = AiCharStats(numberOfEachLetter);
                }
                aiWord = new String(charArray); //Converts charArray to a String

               //Opens and reads the Dictionary
                Scanner inputFile = new Scanner(new File("common_words.txt"));
                while (inputFile.hasNext()) { 
                    wordCheck = inputFile.nextLine();
                    if (wordCheck.equalsIgnoreCase(aiWord)) {//Stops dictionary if word is found
                        aiString=aiString + aiWord + " "; //Combines the Strings
                        aiStringLength = aiStringLength + aiWordLength;//Tracks the string length
                        inputFile.close();
                        break;
                    }
                }
            } while (!wordCheck.equalsIgnoreCase(aiWord));
            
        }
        return aiString;
    }

    //This method accepts the users input and deconstructs it
    public static String UserString(String user)throws IOException{
        String input = user; // BUG-if statment does not end with punctuation
        int wordCount = 1;
        int wordLength = 0;
        int maxWord = 1;
        int avgWordLength;
        int letterCount = 0;
        int aiStringLength = 0;
        int[] numberOfEachLetter = new int[26]; //This arraw stores the frequency of letters used.

        //Initiallizes numberOfEachLetter by adding 1 to each element.
        for (int i = 0; i < 26; i++) {
            numberOfEachLetter[i]++;
        }

        int[] inputLength = new int[input.length()]; //This array creates a element for each character in the string.

        for (int i = 0; i < input.length(); i++) { //finds the word length

            //Stores the number of each character in the appropriate array spot; combines upper and lower cases
            if (input.charAt(i) >= 65 && input.charAt(i) <= 90) {//Upper case letters
                numberOfEachLetter[input.charAt(i) - 65]++;
            }

            if (input.charAt(i) >= 97 && input.charAt(i) <= 122) { //Lower case letters
                numberOfEachLetter[input.charAt(i) - 97]++;
            }

            inputLength[i] = input.charAt(i);//Stores the character value in each array
            if (inputLength[i] == 32) {
                wordCount++;//counts the number of words
            }
            if (inputLength[i] != 32 && inputLength[i] != 33 && inputLength[i] != 63 && inputLength[i] != 46) {
                wordLength++;
                letterCount++;//counts the number of letters

            } else if (inputLength[i] == 32 || inputLength[i] == 33 || inputLength[i] == 63 || inputLength[i] == 46) {
                if (wordLength > maxWord) {
                    maxWord = wordLength; // Finds the longest word
                }
                wordLength = 0;
            }
        }
        //Algorithms
        avgWordLength = letterCount / wordCount; //Finds the average word length

        //Generates the String
        return AiString(aiStringLength, input, avgWordLength, maxWord, numberOfEachLetter);
    }
}
