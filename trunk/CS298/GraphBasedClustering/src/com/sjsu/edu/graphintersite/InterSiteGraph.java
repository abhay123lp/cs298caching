package com.sjsu.edu.graphintersite;

import java.util.Collection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sjsu.edu.commonhelpers.Request;
import com.sjsu.edu.edge.Edge;
import com.sjsu.edu.node.Node;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.event.GraphEvent.Vertex;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * 
 * GRAPH API -
 * 
 * 1) Create a Graph
 * 2) Edit Graph by Confidence value
 * 3) Edit Graph by Support value
 * 4) Compute the cluster for inter site clustering
 *  
 * @author Administrator
 *
 */

public class InterSiteGraph 
{
	/**
	 * 
	 * A map to store the URL - Node in the graph mapping
	 * 
	 */
	private Map<String,Node> nodeSet;
	
	/**
	 * Instance of a directed graph that actually holds the graph objects
	 */
	private DirectedGraph<Node, Edge> graphInterSite;
	private Node prevNode;
		
	public InterSiteGraph()
	{
		prevNode = null;
		nodeSet = new HashMap<String, Node>();
		graphInterSite = new DirectedSparseGraph<Node, Edge>();
	}
	
	/**
	 * Returns all the Edges in the Graph
	 * 
	 * @return
	 */
	public Collection<Edge> getEdges()
	{
		Collection<Edge> edges = graphInterSite.getEdges();
		
		return edges;
	}
	
	/**
	 * Returns all the Nodes/Vertices in the Graph
	 * 
	 * @return
	 */
	public Collection<Node> getNodes()
	{
		Collection<Node> nodes = graphInterSite.getVertices();
		
		return nodes;
	}
	
	/**
	 * 
	 * Adds node to the graph
	 * 
	 * @param nextNode
	 */
	public void addNode(Node nextNode)
	{
		Integer edgeCount = null;
		String URL = null;
		Edge edge = null;
		Edge e = null;
		
		if(prevNode!=null) //Graph is Already created
		{
			URL = nextNode.getURL().trim();
			
			if(nodeSet.containsKey(URL))
			{
				nextNode = nodeSet.get(URL);
				
				edge = graphInterSite.findEdge(prevNode,nextNode);
				
				//Edge Found
				if(edge!= null)
				{
					edgeCount = edge.getCount();
					edgeCount +=1;
					
					edge.setCount(edgeCount);
					
					e = graphInterSite.findEdge(prevNode, nextNode);
						
					//Update the next Node's Popularity
					updateNodePopularity(nextNode);
					
					prevNode = nextNode;
				}
				else // Add New Edge
				{
					e = new Edge();
					
					e.setCount((Integer) 1);
					
					//Set the Start Node & End Node
					e.setStartNode(prevNode);
					e.setEndNode(nextNode);
					
					graphInterSite.addEdge(e, prevNode, nextNode);
					
					//Update the next Node's Popularity
					updateNodePopularity(nextNode);
					
					//Set the Previous Node value to this Node
					prevNode = nextNode;
				}
			}
			else
			{
				//Add the nextNode to the Graph
				graphInterSite.addVertex(nextNode);
				
				//Add the Node to the Node Set
				nodeSet.put(nextNode.getURL(), nextNode);
				
				//Create a new Edge
				edge = new Edge();
				
				edge.setCount((Integer)1);
				
				//Set the Start Node and the End Node
				edge.setStartNode(prevNode);
				edge.setEndNode(nextNode);
				
				graphInterSite.addEdge(edge, prevNode, nextNode);
				
				//Update the next Node's Popularity
				updateNodePopularity(nextNode);
				
				//Set the prevNode as nextNode 
				prevNode = nextNode;
			}
		}
		else //Start of the Graph
		{
			//Add the Head Node to the Graph
			nodeSet.put(nextNode.getURL(), nextNode);
			
			//Set the Value of the previous Node to this node
			prevNode = nextNode;
			
			//Add the Node as a vertex to the Graph
			graphInterSite.addVertex(nextNode);
		}
	}
	
		
	/**
	 * Method to update the popularity of the node
	 * 
	 * @param node
	 */
	private void updateNodePopularity(Node node)
	{
		//Get the Node popularity
		Integer nodePopularity = node.getPopularity();
		
		if(nodePopularity == null)
		{
			node.setPopularity((Integer)1);
		}
		else //Increment it by one 
		{	
			nodePopularity +=1;
			node.setPopularity(nodePopularity);
		}
	}
	
	/**
	 * 
	 * Procedure to cut with confidence
	 * 
	 * @param confidenceThreshold
	 */
	public void cutWithConfidence(Double confidenceThreshold)
	{
		//Set of Edges for the Graph
		Collection<Edge> edgeSet = graphInterSite.getEdges();
		
		//Directed Edge
		Edge edge = null;
		
		//Directed Edge Start Node
		Node edgeStartNode = null;
		
		//Directed Edge popularity
		Integer edgeCount = null;
		
		//Confidence Measure
		Double confidenceMeasure = null;
		
		//Start Node Popularity
		Integer edgeStartNodePopularity = null;
		
		//Edges to be discarded from the graph
		List<Edge> edgeToBeRemoved = new LinkedList<Edge>();
		
		for (Iterator<Edge> iterator = edgeSet.iterator(); iterator.hasNext();) 
		{
			//Get the edge
			edge = (Edge) iterator.next();
			
			//Get the edge count for the edge
			edgeCount = edge.getCount();
			
			//Get the start node for the directed edge
			edgeStartNode = edge.getStartNode();
			
			Node endNode = edge.getEndNode();
			
			//Get the popularity of the start node
			edgeStartNodePopularity = edgeStartNode.getPopularity();
			
			if(edgeStartNodePopularity!=null) //Get the confidence, conditional probability f(A,B)/f(A)
			{
				confidenceMeasure = (new Double(edgeCount.toString())/ new Double(edgeStartNodePopularity.toString()));
				edge.setConfidenceMeasure(confidenceMeasure);
				
				//Add the edge to list of edges to be removed if the edge confidence value
				//is less than confidence threshold value
				//if(confidenceMeasure >= confidenceThreshold)
				if(!((confidenceMeasure > confidenceThreshold) || (confidenceMeasure == confidenceThreshold)))
					edgeToBeRemoved.add(edge);
			}
			else
			{
				edgeToBeRemoved.add(edge);
			}				
		}
		
		if(edgeToBeRemoved.size()>0)
		{
			//Remove all the edges that do not meet minimum confidence threshold
			for (Edge discardEdge : edgeToBeRemoved) 
			{	
				graphInterSite.removeEdge(discardEdge);
			}
		}
	}
	
	/**
	 * 
	 * Procedure to cut with Support
	 * 
	 * @param supportThreshold
	 */
	public void cutWithSupport(Integer supportThreshold)
	{
		Collection<Edge> edgeSet = graphInterSite.getEdges();
		
		//Edges to be Removed
		List<Edge> edgeToBeRemoved = new LinkedList<Edge>();
		
		//Individual Edge
		Edge edge = null;
		
		//Edge count
		Integer edgeCount = null;
		
		//EdgeSet iterator
		for (Iterator<Edge> iterator = edgeSet.iterator(); iterator.hasNext();) 
		{
			//Get the edge
			edge = (Edge) iterator.next();
			
			//Get the edgeCount
			edgeCount = edge.getCount();
			
			//If the edgeCount is greater than the support threshold
			if(!((edgeCount > supportThreshold) || (edgeCount == supportThreshold)))
			{
				edgeToBeRemoved.add(edge);
			}
		}
		
		//Discard all the edges that do not meet support threshold
		if(edgeToBeRemoved.size()>0)
		{
			for (Edge discardEdge : edgeToBeRemoved) 
			{
				graphInterSite.removeEdge(discardEdge);				
			}
		}
	}
	
	/**
	 * Does a breath first Traversal for the remaining of
	 * the Graph
	 * 
	 */
	public List<Set<Node>> breadthFirstTraversal()
	{
		Collection<Node> verticesOfGraph = graphInterSite.getVertices();
		
		LinkedList<Node> queue = new LinkedList<Node>();
		
		Set<Node> clusterOfNodes = new HashSet<Node>();
		List<Set<Node>> listOfClusters = null;
		
		boolean entryNode = true;
		
		/*System.out.println(" +++++ ***** **** +++++");
		System.out.println(" BFS ");
		System.out.println(" +++++ ***** **** +++++");*/
		
		Node n = getUnvisitedNode();
		
		Node neighbhorNode = null;
		
		if(n== null)
			return listOfClusters;
		
		n.setVisited(true);
		clusterOfNodes.add(n);
		queue.push(n);
		//System.out.println(n.getURL());
		
		if(verticesOfGraph!=null)
		{
			listOfClusters = new LinkedList<Set<Node>>();
			
			while(getUnvisitedNode()!=null)
			{
				if(entryNode)
					entryNode = false;
				else
				{
					Node e = getUnvisitedNode();
					queue.push(e);
				}
				
				while(!queue.isEmpty())
				{
					Node node = queue.remove();
					node.setVisited(true);
					Node child = null;
					
					clusterOfNodes.add(node);
					
					while((child = getUnvisitedChildNode(node))!= null)
					{
						child.setVisited(true);
						//boolean added = clusterOfNodes.add(child);
						clusterOfNodes.add(child);
						queue.add(child);
					}
				}
				
				listOfClusters.add(clusterOfNodes);
				clusterOfNodes = new HashSet<Node>();
			}
		}
		
		/*System.out.println(" +++++ ***** **** +++++");
		System.out.println(" BFS ENDS ");
		System.out.println(" +++++ ***** **** +++++");*/
		
		return listOfClusters;
	}
	
	
	/**
	 * Helper to remove nodes from the graph who do not have
	 * any neighbors or edge pointing from the node is pointing
	 * to the node itself and there are no other nodes
	 * 
	 */
	public void preprocessGraph()
	{
		Collection<Node> nodesOfGraph = graphInterSite.getVertices();
		
		Set<Node> nodesToBeRemoved = new HashSet<Node>();
		
		for (Node node : nodesOfGraph)
		{						
			if(graphInterSite.getNeighbors(node).size()== 0)
			{
				nodesToBeRemoved.add(node);
			}
			else if(graphInterSite.getNeighborCount(node)== 1)
			{
				Collection<Node> neighbors = graphInterSite.getNeighbors(node);
				
				for (Node node2 : neighbors) 
				{
					if(node2 == node)
					{
						nodesToBeRemoved.add(node2);
					}
				}
			}
			else
			{
				continue;
			}	
		}
		
		if(nodesToBeRemoved.size() > 0)
		{
			for (Node node : nodesToBeRemoved) 
			{
				graphInterSite.removeVertex(node);
			}
		}
	}
	
	/**
	 * 
	 * Helper to get Unvisited Neighbor Node for the child Node
	 * 
	 * @param childNode
	 * @return
	 */
	private Node getUnvisitedChildNode(Node childNode)
	{		
		Collection<Node> neighbourNodes = graphInterSite.getNeighbors(childNode);
		
		for (Node node : neighbourNodes) 
		{
			if(!node.getVisited())
			{
				//node.setVisited(true);
				return node;
			}
		}
		
		return null;	
	}
	
	
	/**
	 * 
	 * Process Graph with confidence threshold and support threshold and
	 * do a graph traversal using BFS to obtain a cluster of nodes and
	 * return the same
	 * 
	 * @param confidenceThreshold
	 * @param supportThreshold
	 * @return
	 */
	public List<Set<Node>> processGraph(Double confidenceThreshold,Integer supportThreshold)
	{
		List<Set<Node>> clusterOfNodes = null;
		
		System.out.println("--- ---- -- CUT WITH CONFIDENCE -- --- ---");
		
		cutWithConfidence(confidenceThreshold);
		
		System.out.println("--- ---- -- CUT WITH SUPPORT -- --- ---");
		
		cutWithSupport(supportThreshold);
		
		//Remove all vertices(Web page nodes) from the graph, that have no neighbors,
		// and edges out of the vertex(Web page Node) point to itself
		preprocessGraph();
		
		System.out.println("--- ---- -- BFS TRAVERSAL -- --- --- ");
		clusterOfNodes = breadthFirstTraversal();
		
		return clusterOfNodes;
	}
	
	/**
	 * 
	 * Helper to get Unvisited Neighbor Node for the child Node
	 * 
	 * @param childNode
	 * @return
	 */
	private Node getUnvisitedNode(Node node)
	{		
		Collection<Node> neighbourNodes = graphInterSite.getNeighbors(node);
		
		for (Node node1 : neighbourNodes) 
		{
			if(!node1.getVisited())
			{
				node1.setVisited(true);
				return node;
			}
		}
		
		return null;	
	}
	
	private Node getUnvisitedNode()
	{		
		Collection<Node> neighbourNodes = graphInterSite.getVertices();
		
		for (Node node : neighbourNodes) 
		{
			if(!node.getVisited())
			{
				//node.setVisited(true);
				return node;
			}
		}
		
		return null;	
	}
	
	public void processRequest(Request request)
	{
		if(prevNode!=null)
		{
			if(!prevNode.getURL().equals(request.getRequestedURL()))
			{
				Node node = new Node();
				
				node.setTimestamp(request.getRequestTime());
				node.setURL(request.getRequestedURL());
				node.setSizeOfObject(request.getRequestedItemSize());
				
				addNode(node);
			}
		}
		else
		{
			Node node = new Node();
			
			node.setTimestamp(request.getRequestTime());
			node.setURL(request.getRequestedURL());
			node.setSizeOfObject(request.getRequestedItemSize());
			
			addNode(node);
		}
	}
	
}
