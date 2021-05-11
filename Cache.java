package finalproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class is responsible for caching list of crawled URLs
 */
public class Cache {

	private static String initialUrl;
	private static ArrayList<String> visitedUrlsList = new ArrayList<String>();

	Cache(String initialURL) {
		initialUrl = initialURL;
		createArrayListOfVisitedURls();
	}

	/**
	 * Creates an array list of already crawled URLs
	 */
	public static void createArrayListOfVisitedURls() {
		File dir = new File("ParsedOutput/");
		if (dir.isDirectory()) {
			File filesList[] = new File("ParsedOutput/").listFiles();
			if (filesList.length > 0) {
				for (int i = 0; i < filesList.length; i++) {
					String url = getURLFromCurrentFile(filesList[i].getName());
					if (url.equals(initialUrl)) {
						visitedUrlsList.add(url);
					}
				}
			}
		}
	}

	/**
	 * Checks is the initial URL is already crawled before. Returns true if it is
	 * already crawled else false
	 */
	public boolean checkIfPresentInCache() {
		boolean inCache = false;
		if (visitedUrlsList.size() > 0) {
			if (visitedUrlsList.contains(initialUrl)) {
				System.out.println("\nWill search the keyword from repositories in cache");
				inCache = true;
			} else {
				System.out.println("\nCrawling will be initiated as URL not found in cache");
				inCache = false;
			}
		}
		return inCache;
	}

	/**
	 * Gets the URL from the respective file
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
