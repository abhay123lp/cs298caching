package com.sjsu.edu.graph;

import java.util.HashMap;
import java.util.Map;

import com.sjsu.edu.helpers.Request;
import com.sjsu.edu.node.Node;

public class Graph 
{
	/**
	 * nodeValues: Unique URL - Node Mapping 
	 * 
	 */
	private Map<String, Node> nodeValues;
	
	/**
	 * 
	 */
	private Map<Node,Integer> tableOfNodes;
	
	/**
	 * 
	 */
	private Node headNode;
	
	/**
	 * 
	 */
	private Node prevNode;
	//private Node currentNode;
	
	public Graph()
	{
		headNode = null;
		prevNode = null;
		tableOfNodes = new HashMap<Node, Integer>();
		nodeValues = new HashMap<String, Node>();
	}
	
	
	/**
	 *
	 * @param request
	 */
	public void addNode(Request request)
	{
		String httpURL = request.getRequestedURL().trim();
		Node newNode = null;
		Integer value = null;
	
		//If the headNode is null 
		if(headNode != null)
		{
			if(nodeValues.containsKey(httpURL))
			{	
				newNode = nodeValues.get(httpURL);
				
				value = tableOfNodes.get(newNode);
				
				value = value+1;
				
				tableOfNodes.put(newNode, value);
				
				//nodeValues.put(httpURL, newNode);
				
				if(prevNode == headNode)
					headNode.setNextNode(newNode);
				else
					prevNode.setNextNode(newNode);
					
				prevNode = newNode;
				
			}
			else
			{				
				value = new Integer(1);
				
				//Create a new Node
				newNode = new Node();
				
				//Set the value of URL for the Node
				newNode.setValue(httpURL);
				
				//Update the Unique URL Table
				nodeValues.put(httpURL, newNode);
				
				//Update the Table of Nodes, with the popularity
				tableOfNodes.put(newNode, value);
				
				if(prevNode == headNode)
					headNode.setNextNode(newNode);
				else//Set the previous node's nextNode value to the new Node
					prevNode.setNextNode(newNode);
				
				//Previous Node
				prevNode = newNode;
			}
		}
		else
		{//If newNode is null
			
			value = new Integer(0);
			
			newNode = new Node();
			
			newNode.setValue(httpURL);
			
			nodeValues.put(httpURL, newNode);
			
			tableOfNodes.put(newNode, value);
			
			prevNode = newNode;
			
			headNode = prevNode;
		}
		
	}
	
	public static void main(String[] args)
	{
		Graph graph = new Graph();
		
		Request request = new Request();
		
		//ABCBCDCFD
		
		request.setRequestedURL("A");
		
		graph.addNode(request);
		
		request = new Request();
		
		request.setRequestedURL("B");
		
		graph.addNode(request);
		
		request = new Request();
		
		request.setRequestedURL("C");
		
		graph.addNode(request);
		
		request = new Request();
		
		//B-C-D-C-F-D
		
		request.setRequestedURL("B");
		
		graph.addNode(request);
		
		request = new Request();
		
		//C
		request.setRequestedURL("C");
		
		graph.addNode(request);
		
		request = new Request();
		
		//D
		request.setRequestedURL("D");
		
		graph.addNode(request);
		
		request = new Request();
		
		request.setRequestedURL("C");
		
		graph.addNode(request);
		
		request = new Request();
		
		request.setRequestedURL("F");
		
		graph.addNode(request);
		
		request = new Request();
		
		request.setRequestedURL("D");
		
		graph.addNode(request);
	}
}
