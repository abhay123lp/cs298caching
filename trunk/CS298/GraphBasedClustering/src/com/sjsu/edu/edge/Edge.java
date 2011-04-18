package com.sjsu.edu.edge;

import com.sjsu.edu.node.Node;

public class Edge {
	
	private Integer count;
	private Node startNode;
	private Node endNode;
	private Double confidenceMeasure;
	
	public Double getConfidenceMeasure() {
		return confidenceMeasure;
	}

	public void setConfidenceMeasure(Double confidenceMeasure) {
		this.confidenceMeasure = confidenceMeasure;
	}

	public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
	
	public void incrementEdgeCount()
	{
		this.count +=1;
	}
	

}
