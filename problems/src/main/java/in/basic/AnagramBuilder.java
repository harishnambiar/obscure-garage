package in.basic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * The AnagramBuilder accepts 2 arguments:<br/>
 * - file name of the dictionary file (containing words)<br/>
 * - length of the anagrams to be printed <br/>
 * 
 * If no arguments are passed it will be defaulted to the "dictionary.txt" and 5
 * 
 * The AnagramBuilder will parse all the words in the dictionary and group them
 * according to their anagrams, but will only print the group of anagrams which
 * has the length specified in the arguments to the program
 * 
 * @author hnambiar
 *
 */
public class AnagramBuilder {

	private static Logger LOGGER = Logger.getLogger(AnagramBuilder.class);
	private static String DEFAULT_DICTIONARY = "dictionary.txt";
	private static int DEFAULT_ANAGRAM_LENGTH = 5;

	public static void main(String[] args) {

		final String dictionaryFileName = StringUtils.isEmpty(args[0]) ? DEFAULT_DICTIONARY : args[0];
		final int anagramLength = StringUtils.isEmpty(args[1]) ? DEFAULT_ANAGRAM_LENGTH : Integer.parseInt(args[1]);

		final AnagramBuilder anagramBuilder = new AnagramBuilder();
		final Map<String, List<String>> anagramMap = anagramBuilder.populateFromDictionary(dictionaryFileName);
		anagramBuilder.printAnagrams(anagramMap, anagramLength);

	}

	/**
	 * Reads the dictionary and populates the MultiMap structure
	 * 
	 * @param fileName
	 * @return
	 */
	private Map<String, List<String>> populateFromDictionary(final String fileName) {
		final Map<String, List<String>> anagramMap = new HashMap<String, List<String>>();
		try (Scanner scanner = new Scanner(new File(fileName))) {
			while (scanner.hasNext()) {

				final String parsedWord = scanner.next();
				LOGGER.debug("BEFORE: " + parsedWord);
				final String alphabeticKey = createAlphabeticKey(parsedWord);
				LOGGER.debug("AFTER: " + alphabeticKey);
				List<String> anagrams = anagramMap.get(alphabeticKey);

				if (anagrams == null) {
					anagramMap.put(alphabeticKey, anagrams = new ArrayList<String>());
				}
				anagrams.add(parsedWord);

			}
		} catch (FileNotFoundException e) {
			LOGGER.error("Dictionary not found" +ExceptionUtils.getStackTrace(e));
			return Collections.emptyMap();
		}

		return anagramMap;
	}

	private void printAnagrams(final Map<String, List<String>> anagramMap, final int maxLength) {
		int groupCounter = 1;
		for (String anagramKey : anagramMap.keySet()) {
			if (anagramKey.length() == maxLength) {
				LOGGER.info("---------------");
				LOGGER.info("GROUP " + groupCounter++);
				LOGGER.info("---------------");
				for (String anagram : anagramMap.get(anagramKey)) {
					LOGGER.info(anagram);
				}
			}
		}

	}

	private String createAlphabeticKey(final String word) {
		char[] wordCharArray = word.toCharArray();
		Arrays.sort(wordCharArray);
		return new String(wordCharArray);
	}
}
