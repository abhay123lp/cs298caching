package com.sjsu.edu.node;

public class Node {

	private String webObject;
	private Integer popularity;
	private Long sizeOfObject;
	private Boolean visited;
	private String URL;
	private Long timestamp;
	
	public Long getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}


	public String getURL() {
		return URL;
	}


	public void setURL(String uRL) {
		URL = uRL;
	}


	public Node()
	{
		visited = new Boolean(false);
	}
	
	
	public Boolean getVisited() {
		return visited;
	}

	public void setVisited(Boolean visited) {
		this.visited = visited;
	}

	public Long getSizeOfObject()
	{
		return sizeOfObject;
	}

	public void setSizeOfObject(Long sizeOfObject) 
	{
		this.sizeOfObject = sizeOfObject;
	}

	public Integer getPopularity() 
	{
		return popularity;
	}

	public void setPopularity(Integer popularity) 
	{
		this.popularity = popularity;
	}

	public String getWebObject() 
	{
		return webObject;
	}

	public void setWebObject(String webObject) 
	{
		this.webObject = webObject;
	}

}
