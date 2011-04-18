package com.sjsu.edu.graphlogfilereader;

import java.io.BufferedInputStream;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.sjsu.edu.commonconstants.CacheConstants;
import com.sjsu.edu.commonhelpers.Request;
import com.sjsu.edu.edge.RequestProcessor;
import com.sjsu.edu.filewriter.FileWriterForCluster;
import com.sjsu.edu.graphintersite.InterSiteGraph;
import com.sjsu.edu.intrasiteclustering.IntrasiteClusterProcessor;
import com.sjsu.edu.node.Node;

public class GraphLogFileReader {
	
	private String baseDir;
	
	public GraphLogFileReader(String baseDirectory)
	{
		baseDir = baseDirectory;
	}

	public void processLogFile() throws IOException, InterruptedException
	{
		String[] filesInDir = null;
		InterSiteGraph clusterGraph = null;
		String segregatedRequestDir = null;
		File f = new File(baseDir);
		File clientIPFile = null;
		String fileDir = null;
		String filePath = null;
		String logLine = null;
		List<Set<Node>> interSiteNodes = null;
		boolean clientIPstored = true;
		String clientIPAddress = null;
		FileInputStream fileInputStream = null;
		BufferedInputStream bufferedInputStream = null;
		DataInputStream dataInputStream = null;
		Request request = null;
		IntrasiteClusterProcessor processor = null;
		Double confidenceThreshold = null;
		Integer supportThreshold = null;
		Set<Node> intraSiteNodes = null;
		int clusterCounter = 0;
		FileWriterForCluster fileWriter = null;
		String baseDirForInterSite = null;
		
		baseDirForInterSite = "D:\\ClusteredWebObjects\\IntersiteClusters";
		
		filesInDir = f.list();
		clusterGraph = new InterSiteGraph();
		
		for (String string1 : filesInDir) 
		{
		
			//Demo for Inter Site Clustering
			String string = "D:\\ClientIPCustomizedLogFile\\98.241.74.53\\98.241.74.53.txt";
			//String string = "D:\\ClientIPCustomizedLogFile\\98.241.74.53\\98.241.743";
		
			//Demo for Intra Site Clustering
			//String string = "D:\\CustomizedLogFile\\3.217.149.31\\www.chesshere.com\\www.chesshere.com.txt";
		
			fileDir = string1 + "\\" + string1 + ".txt";
			
			filePath = baseDir + "\\" + fileDir;
			
			clientIPFile = new File(filePath);
			//clientIPFile = new File(string);
			
			fileInputStream = 
				new FileInputStream(clientIPFile);
			
			bufferedInputStream = 
				new BufferedInputStream(fileInputStream);
			
			dataInputStream = 
				new DataInputStream(bufferedInputStream);
			
			while(dataInputStream.available()!= CacheConstants.ZERO)
			{				
				logLine = dataInputStream.readLine();
				
				request = RequestProcessor.processRequest(logLine);
				
				if(clientIPstored)
				{
					clientIPAddress = request.getClientIPAddress();
					clientIPstored = false;
				}
				
				clusterGraph.processRequest(request);
			}
			
		  clientIPstored = true;
		
		  System.out.println("PROCESSING FOR CLIENT IP: " + clientIPAddress);
		  
		  interSiteNodes =	clusterGraph.processGraph(0.2, 2);
		  
		  if(interSiteNodes!=null)
		  {
		  
			  //Code for IntraSite Objects -- Begins
			 /* segregatedRequestDir = "D:\\CustomizedLogFile\\";
			  
			  confidenceThreshold = new Double(0.1);
			  supportThreshold = new Integer(1);
			  
			  for (Set<Node> set : interSiteNodes) 
			  {
				  for (Node node : set) 
				  {
					  processor = new IntrasiteClusterProcessor(segregatedRequestDir);
					  
					  System.out.println(node.getURL());
					  
					  System.out.println("++++ INTRASITE NODES ++++++ ");
							 
					  intraSiteNodes =  processor.processIntraSiteCluster
					  (node,clientIPAddress,
					   confidenceThreshold,supportThreshold);
					 
					 if(intraSiteNodes.size() > 0)
						 for (Node node1 : intraSiteNodes) {
							 System.out.println(node1.getURL());
						 }
					 System.out.println("++++ INTRASITE NODES END ++++++ ");
					 
				  }
			  }*/
			
			  //Code for IntraSite Objects -- Ends
			  
			  System.out.println("Inter site Cluster Begins for Client IP " + clientIPAddress);
			  System.out.println(" ------ Cluster Begins -------");
			  
			  for (Set<Node> set : interSiteNodes) 
			  {				  
				  if(set.size() > 1)
				  {
					  
					  clusterCounter=clusterCounter+1;
					  for (Node node : set) 
					  {
						  System.out.println(node.getURL());  
					  }
					  
					  fileWriter = new FileWriterForCluster(baseDirForInterSite);
					  fileWriter.writeToFile(set, clusterCounter, clientIPAddress);
					  System.out.println(" ------ Cluster ENDS -------");
				  }			
			  }	
			  
			  clientIPstored = true;
			  clusterCounter =0;
			  
		  }//
		  else
		  {
			  System.out.println("Intersite Nodes for Client IP :"  + clientIPAddress + " is null");
		  }
		  
		  System.out.println();
		  Thread.sleep(2000);
		  System.gc();
		  Thread.sleep(1000);
		}
	}
}
