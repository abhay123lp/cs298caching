package com.sjsu.edu.domainprocessor;

import java.io.BufferedInputStream;


import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sjsu.edu.commonconstants.CacheConstants;
import com.sjsu.edu.commonhelpers.GarbageCollectorHelper;
import com.sjsu.edu.commonhelpers.IntrasiteRequestProcessor;
import com.sjsu.edu.commonhelpers.Request;
import com.sjsu.edu.domainprocessor.helpers.ClusterProcessor;



/**
 * 
 * The Log processor is the main component that processes the
 * unique individual log files
 * 
 * @author Administrator
 *
 */
public class LogProcessor {
	
	private String inputFileName;
	private String outputFileName;
	private String baseDir;
	private Map<String,Long> uniqueDomainNames;
	private ClusterProcessor clusterProcessor;
	
	public LogProcessor(String inputFile,String outputFile, 
			String baseDir)
	{
		this.inputFileName = inputFile;
		this.outputFileName = outputFile;
		this.baseDir = baseDir;
		uniqueDomainNames = new HashMap<String, Long>();
		clusterProcessor = new ClusterProcessor();	
	}
	
	/**
	 * Helper that initiates the Web Log File Reading Process
	 * @throws IOException 
	 */
	//public Map<String,Integer> readFile() throws IOException
	public Map<String,Long> readFile() throws IOException
	{
		File f = new File(inputFileName);
		
		FileInputStream fileInputStream = 
			new FileInputStream(f);
		
		BufferedInputStream bufferedInputStream = 
			new BufferedInputStream(fileInputStream);
		
		DataInputStream dataInputStream = 
			new DataInputStream(bufferedInputStream);
		
		 //readLines(dataInputStream);		
		
		return readLines(dataInputStream);
			
		//printStats();
		
	}
	
	//private Map<String,Integer> readLines(DataInputStream dataInputStream) throws IOException
	private Map<String,Long> readLines(DataInputStream dataInputStream) throws IOException
	{
		StringBuffer requestedItemBuffer = null;
		List<StringBuffer> sessionList = new ArrayList<StringBuffer>();
		//Set<String> uniqueObjSet = new HashSet<String>();
		Map<String,Long> uniqueObjSet = new HashMap<String,Long>();
		Request request = null;
		String logLine = null;
		Long requestTime = null;
		Long requestTimePlus = null;
		String requestedObject = null;
		//Set<String> clusteredObjects = null;
		//Map<String,Double> clusteredObjects = null;
		Map<String,Long> clusteredObjects = null;
		
		int counter = CacheConstants.ZERO;
		
		while(dataInputStream.available()!=CacheConstants.ZERO)
		{		 
		  logLine = dataInputStream.readLine();
		  
		  //request = processLine(logLine);
		  
		  request = IntrasiteRequestProcessor.processRequest(logLine);	  
		  
		  //Get the request Time for the first Line
		  requestTime = request.getRequestTime();
		  
		  if(requestTimePlus == null)
		  {
			  requestTimePlus = 
				  new Long(requestTime.longValue() + 
						  (CacheConstants.THIRTY * 
								  CacheConstants.SIXTY * 
								  CacheConstants.THOUSAND));
		  }
		  
		  if(requestTime >= requestTimePlus 
				  && counter != CacheConstants.ZERO)
		  {
			  sessionList.add(requestedItemBuffer);
			  
			  requestedItemBuffer = null;
			  
			  counter = CacheConstants.ZERO;
			  
			  requestTimePlus = new Long(requestTime.longValue() + 
					  (CacheConstants.THIRTY * 
							  CacheConstants.SIXTY * 
							  CacheConstants.THOUSAND));
		  }
		 
		  //Set the RequestTimePlus; Only if Counter is set to ZERO
		  if(counter == CacheConstants.ZERO)
		  {			    
			  requestedItemBuffer = new StringBuffer();
			  
			  requestedObject = extractRequestedObject(request.getRequestedURL());
			  
			  //uniqueObjSet.add(requestedObject);
			  uniqueObjSet.put(requestedObject,request.getRequestedItemSize());
			  
			  requestedItemBuffer.
			  			append(requestedObject);
			  
			  counter += CacheConstants.ONE;	  
		  }
		  else
		  {
			  requestedObject = extractRequestedObject(request.getRequestedURL());
			  
			  //uniqueObjSet.add(requestedObject);
			  uniqueObjSet.put(requestedObject,request.getRequestedItemSize());
			  
			  requestedItemBuffer.
			  			append("," + requestedObject);
		  }
		 
		}
		
		//Add items to each session to a SessionList
		sessionList.add(requestedItemBuffer);
		
		requestedItemBuffer = null;
		
		//Invoke the Garbage Collector
		invokeGC();
		
		
		/** If the number of user sessions is at least greater than 2 only then there is point in
		 * creating a cluster of popular objects for that domain, otherwise the if number of sessions is
		 * just 1 then there will be only one cluster with each object having probability as 1
		 * Thus, this would not be an effective clustering pre-fetching scheme */
			
		//When the above loop is done run the code to find
		//clusters
		if(sessionList.size() >=2)
		{
			clusteredObjects = clusterProcessor.computeCluster(sessionList,uniqueObjSet);
		}
		
		System.out.println("Number of Unique Objects : " + uniqueObjSet.size());
		
		printStats();
		
		return clusteredObjects;
	}
	
	/**
	 * Helper to extract Requested Object
	 */
	private String extractRequestedObject(String URL)
	{
		int index = URL.indexOf("//");
		
		URL = URL.substring(index + CacheConstants.TWO);
		
		System.out.println(URL);
		
		index = URL.indexOf("/");
		
		if(index != -1)
			URL = URL.substring(index);
		
		return URL;
	}
	
	/**
	 * Helper to process each line in the file
	 *//*
	private Request processLine(String logLine)
	{
		Request request = null;
		
		String[] fields = logLine.split(" ");
		
		request = createRequestObject(fields);
			
		return request;
	}*/
	
	
	/**
	 * Get the request time 
	 * 
	 * @param requestTimeStr
	 * @return
	 */
	private Long processRequestTime(String requestTimeStr)
	{		
		Double d = new Double(requestTimeStr);
		
		Long l = new Long((long) (d * CacheConstants.THOUSAND));
	    
		return l;
	}

	/**
	 * Helper to extract the web object from the URL 
	 * 
	 * @param rawURL
	 * @return
	 */
	private String extractWebObject(String rawURL)
	{
		return null;
	}
	
	/**
	 * Helper to process the URL
	 */
	private String processURL(String URL)
	{
		//URL : http://www.gmail.com/skd/
		URL = URL.trim();
		
		int index = URL.indexOf("//");
		int index2 = -1;
		
		//www.gmail.com/skd/
		URL = URL.substring(index + 2);

		index2 = URL.indexOf("/");
		
		if(index2>-1){
		//www.gmail.com
			URL = URL.substring(0,index2);
		}else
		{
			URL = URL.substring(0);
		}		
		
		if(URL.contains(":"))
		{
			int index3 = URL.indexOf(":");
			URL = URL.substring(0,index3);
		}
		
		if (uniqueDomainNames.containsKey(URL)) {
			
			Long count = uniqueDomainNames.get(URL);
			count+=1;
			uniqueDomainNames.put(URL, count);
			
		}else{
			
			Long count = new Long(1);
			uniqueDomainNames.put(URL, count);
		}
		
		return URL;
	}
	
	/**
	 * Create a Request Object out of the Log File Line 
	 * 
	 * @param fields
	 * @return
	 */
	private Request createRequestObject(String[] fields)
	{
		Request request = new Request();
		
		//Long requestTime = new Long(fields[0].toString());
		String requestTimeString = fields[0].trim();
		
		Long requestTime = new Long(requestTimeString);
		
		Long requestProcessingTime = new Long(fields[1]);
		String clientIPAddress = fields[2].trim();
		String tcpCode = fields[3].trim();
		Long requestedItemSize = new Long(fields[4].toString());
		String requestedObject = fields[6].trim();
		//requestParam[7] --> '-'
		String timeOutRedirectionAttribute = fields[8].trim();
		String requestedObjectType = fields[9].trim();
		
		request.setRequestTime(requestTime);
		request.setRequestProcessingTime(requestProcessingTime);
		request.setClientIPAddress(clientIPAddress);
		request.setTcpCode(tcpCode);
		request.setRequestedItemSize(requestedItemSize);
		request.setRequestedURL(requestedObject);
		request.setTimeOutRedirectionAttribute(timeOutRedirectionAttribute);
		request.setRequestedObjectType(requestedObjectType);
	
		return request;
	}
	
	/**
	 * Helper to invoke Garbage Collection
	 */
	private void invokeGC()
	{
		GarbageCollectorHelper.invokeGC();
	}
	
	
	private void printStats() throws IOException
	{
		Set<String> uniqueDomain = uniqueDomainNames.keySet();
		
		/*File outputFile = new File(outputFileName);
		FileWriter fileWriter = null;
		fileWriter = new FileWriter(outputFile);*/
			
		for (String string : uniqueDomain) {
			
			System.out.println(string);
			/*fileWriter.write(string + ",Count:" 
					+ uniqueDomainNames.get(string)+"\n");*/
		}
		
		//fileWriter.close();
		System.out.println("Unique Domain Count:" + uniqueDomain.size());
		
	}
	
}
