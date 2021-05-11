package finalproject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

/**
 * Simple main method which acts as a menu for the whole application Creates
 * instances of crawling, caching and search engine
 */
public class MainController {

	private static HashSet<String> visitedWebPagesAfterCrawling;

	/**
	 * Used for deleting the cache folder
	 */
	public static void deleteParsedOutputFolder() {
		File cacheFolderParsedOutput = new File("ParsedOutput/");
		try {
			FileUtils.cleanDirectory(cacheFolderParsedOutput);
			cacheFolderParsedOutput.delete();
			System.out.println("\n");
			System.out.println("Deleted Cache folder....\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Simple main method acting as a menu for all operations in the application
	 * Creates instances of caching, crawling and web search engine
	 */
	public static void main(String[] args) throws IOException {
		// Simple menu for all operations
		System.out.println("\nEnter: \n" + "1 to crawl initial URL and search\n" + "2 to clear all Caches" + "\n");
		Scanner scanUserChoice = new Scanner(System.in);
		String userChoice = scanUserChoice.nextLine();

		// switch case with user's choice
		switch (userChoice) {

		case "1":
			System.out.println("\nEnter initial URL \n");
			Scanner scanInitialURL = new Scanner(System.in);
			String initialURL = scanInitialURL.nextLine();
			Cache ch = new Cache(initialURL);
			// checks if the initial URL is already crawled then perform search from
			// repository of downloaded webpages
			if (ch.checkIfPresentInCache()) {
				System.out.println("\n");
				System.out.println("Enter search keyword ");
				Scanner scanSearchKeyWord = new Scanner(System.in);
				String searchKeyword = scanSearchKeyWord.nextLine();
				WebSearchEngine ws = new WebSearchEngine(searchKeyword);
				ws.searchInDataBaseOfTextFiles();
				ws.rankWebPages();
				ws.printSearchResults();
			} else {
				// if the initial URL is not crawled then initiate crawling with the initial URL
				File cacheFolderParsedOutput = new File("ParsedOutput/");
				if (cacheFolderParsedOutput.exists()) {
					deleteParsedOutputFolder();
				}
				WebCrawler wc = new WebCrawler();
				visitedWebPagesAfterCrawling = wc.crawlAndGetLinksFromCurrentWebPage(initialURL, 1);
				WebParser webParser = new WebParser(visitedWebPagesAfterCrawling);
				try {
					webParser.parseEachFile();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				System.out.println("\nEnter search keyword");
				Scanner scanSearchKeyWord = new Scanner(System.in);
				String searchKeyword = scanSearchKeyWord.nextLine();
				WebSearchEngine ws = new WebSearchEngine(searchKeyword);
				ws.searchInDataBaseOfTextFiles();
				ws.rankWebPages();
				ws.printSearchResults();
			}
			break;

		case "2":
			deleteParsedOutputFolder();
			break;

		default:
			System.out.println("\nExiting from program invalid input...");
			System.exit(0);
			break;
		}
	}
}
