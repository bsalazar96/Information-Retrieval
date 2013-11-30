package ir.webutils;
import java.util.*;
import java.io.*;
import ir.utilities.*;
import java.net.*;
import java.math.*;
import java.text.DecimalFormat;

public class PageRankSpider extends Spider{

	public double alpha = 0.15;
	public int iterations = 50;

//for the graph
	public Graph graph;
//to get a link from the name (P001.html) to the link of the url mapped to it
	public HashMap<String, Link> mapping;
//to keep track of which pages and links have already been indexed. 
	public HashSet<HTMLPage> indexedPages;
	public HashSet<Link> indexedLinks;


//constructor, initialize all of the data structures
	public PageRankSpider () {
		graph = new Graph();
		mapping = new HashMap<String, Link>();
		indexedPages = new HashSet<HTMLPage>();
		indexedLinks = new HashSet<Link>();
	}

	public void go(String[] args) {
		processArgs(args);
		doCrawl();
		calculatePageRanks();
		printRanks();
	}


//prints out the names and corresponding pageranks for the link indexed
	public void printRanks(){
		Node[] allNodes = graph.nodeArray();
		int size = allNodes.length;
		try {
			PrintWriter out = new PrintWriter(new FileWriter(new File(saveDir,"pageRanks.txt")));
			String name = "";
			double rank = 0.0;
			SortedSet<String> keys = new TreeSet<String>(mapping.keySet());


			for (String k : keys){
				Node temp = graph.getNode(mapping.get(k).toString());
				String formatted = new DecimalFormat("#.##########").format(new BigDecimal(temp.rank));

				out.print(k+"   " + formatted+"\n");
			}
			out.close();
		}
		catch (IOException e){
			System.err.println("HTMLPage.write(): " + e);
		}
	}

	//calculate pageRanks
	public void calculatePageRanks(){
		Node[] allNodes = graph.nodeArray();
		int size = allNodes.length;
		double e = alpha/size;
		double newRank = 0.0;
		double normalizedSum = 0.0;
		double c = 1.0;

		for (int i =0 ;i < allNodes.length; i++){
			allNodes[i].rank = 1.0/size;
		}


		for (int j =0; j< iterations; j++){
			double[] newRanks = new double[size];
			for (int index =0 ; index< size ; index++){
				normalizedSum = normalizedInEdges(allNodes[index]);
				newRank = (1-alpha)*normalizedSum + e;
				newRanks[index] = newRank;
			}
			c = 1.0/sumAllRank(newRanks); 

			for (int i = 0; i<size; i++)
				allNodes[i].rank = c * newRanks[i];
		}
	}

	public double sumAllRank (double[] nodes){
		double sum = 0.0;
		for (int i=0; i< nodes.length; i++){
			sum += nodes[i];
		}
		return sum;
	}

	public void indexPage(HTMLPage page) {
		String name = "P" + MoreString.padWithZeros(count, (int) Math.floor(MoreMath.log(maxCount, 10)) + 1);

		page.write(saveDir,name);
//System.out.println("Mapping " + (name+".html") + " to " + page.getLink().toString());
		mapping.put(name +".html", page.getLink());
	}

/* Sum the ranks of the all the nodes that point to n
and divide by the number of those nodes*/
public double normalizedInEdges (Node n){
	double sum = 0.0;
	if (n.edgesIn.size() != 0)
		for (Node k : n.edgesIn){
			sum += k.rank/k.edgesOut.size();
		}
		return sum;
	}

	public void doCrawl() {
		if (linksToVisit.size() == 0) {
//		System.err.println("Exiting: No pages to visit.");
			System.exit(0);
		}
		visited = new HashSet<Link>();
		while (linksToVisit.size() > 0 && count < maxCount) {
			if (slow) {
				synchronized (this) {
					try {
						wait(1000);
					}
					catch (InterruptedException e) {
					}
				}
			}
// Take the top link off the queue
			Link link = linksToVisit.remove(0);
			System.out.println("Trying: " + link);
// Skip if already visited this page
			if (!visited.add(link)) {
				System.out.println("Already visited");
				continue;
			}
			if (!linkToHTMLPage(link)) {
				System.out.println("Not HTML Page");
				continue;
			}
			HTMLPage currentPage = null;
// Use the page retriever to get the page
			try {
				currentPage = retriever.getHTMLPage(link);
			}
			catch (PathDisallowedException e) {
				System.out.println(e);
				continue;
			}
			if (currentPage.empty()) {
				System.out.println("No Page Found");
				continue;
			}
			if (currentPage.indexAllowed()) {
				count++;
				System.out.println("Indexing" + "(" + count + "): " + link);
				indexPage(currentPage);
				indexedPages.add(currentPage);
				indexedLinks.add(currentPage.getLink());
			}
			if (count < maxCount) {
				List<Link> newLinks = getNewLinks(currentPage);
				linksToVisit.addAll(newLinks);
			}
		}

//now that we have a HashSet of everything that can be indexed

		for (HTMLPage page : indexedPages){
			List<Link> children = getNewLinks(page);
			for (Link child : children){
				if (indexedLinks.contains(child) && !(page.getLink()).equals(child)){
					graph.addEdge(page.getLink().toString(),child.toString());
				}
			}
		}
	}


	public static void main(String args[]) {
		new PageRankSpider().go(args);
	}
}