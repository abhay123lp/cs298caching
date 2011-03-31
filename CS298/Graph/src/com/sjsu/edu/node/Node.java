package com.sjsu.edu.node;

import java.util.HashMap;
import java.util.Map;

public class Node {

	private String value;
	private Map<Node,Integer> nextNodes;
	
	public Node()
	{
		value = null;
		nextNodes = new HashMap<Node, Integer>();
	}
	
	/**
	 * Sets the Next Nodes for 'this' Node 
	 * 
	 * @param nextNode
	 */
	public void setNextNode(Node nextNode)
	{
		Integer value = null;
		
		if(nextNodes.containsKey(nextNode))
		{
			value = nextNodes.get(nextNode);
			value = value+1;
			nextNodes.put(nextNode, value);
		}
		else
		{
			value = new Integer(1);
			nextNodes.put(nextNode, value);
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Map<Node, Integer> getNextNodes() {
		return nextNodes;
	}

	public void setNextNodes(Map<Node, Integer> nextNodes) {
		this.nextNodes = nextNodes;
	}
}
