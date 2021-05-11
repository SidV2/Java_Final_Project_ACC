package finalproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * Class responsible for downloading web-pages into a text file
 */
public class WebParser {

	private static HashSet<String> visitedWebPages;

	WebParser(HashSet<String> visitedLinks) {
		visitedWebPages = visitedLinks;
	}

	/**
	 * Reads the list of visited URLs by the crawler
	 */
	public void parseEachFile() throws MalformedURLException {
		Iterator<String> it = visitedWebPages.iterator();
		int index = 0;
		while (it.hasNext()) {
			parseEachURLWithJsoup(it.next(), index++);
		}
	}

	/**
	 * Parses each url and saves it as a text file
	 */
	public static void parseEachURLWithJsoup(String urlInContext, int index) throws MalformedURLException {
		BufferedWriter bw = null;
		System.out.println("\nParsing URL and downloading webpage:  " + urlInContext);
		System.out.println("\n");
		File file = new File("ParsedOutput");
		file.mkdir();
		String fileName = getFileName(urlInContext, index);
		String outputFileLocation = String.format("ParsedOutput/%s", fileName);
		try {
			bw = new BufferedWriter(new FileWriter(outputFileLocation, true));
			Document doc = Jsoup.connect(urlInContext).get();
			Document.OutputSettings outputSettings = new Document.OutputSettings();
			outputSettings.prettyPrint(false);
			String htmlString = doc.html();
			String outputText = Jsoup.clean(htmlString, "", Whitelist.none(), outputSettings);
			bw.write(urlInContext);
			bw.write(outputText);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Helper method to form a filename for the visited URLs
	 */
	public static String getFileName(String urlInContext, int index) {
		String fileName = "";
		try {
			URL url = new URL(urlInContext);
			fileName = url.getHost();
			int pos1 = fileName.indexOf('.', 0);
			int pos = fileName.length();
			fileName = fileName.substring(pos1 + 1, pos) + index + ".txt";
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return fileName;
	}
}
