package com.sjsu.edu.logpreprocessor;

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

/**
 * 
 * The component reads each line in the Proxy Server Log file
 * and determines if the requested object is 
 * <br/> 1) image  - .png/.jpeg/.jpg/.gif/
 * <br/> 2) cascading style sheet css - .css
 * <br/> 3) javascript js - .js
 * <br/> 4) cgi - .cgi
 * <br/> 5) contains a query - '?'
 * 
 * and is thrown out of the file as such objects are 
 * useful for web usage pattern analysis
 * 
 * @author Administrator
 *
 */

public class LogPreprocessor {
	
	private String inputFileName;
	private String outputFileName;
	private String baseDir;
	private Map<String,Long> uniqueClientIP;
	private Map<String,Long> uniqueDomainNames;
	private String datasetDetailsFileName;
	
	public LogPreprocessor()
	{
		uniqueClientIP = new HashMap<String, Long>();
		uniqueDomainNames = new HashMap<String, Long>();
		inputFileName = null;
		outputFileName = null;
		baseDir = null;
		datasetDetailsFileName = null;
	}
	
	/**
	 * Helper to Initialize File name
	 * 
	 * @param inputFileName
	 */
	public void initializeFileName(String inputFileName,
			String outputFileName,String baseDir,String datasetDetailsFileName)
	{
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.baseDir = baseDir;
		this.datasetDetailsFileName = datasetDetailsFileName;
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
			  
			  String customizedURL = request.getRequestedURL().toLowerCase().trim();
			  String customizedRequestType = request.getRequestedObjectType().trim();
			  
			  /**
			   * If the URL request is for a css,js,png,jpeg,jpg,png,gif
			   * invalidate the request
			   */
			  		//contains .gif or .jpeg
			  		//contains .css or .js
			  		//contains .jpg or .png	
			  		//contains .ico
			  if( !customizedURL.contains(CacheConstants.gif) 
				  && !customizedURL.contains(CacheConstants.jpeg)
				  && !customizedURL.contains(CacheConstants.jpg) 
				  && !customizedURL.contains(CacheConstants.png)				 
				  && !customizedURL.contains(CacheConstants.css) 
				  && !customizedURL.contains(CacheConstants.js)
				  && !customizedURL.contains(CacheConstants.ico)
				  && !customizedURL.contains("?") 
				  && !customizedURL.contains("cgi")
				  && !customizedRequestType.contains("javascript")
				  && !customizedRequestType.contains("image")
				  && !customizedRequestType.contains("video")
				  && !customizedRequestType.contains("xml")
				  && !customizedRequestType.contains("audio"))
			  {

				  //** Write the Line to appropriate Domain File*//*
				  writeLine(logLine, URL);
				  
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
		
		String[] fields = logLine.split(" ");
		
		request = createRequestObject(fields);
		
		//Process the ClientIP
		processClientIP(request.getClientIPAddress());
		
		return request;
		
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
	private void writeLine(String line,String URL) throws IOException
	{
		String baseDirectory = baseDir;
		String pathName = null;
		String pathDir = null;
		FileWriter fileWriter = null;
		File outputFile = null;
		File outputDir = null;
		File baseParentDir = null;
		
		//pathName = baseDir + "\\" + URL + "\\" + URL + ".txt";
		pathName = this.outputFileName;
		pathDir = baseDir + "\\" + URL;

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
	 */
	private Request createRequestObject(String[] fields)
	{
		Request request = new Request();
		
		//Long requestTime = new Long(fields[0].toString());
		String requestTime = fields[0];
		Long requestProcessingTime = new Long(fields[1]);
		String clientIPAddress = fields[2].trim();
		String tcpCode = fields[3].trim();
		Integer requestedItemSize = new Integer(fields[4]);
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
	
	private void printStats() throws IOException
	{
		Set<String> uniqueDomain = uniqueDomainNames.keySet();
		
		File outputFile = new File(datasetDetailsFileName);
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

