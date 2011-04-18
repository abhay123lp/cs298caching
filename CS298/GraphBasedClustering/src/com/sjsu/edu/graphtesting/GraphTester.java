package com.sjsu.edu.graphtesting;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.sjsu.edu.edge.Edge;
import com.sjsu.edu.graphintersite.InterSiteGraph;
import com.sjsu.edu.graphlogfilereader.GraphLogFileReader;
import com.sjsu.edu.node.Node;

public class GraphTester {
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		//GraphTester gTester = new GraphTester();
		
		//gTester.createAndProcessDefaultGraph(gTester);
		
		String baseDirectory = "D:\\ClientIPCustomizedLogFile\\";
		
		InterSiteGraph g = new InterSiteGraph();
		
		GraphLogFileReader graphLogFileReader = new GraphLogFileReader(baseDirectory);
		
		graphLogFileReader.processLogFile();
		
	}
	
	/**
	 * 
	 * Creates and processes a default graph for processing
	 * and obtains a cluster of objects from the graph
	 * 
	 * @param gTester
	 */
	
	public void createAndProcessDefaultGraph(GraphTester gTester)
	{
		
		InterSiteGraph g = new InterSiteGraph();
		
		//Create Default Graph
		gTester.createDefaultGraph(g);
				
		Collection<Edge> edges = g.getEdges();
		
		//Cut with Support
		g.cutWithSupport((Integer) 3);
		
		System.out.println(" +++++ ***** **** +++++");
		System.out.println("AFTER CUT WITH SUPPORT");
		System.out.println(" +++++ ***** **** +++++");
		
		for (Edge edge : edges)
		{			
			System.out.println("Edge: " + edge.getStartNode().getURL() + "-->"
					+ edge.getEndNode().getURL() + ", Edge Count:" + edge.getCount());
			
			System.out.println();
		}
		
		g.cutWithConfidence((Double) 0.2);
		
		System.out.println(" +++++ ***** **** +++++");
		System.out.println("AFTER CUT WITH CONFIDENCE");
		System.out.println(" +++++ ***** **** +++++");
		
		edges = g.getEdges();
		
		for (Edge edge : edges) {
			
			System.out.println("Edge: " + edge.getStartNode().getURL() + "-->"
					+ edge.getEndNode().getURL() + ", Edge Count:" + edge.getCount()
					+ ",Confidence Measure: " + edge.getConfidenceMeasure());
			
			System.out.println();
		}
		
		g.preprocessGraph();
			
		/*Collection<Node> nodes = g.getNodes();
		
		for (Node node : nodes) {
			
			System.out.println("Node URL: " + node.getURL() + ", Node Popularity: " +
					node.getPopularity());
			
			System.out.println();
			
		}*/
		
		Collection<Node> nodes = g.getNodes();
		
		System.out.println(" +++++ ***** **** +++++");
		System.out.println("AFTER GRAPH PRE-PROCESSING");
		System.out.println(" +++++ ***** **** +++++");
				
		for (Node node : nodes) {
			System.out.println(node.getURL());
		}
		
		int i =1;
		
		List<Set<Node>> listOfCluster = g.breadthFirstTraversal();
		
		System.out.println();
		System.out.println("**** ++++ PRINTING CLUSTERS ++++ *****");
		System.out.println();
		
		for (Set<Node> set : listOfCluster) {
			
			System.out.println("++++ ***** Begin Cluster " + i + " **** ++++");
			
			for (Node node : set) 
			{
				System.out.println(node.getURL());
			}
			
			System.out.println("++++ ***** End Cluster " + i + " **** ++++");
			System.out.println();
			i+=1;
			
			
		}
	}
	
	/**
	 * Create a Default Graph for testing
	 * 
	 * @param g
	 */
	public void createDefaultGraph(InterSiteGraph g)
	{
		Node nextNode = new Node();
		nextNode.setURL("GOOGLE");
		//nextNode.setURL("www.google.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("FACEBOOK");
		//nextNode.setURL("www.facebook.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("GOOGLE");
		//nextNode.setURL("www.google.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("FACEBOOK");
		//nextNode.setURL("www.facebook.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("GOOGLE");
		//nextNode.setURL("www.google.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("HOTMAIL");
		//nextNode.setURL("www.hotmail.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("NDTV");
		//nextNode.setURL("www.ndtv.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("IBNLIVE");
		//nextNode.setURL("www.ibnlive.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("TECRUNCH");
		//nextNode.setURL("www.ibnlive.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("IBNLIVE");
		//nextNode.setURL("www.ibnlive.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("TECRUNCH");
		//nextNode.setURL("www.ibnlive.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("IBNLIVE");
		//nextNode.setURL("www.ibnlive.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("TECRUNCH");
		//nextNode.setURL("www.ibnlive.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("IBNLIVE");
		//nextNode.setURL("www.ibnlive.com");
		g.addNode(nextNode);
		
		
		nextNode = new Node();
		nextNode.setURL("GOOGLE");
		//nextNode.setURL("www.google.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("FACEBOOK");
		//nextNode.setURL("www.facebook.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("HOTMAIL");
		//nextNode.setURL("www.hotmail.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("FACEBOOK");
		//nextNode.setURL("www.facebook.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("IBNLIVE");
		//nextNode.setURL("www.ibnlive.com");
		g.addNode(nextNode);
		
		nextNode = new Node();
		nextNode.setURL("FACEBOOK");
		//nextNode.setURL("www.facebook.com");
		g.addNode(nextNode);
		
	}

}
