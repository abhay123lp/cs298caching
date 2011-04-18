package com.sjsu.edu.helpers.logfilereader.clientIPURLbasedSplitter;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sjsu.edu.commonconstants.CacheConstants;
import com.sjsu.edu.commonhelpers.Request;
import com.sjsu.edu.commonhelpers.IntersiteRequestProcessor;


public class WebLogClientIPURLSplitter {
	
	String inputFileName;
	String outputFileName;
	String baseDir;
	Map<String,Long> uniqueClientIP;
	Map<String,Long> uniqueDomainNames;
	
	public WebLogClientIPURLSplitter()
	{
		uniqueClientIP = new HashMap<String, Long>();
		uniqueDomainNames = new HashMap<String, Long>();
		inputFileName = null;
		outputFileName = null;
		baseDir = null;
	}
	
	/**
	 * Helper to Initialize File name
	 * 
	 * @param inputFileName
	 */
	public void initializeFileName(String inputFileName,
			String outputFileName,String baseDir)
	{
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.baseDir = baseDir;
	}
	
	/**
	 * Helper that initiates the Web Log File Reading Process
	 * @throws IOException 
	 */
	public void readFile() throws IOException
	{
		int totalNoOfRequsts = CacheConstants.ZERO;
		File f = new File(inputFileName);
		
		FileInputStream fileInputStream = 
			new FileInputStream(f);
		
		BufferedInputStream bufferedInputStream = 
			new BufferedInputStream(fileInputStream);
		
		DataInputStream dataInputStream = 
			new DataInputStream(bufferedInputStream);
		
			while(dataInputStream.available()!=CacheConstants.ZERO)
			{
			  String logLine = dataInputStream.readLine();
			  
			  /** Read a Line in the Log File */
			  Request request = readLine(logLine);
			  
			  String URL = request.getRequestedURL();
		
			  if(!URL.contains("?"))
			  {
				  /** Write the Line to appropriate Domain File*/
				  writeLine(logLine, URL,request);
				  
				  totalNoOfRequsts+=CacheConstants.ONE;
				  
				  System.out.println("Number of Request:" 
						  + totalNoOfRequsts);
				  
				  //+++++++++++++++++++++++++++++++
				  //Invoke Garbage Collection
				  //+++++++++++++++++++++++++++++++
				  if(totalNoOfRequsts%CacheConstants.FIFTYTHOUSHAND 
						  == CacheConstants.ZERO)
				  {
					  Runtime rc = Runtime.getRuntime();
					  
					  rc.gc();
					  
					  try 
					  {
						Thread.sleep(CacheConstants.THOUSAND);
					  }
					  catch (InterruptedException e) 
					  {
						e.printStackTrace();
					  }
					  
					  rc.gc();
				  }
				//+++++++++++++++++++++++++++++++
				//Invoke Garbage Collection
				//+++++++++++++++++++++++++++++++
			  }
			  else
			  {
				  System.out.println("URL Contains ?" + URL);
			  }
		}
		
			printStats();
			
	}
	
	/**
	 * Helper to read line in the file
	 */
	private Request readLine(String logLine)
	{
		logLine = logLine.trim();
		
		Request request = processLine(logLine);
		
		return request;
	}
	
	/**
	 * Helper to process each line in the file
	 */
	private Request processLine(String logLine)
	{
		Request request = null;
		
		//String[] fields = logLine.split(" ");
		//request = createRequestObject(fields);
		
		request = IntersiteRequestProcessor.processRequest(logLine);
		
		//Process the URL
		String URL = processURL(request.getRequestedURL());
		
		request.setRequestedURL(URL);
		
		//Process the ClientIP
		processClientIP(request.getClientIPAddress());
		
		return request;
		
	}
	
	/**
	 * Helper to process the URL
	 */
	private String processURL(String URL)
	{
		/*//URL : http://www.gmail.com/skd/
		URL = URL.trim();
		
		int index = URL.indexOf("//");
		int index2 = -1;
		
		//www.gmail.com/skd/
		URL = URL.substring(index + CacheConstants.TWO);

		index2 = URL.indexOf("/");
		
		if(index2>-1)
		{
			URL = 
				URL.substring(CacheConstants.ZERO,index2);
		}
		else
		{
			URL = 
				URL.substring(CacheConstants.ZERO);
		}		
		
		if(URL.contains(":"))
		{
			int index3 = URL.indexOf(":");
			
			URL = 
				URL.substring(CacheConstants.ZERO,index3);
		}
*/		
		if (uniqueDomainNames.containsKey(URL)) {
			
			Long count = uniqueDomainNames.get(URL);
			count+=1;
			uniqueDomainNames.put(URL, count);
			
		}else{
			
			Long count = new Long(CacheConstants.ONE);
			uniqueDomainNames.put(URL, count);
		}
		
		return URL;
	}
	
	/**
	 * Helper to process the Client IP
	 */
	private void processClientIP(String clientIP)
	{
		clientIP = clientIP.trim();
		
		if(uniqueClientIP.containsKey(clientIP))
		{
			Long count = uniqueClientIP.get(clientIP);
			count += CacheConstants.ONE;
			uniqueClientIP.put(clientIP, count);	
		}
		else
		{
			Long count = new Long(CacheConstants.ONE);
			uniqueClientIP.put(clientIP, count);
		}
	}
	
	/**
	 * Helper to write Line
	 * 
	 * @param line
	 * @throws IOException 
	 */
	private void writeLine(String line,String URL,Request request) throws IOException
	{
		String pathName = null;
		String pathDir = null;
		FileWriter fileWriter = null;
		File outputFile = null;
		File outputDir = null;
		File baseParentDir = null;
		
		/*pathName = baseDir + "\\" + URL + "\\" + URL + ".txt";
		pathDir = baseDir + "\\" + URL;*/
		
		pathName = baseDir + "\\" + request.getClientIPAddress().trim() + "\\" 
					+ request.getClientIPAddress() + "\\" + request.getRequestedURL() + ".txt";
		
		pathDir = baseDir + "\\" + request.getClientIPAddress().trim()
					+ "\\" + request.getClientIPAddress();

		baseParentDir = new File(baseDir);
		
		if(!baseParentDir.exists())
			baseParentDir.mkdir();
			
		outputDir = new File(pathDir);
		
		if(!outputDir.exists())
			outputDir.mkdir();
		
		outputFile = new File(pathName);
		fileWriter = new FileWriter(outputFile,true);
		
		if(outputFile.exists())
		{	
			fileWriter.append(line + "\n");
		}
		else
		{
			fileWriter.write(line + "\n");
		}
		
		fileWriter.flush();
		fileWriter.close();
	}
	
	/**
	 * Create a Request Object out of the Log File Line 
	 * 
	 * @param fields
	 * @return
	 *//*
	private Request createRequestObject(String[] fields)
	{
		Request request = new Request();
		
		//Request Time
		String requestTimeString = fields[0].trim();
		
		Long requestTime = new Long(requestTimeString);
		
		//Request Processing Time
		Long requestProcessingTime = new Long(fields[1].trim());
		
		//Get the Client IP Address
		String clientIPAddress = fields[2].trim();
		
		//TCP Code
		String tcpCode = fields[3].trim();
		
		//Requested Item Size
		Long requestedItemSize = new Long(fields[4].trim());
		
		//Requested Web Page
		String requestedObject = fields[6].trim();

		//Time OUT Redirection Attribute
		String timeOutRedirectionAttribute = fields[8].trim();
		
		//Requested object type
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
	}*/
	
	private void printStats() throws IOException
	{
		Set<String> uniqueDomain = uniqueDomainNames.keySet();
		
		File outputFile = new File(outputFileName);
		FileWriter fileWriter = null;
		fileWriter = new FileWriter(outputFile);
			
		for (String string : uniqueDomain) {
			
			System.out.println(string);
			fileWriter.write(string + ",Count:" 
					+ uniqueDomainNames.get(string)+"\n");
		}
		
		Set<String> uniqueUserGroups = uniqueClientIP.keySet();
		
		fileWriter.write("-------------------------- \n");
		fileWriter.write("Unique User Groups \n");
		fileWriter.write("-------------------------- \n");
		
		for (String string : uniqueUserGroups) {
			System.out.println(string);
			fileWriter.write(string + ",Count:" 
					+ uniqueClientIP.get(string)+"\n");
		}
		
		fileWriter.close();
		System.out.println("Unique Domain Count:" + uniqueDomain.size());
		
	}
}

