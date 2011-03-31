package com.sjsu.cache.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sjsu.cache.lru.LRUCache;
import com.sjsu.edu.cache.constants.CacheConstants;
import com.sjsu.edu.printer.Printer;

public class LRULogReader {
	
	private Integer maxItemSize;
	private	String reqItem;
	private Long requestNumber;
	

	//private LRUCache<String, String> theLRUCache;
	private LRUCache theLRUCache;
	
	//public LRULogReader(Integer capacity)
	public LRULogReader(Long capacity)
	{
		//theLRUCache = new LRUCache<String, String>(capacity);	
		theLRUCache = new LRUCache(capacity);
		maxItemSize = new Integer(0);
		reqItem = null;
		requestNumber = new Long(0);
	}
	
	public Integer getMaxItemSize() {
		return maxItemSize;
	}

	public void executeRequest(String fileName,String outputFileName)
	{
		readLogFile(fileName,outputFileName);
	}

	@SuppressWarnings({ "deprecation"})
	private void readLogFile(String inputFile,String outputFile)
	{
		
		  Long dataViolatingSize = new Long(0);
		  
		  Integer numberOfItemsViolatingSize = new Integer(0);
		
		  //Create File from which to read
		  File fileInput = new File(inputFile);
		  
		  //Create File to which to write
		  File fileOutput = new File(new String(outputFile));
		  
		  //The FileOutputStream 
		  FileOutputStream fos = null;
		  
		  //The FileInputStream
		  FileInputStream fis = null;
		
		  //Number of Requests
		  long noOfRequests = CacheConstants.ZERO;
		  
		  //Runtime Object
		  Runtime r = Runtime.getRuntime();
		  
		  try{
			  
			  //Make sure the File exists - To prevent File not found exception
			  if(fileInput.exists()){
				 
				  //FileInputStream
				  fis = new FileInputStream(fileInput);
				  
				  //FileOutputStream
				  fos = new FileOutputStream(fileOutput);
				  
				  //BufferedOutputStream
				  BufferedOutputStream bos = new BufferedOutputStream(fos);
				  
				  //BufferedInputStream
				  BufferedInputStream bis = new BufferedInputStream(fis);
				  
				  //DataOutputStream
				  DataOutputStream dos = new DataOutputStream(bos);
				  
				  //DataInputStream
				  DataInputStream dis = new DataInputStream(bis);
					 				    
				  while(dis.available()!=CacheConstants.ZERO)
				 // while(noOfRequests < 100)
				  {					  
					//Read a line from the Web Log File  
					String line = dis.readLine();
					
					//Pre-process the request
					Request request = preProcessRequest(line);
					
					if(maxItemSize < request.requestedItemSize)
					{
						maxItemSize = request.requestedItemSize;
						reqItem = request.getRequestedObject();
						requestNumber = noOfRequests + 1;
					}
					
					if(!(request.requestedItemSize > theLRUCache.getMaxCacheSize()))
					{
						//LRU Cache
						theLRUCache.cacheItem(request);
						
						//Increment the Total Number of requests
						noOfRequests +=CacheConstants.ONE;
					}
					else
					{
						dataViolatingSize = dataViolatingSize +  new Long(request.requestedItemSize.toString());
						numberOfItemsViolatingSize = numberOfItemsViolatingSize + 1;
					}
					
					//Once the item is pushed to the Cache set the handle to null
					request = null;
					
					/*//Increment the Total Number of requests
					noOfRequests +=CacheConstants.ONE;*/
					
					//Every 25000 request clear out the JVM by trying to run GARBAGE Collector
					//if(noOfRequests%CacheConstants.TWENTYFIVETHOUSHAND
					if(noOfRequests%CacheConstants.TENTHOUSAND
							==CacheConstants.ZERO)
					{
						try 
						{
							//Invoke GARBAGE Collector
							r.gc();
							
							Printer.printString("Putting the thread to sleep,Current Cache Size :" 
									+ theLRUCache.getCurrentCacheSize() + ", Current Cache Map Size :" + theLRUCache.getCacheMapSize()
									+ ",Total Number of Items Scanned :" + theLRUCache.getTotalNumberOfItems() + "Cache Size:" + theLRUCache.getCurrentCacheSize());
							
							//Put the Thread to sleep
							Thread.sleep(CacheConstants.TWOTHOUSAND);
							
							//Invoke GARBAGE Collector
							r.gc();
							
						}
						catch (InterruptedException e) 
						{
							e.printStackTrace();
						}	
					}
					
				  }
				  
				  printLRUStats();
				  
				  System.out.println("Number of Items violating Size :" + numberOfItemsViolatingSize);
				  System.out.println("Size of Items violationg Size : " + dataViolatingSize );
				  //Attempt a write to file
				  //dos.writeBytes("Total Hits : " + theLRUCache.getCacheHits());	  
			  }		
		  
		  } 
		  catch (IOException e) 
		  {
			e.printStackTrace();
		  }	
		  finally
		  {
			try 
			{
				fis.close();
				fos.close();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		 }
		  
		  System.out.println("Max Item Size :" + maxItemSize + 
				  ",Request Number:" + this.requestNumber + ",Requested Item:" + this.reqItem);
	}
	
	private void printLRUStats()
	{	
		//Once the Complete Processing of all Web Log Entries is done - Print out all the details
		System.out.println("Total Number of Items : "  + theLRUCache.getTotalNumberOfItems());
		System.out.println("Total Misses : " + theLRUCache.getCacheMiss());
		System.out.println("Total Hits : " + theLRUCache.getCacheHits());
		System.out.println("Total CacheSize : " + theLRUCache.getTotalRequestedItemSize());
		System.out.println("Cache Map Size : "  + theLRUCache.getCacheMapSize());
		System.out.println("Total Cache Hit Size : " + theLRUCache.getTotalCacheHitSize());
		System.out.println("Total Cache Miss Size : " + theLRUCache.getTotalCacheMissSize());
		
	}
	
	private Request preProcessRequest(String request)
	{
		String[] requestParam = request.split(" ");
		//int i=0;
		Request request2 = new Request();
			
		/*Long requestTime = new Long(requestParam[0].toString());*/
		Float requestTime = new Float(requestParam[0].toString());
		Long requestProcessingTime = new Long(requestParam[1]);
		String clientIPAddress = requestParam[2].trim();
		String tcpCode = requestParam[3].trim();
		Integer requestedItemSize = new Integer(requestParam[4]);
		String requestedObject = requestParam[6].trim();
		String timeOutRedirectionAttribute = requestParam[8].trim();
		String requestedObjectType = requestParam[9].trim();
		
		request2.requestTime = requestTime;
		request2.requestProcessingTime = requestProcessingTime;
		request2.clientIPAddress = clientIPAddress;
		request2.tcpCode = tcpCode;	
		request2.requestedItemSize = requestedItemSize;
		request2.setRequestedObject(requestedObject);
		request2.timeOutRedirectionAttribute = timeOutRedirectionAttribute;
		request2.requestedObjectType = requestedObjectType;	
	
		requestParam = null;
		
		return request2;
	}
	
}
