package finalproject;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import dependencies.*;

/**
 * Web search engine responsible for showing search results to users
 */
public class WebSearchEngine {

	private static HashMap<String, Integer> sortedSearchResults = new HashMap<String, Integer>();
	private static HashMap<String, Integer> searchResults = new HashMap<String, Integer>();
	private static HashMap<String, Integer> tempSortedList = new LinkedHashMap<String, Integer>();
	private static String searchKeyWord;

	WebSearchEngine(String searchKeyWord) {
		WebSearchEngine.searchKeyWord = searchKeyWord;
	}

	/**
	 * Used for searching in repository of text files. Stored inside parsed output
	 * folder
	 */
	public void searchInDataBaseOfTextFiles() {
		File filesList[] = new File("ParsedOutput/").listFiles();
		for (int i = 0; i < filesList.length; i++) {
			searchInEachFile(filesList[i]);
		}
	}

	/**
	 * searches in each file inside parsed output folder. Search algorithm used is
	 * Boyre Moore Good Suffix rule. Returns the frequency of the particular keyword
	 * inside each file.
	 * 
	 */
	public static void searchInEachFile(File currentFileInContext) {
		String inputFileName = currentFileInContext.getName();
		In inputStream = new In("ParsedOutput/" + inputFileName);
		String readSequenceFile = inputStream.readAll();
		int frequency = BoyreMooreCustom.search(readSequenceFile.toLowerCase().toCharArray(),
				searchKeyWord.toCharArray());
		storeSearchResults(frequency, inputFileName);
	}

	/**
	 * Search results are stored inside a hash-map called searchResults Map stores
	 * the URL and the frequency of search keyword in that particular URL
	 */
	public static void storeSearchResults(int frequency, String currentFileName) {
		String url = getURLFromCurrentFile(currentFileName);
		searchResults.put(url, frequency);
	}

	/**
	 * Rank web pages by sorting the frequencies of the search keyword in descending
	 * order
	 */
	public void rankWebPages() {

		// Iterate through searchResults HashMap and store entries in
		// sortedSearchResultArrayList
		List<Map.Entry<String, Integer>> sortedSearchResultArrayList = new LinkedList<Map.Entry<String, Integer>>(
				searchResults.entrySet());

		// Sort the list by value. Frequencies are stored as value inside searchResults
		// HashMap
		// Uses a comparator to iterate through entries of sortedSearchResultArrayList
		// and sorts the value(frequency) in descending order
		Collections.sort(sortedSearchResultArrayList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		for (Map.Entry<String, Integer> sortedItem : sortedSearchResultArrayList) {
			tempSortedList.put(sortedItem.getKey(), sortedItem.getValue());
		}
		// Stores sortedList of values(frequencies) in decreasing order in a HashMap
		sortedSearchResults = tempSortedList;
	}

	/**
	 * Print search results in console window by iteratiing through
	 * sortedSearchResults HashMap
	 */
	public void printSearchResults() {
		for (Entry<String, Integer> searchResult : sortedSearchResults.entrySet()) {
			System.out.println("\n");
			System.out.println("Keyword" + " " + searchKeyWord + " " + "occurs " + "in url:  " + searchResult.getKey()
					+ " " + searchResult.getValue() + " times");
		}
	}

	/**
	 * Helper method to get URL from current file. URLs are stored in first line of
	 * file
	 */
	public static String getURLFromCurrentFile(String currentFileName) {
		String firstLineForURL = "";
		try {
			BufferedReader bw = new BufferedReader(new FileReader("ParsedOutput/" + currentFileName));
			firstLineForURL = bw.readLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return firstLineForURL;
	}

}
