package finalproject;

import java.io.IOException;
import java.util.HashSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class used for crawling initial URL specified
 */
public class WebCrawler {

	private static HashSet<String> visitedLinks;
	private static int maxLevelOfCrawler = 5;

	WebCrawler() {
		visitedLinks = new HashSet<String>();
	}

	/**
	 * Recursion based web crawler with a depth
	 */
	public HashSet<String> crawlAndGetLinksFromCurrentWebPage(String referenceURL, int currentLevelOfCrawler) {
		if (!visitedLinks.contains(referenceURL) && currentLevelOfCrawler <= maxLevelOfCrawler) {
			Document document;
			try {
				if (visitedLinks.add(referenceURL)) {
					System.out.println("Crawling URL  " + referenceURL);
				}
				// Visits links on a page and visits its child pages recursively
				document = Jsoup.connect(referenceURL).get();
				Elements linksOnCurrentWebPage = document.select("a[href]");
				for (Element currentLink : linksOnCurrentWebPage) {
					crawlAndGetLinksFromCurrentWebPage(currentLink.attr("abs:href"), currentLevelOfCrawler++);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return visitedLinks;
	}
}
