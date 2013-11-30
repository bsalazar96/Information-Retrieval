package ir.webutils;
import java.util.*;
import java.io.*;
import ir.utilities.*;
import java.net.*; 	


public class PageRankSiteSpider extends PageRankSpider {

	public List<Link> getNewLinks(HTMLPage page) {
		List<Link> links = new LinkExtractor(page).extractLinks();
		URL url = page.getLink().getURL();
		ListIterator<Link> iterator = links.listIterator();
		while (iterator.hasNext()) {
			Link link = iterator.next();
			if (!url.getHost().equals(link.getURL().getHost()))
				iterator.remove();
		}
		return links;
	}


	public static void main(String args[]) {
		new PageRankSiteSpider().go(args);
	}

}