package com.sjsu.cache.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sjsu.cache.lfu.LFUCache;
import com.sjsu.cache.lru.LRUCache;
import com.sjsu.edu.cache.constants.CacheConstants;
import com.sjsu.edu.printer.Printer;

public class LRULogReader {
	
	private LRUCache<String, String> theLRUCache;
	private LFUCache theLFUCache;
	
	public LRULogReader(Integer capacity)
	{
		//theLRUCache = new LRUCache<String, String>(CacheConstants.TWENTYTHOUSAND95);
		//theLRUCache = new LRUCache<String, String>(CacheConstants.TWENTYTHOUSAND596);
		//theLFUCache= new LFUCache(CacheConstants.TWENTYTHOUSAND596);
		theLRUCache = new LRUCache<String, String>(capacity);
		
		//theLRUCache = new LRUCache<String, String>(CacheConstants.TWOTHOUSANDSIXHUNDREDNINTY);
		//theLFUCache = new LFUCache<String, String>(CacheConstants.TWOTHOUSANDSIXHUNDREDNINTY);
		//theLFUCache = new LFUCache(15);
		
	}
	
	public void executeRequest(String fileName,String outputFileName)
	{
		readLogFile(fileName,outputFileName);
	}

	@SuppressWarnings({ "deprecation"})
	private void readLogFile(String inputFile,String outputFile)
	{
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
					
					//Push the Object to the Cache
					
					//LRU Cache
					theLRUCache.cacheItem(request);
					
					//LFU Cache
					//theLFUCache.cacheItem(request);

					//Once the item is pushed to the Cache set the handle to null
					request = null;
					
					//Increment the Total Number of requests
					noOfRequests +=1;
					//System.out.println(noOfRequests);
					
					//Every 25000 request clear out the JVM by trying to run GARBAGE Collector
					if(noOfRequests%CacheConstants.TWENTYFIVETHOUSHAND==CacheConstants.ZERO)
					{
						try {
							//Invoke GARBAGE Collector
							r.gc();
							
							Printer.printString("Putting the thread to sleep");
							
							//Put the Thread to sleep
							Thread.sleep(CacheConstants.TWOTHOUSAND);
							
							//Invoke GARBAGE Collector
							r.gc();
							
						}catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}
				  }
				  
				  printLRUStats();
				  //Attempt a write to file
				  //dos.writeBytes("Total Hits : " + theLRUCache.getCacheHits());
				  
			  }		
		  
		  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }	finally{
			  try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
		}
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
	
	private void printLFUStats()
	{
		   //Once the Complete Processing of all Web Log Entries is done - Print out all the details
		
		  System.out.println("Total Number of Items : "  + theLFUCache.getTotalNumberOfItems());
		  System.out.println("Total Misses : " + theLFUCache.getCacheMisses());
		  System.out.println("Total Hits : " + theLFUCache.getCacheHits());
		  System.out.println("Total CacheSize : " + theLFUCache.getTotalRequestedItemSize());
		  System.out.println("Cache Map Size : "  + theLFUCache.getCacheMapSize());
		  System.out.println("Total Cache Hit Size : " + theLFUCache.getTotalCacheHitByteSize());
		  System.out.println("Total Cache Miss Size : " + theLFUCache.getTotalCacheMissByteSize());
		 
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
		//requestParam[7] --> '-'
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
		/*theLRUCache.addToRequests(request2);*/
	}
}

