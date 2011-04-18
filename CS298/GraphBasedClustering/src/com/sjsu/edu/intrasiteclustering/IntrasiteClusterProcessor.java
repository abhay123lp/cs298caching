package com.sjsu.edu.intrasiteclustering;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sjsu.edu.commonconstants.CacheConstants;
import com.sjsu.edu.commonhelpers.IntrasiteRequestProcessor;
import com.sjsu.edu.commonhelpers.Request;
import com.sjsu.edu.commonhelpers.IntersiteRequestProcessor;
import com.sjsu.edu.graph.intrasite.IntraSiteGraph;
import com.sjsu.edu.graphintersite.InterSiteGraph;
import com.sjsu.edu.node.Node;

public class IntrasiteClusterProcessor {
	
	IntraSiteGraph graphForIntrasiteCluster = null;
	String baseDir = null;
	
	public IntrasiteClusterProcessor(String dir)
	{
		baseDir = dir;
	}
	
	//public void processIntraSiteCluster
	
	public Set<Node> processIntraSiteCluster
	(
		//List<Set<Node>> clusteredNodes,String clientIP,
		Node intraSiteNode,String clientIP,
		Double confidenceThreshold, Integer supportThreshold			
	) 
	throws IOException
	{
		String URL = null;
		String fileName = null;
		graphForIntrasiteCluster = new IntraSiteGraph();
		String logLine = null;
		File urlFile = null;
		Request request = null;
		Set<Node> intraSiteNodes = null;
		Long prevRequestTime = null;
		Long currentRequestTime = null;
		
		//for (Set<Node> set : clusteredNodes) 
		//{
			//for (Node node : set) 
			//{
		URL = intraSiteNode.getURL();
		
		fileName = baseDir + "\\" + clientIP + "\\" +  URL + "\\" + URL + ".txt";
		
		//clientIPFile = new File(filePath);
		urlFile = new File(fileName);
		
		FileInputStream fileInputStream = 
			new FileInputStream(urlFile);
		
		BufferedInputStream bufferedInputStream = 
			new BufferedInputStream(fileInputStream);
		
		DataInputStream dataInputStream = 
			new DataInputStream(bufferedInputStream);
				
		while(dataInputStream.available()!= CacheConstants.ZERO)
		{				
			logLine = dataInputStream.readLine();
		
			request = IntrasiteRequestProcessor.processRequest(logLine);
			
			if(prevRequestTime== null)
			{
				prevRequestTime = request.getRequestTime();
				currentRequestTime = request.getRequestTime();
			}
			else
			{
				currentRequestTime = request.getRequestTime();
			}
			
			if(computeTimeDifference(currentRequestTime, prevRequestTime))
			{
				graphForIntrasiteCluster.resetPrevNode();
				prevRequestTime = null;
				currentRequestTime = null;
			}
			
			graphForIntrasiteCluster.processRequest(request);
		}
		
		Integer nodePop = null;
		Collection<Node> nodes = null;
		
		if(graphForIntrasiteCluster.getNodes().size() == 1)
		{
		   	nodes = graphForIntrasiteCluster.getNodes();
		   	
		   	for (Node node : nodes) 
		   	{
				nodePop = node.getPopularity();
				
				intraSiteNodes = new HashSet<Node>();
				intraSiteNodes.add(node);
			}
		}
		else
		{
			intraSiteNodes = graphForIntrasiteCluster.processGraph(confidenceThreshold, supportThreshold);
		}
		 
		 //}
	
		 //currentRequestTime = null;
		 //prevRequestTime = null;
		 //}
		 
		 return intraSiteNodes;
	}
	
	@SuppressWarnings("deprecation")
	private boolean computeTimeDifference(Long currentReqeustTime,Long prevRequestTime)
	{
		
		Date d = new Date(currentReqeustTime);
		
		Long prevRequestTimePlus = new Long(prevRequestTime.longValue() + 
				(CacheConstants.THIRTY * 
						  CacheConstants.SIXTY * 
						  CacheConstants.THOUSAND));
		
		if(currentReqeustTime > prevRequestTimePlus)
			return true;
		else
			return false;
		
	}

}
