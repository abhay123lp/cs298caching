package com.sjsu.fixedsizecache.helper;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sjsu.cache.lrufixedcachesizeimpl.LRUCache;
import com.sjsu.edu.cache.constants.CacheConstants;
import com.sjsu.edu.printer.Printer;

public class LRULogReader {

	private LRUCache<String, String> theLRUCache;
	private Map<String,Long> clusteredObjects;
	
	public LRULogReader(Integer capacity)
	{
		theLRUCache = new LRUCache<String, String>(capacity);
		clusteredObjects = new HashMap<String,Long>();
	}
	
	public void executeRequest(String fileName,String outputFileName)
	{
		computeCluster();
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
			  if(fileInput.exists())
			  { 
				  //FileInputStream
				  fis = new FileInputStream(fileInput);
				  
				  //FileOutputStream
				  fos = new FileOutputStream(fileOutput);
				  
				  //BufferedOutputStream
				  BufferedOutputStream bos = new BufferedOutputStream(fos);
				  
				  //BufferedInputStream
				  BufferedInputStream bis = new BufferedInputStream(fis);
				  
				  //DataOutputStreamk
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
					
					if(clusteredObjects.containsKey(request.getRequestedObject()) 
							&& !theLRUCache.contains(request.getRequestedObject()))
					{
						Set<String> clusteredObjectKeys = clusteredObjects.keySet();
						
						for (String string : clusteredObjectKeys) {
							Long size = clusteredObjects.get(string);
							theLRUCache.editCache(string, size);
						}
						
						theLRUCache.cacheItem(request);
					}
					else
					{
						//LRU Cache
						theLRUCache.cacheItem(request);
					}
					
					//Once the item is pushed to the Cache set the handle to null
					request = null;
					
					//Increment the Total Number of requests
					noOfRequests +=CacheConstants.ONE;
					
					//Every 25000 request clear out the JVM by trying to run GARBAGE Collector
					if(noOfRequests%CacheConstants.TWENTYFIVETHOUSHAND==CacheConstants.ZERO)
					{
						try 
						{
							//Invoke GARBAGE Collector
							r.gc();
							
							Printer.printString("Putting the thread to sleep");
							
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
	
	private void computeCluster()
	{
		clusteredObjects.put("http://157.91.28.89/study_images.js",new Long(225));
		clusteredObjects.put("http://157.91.28.89/images/ui/minus.gif",new Long(226));
		clusteredObjects.put("http://157.91.28.89/navbar.html", new Long (221));
		clusteredObjects.put("http://157.91.28.89/images/ui/incon_keyOff.gif",new Long(226));
		clusteredObjects.put("http://157.91.28.89/public/presets.js", new Long(226));
		clusteredObjects.put("http://157.91.28.89/public/getids.js",new Long(226));
		clusteredObjects.put("http://157.91.28.89/navigation.js",new Long(226));
		clusteredObjects.put("http://157.91.28.89/public/styles.html", new Long(210));
		clusteredObjects.put("http://157.91.28.89/images/ui/incon_reportOn.gif", new Long(226));
		clusteredObjects.put("http://157.91.28.89/images/ui/incon_keyOn.gif", new Long(226));
		clusteredObjects.put("http://157.91.28.89/images/exam_list_report.gif", new Long(225));
		clusteredObjects.put("http://157.91.28.89/escapefilename.js", new Long(226));
		clusteredObjects.put("http://157.91.28.89/images/ui/incon_studyOff.gif", new Long(226));
		clusteredObjects.put("http://157.91.28.89/public/install_plugin.js", new Long(226));
		clusteredObjects.put("http://157.91.28.89/timeout.js", new Long(226));
		clusteredObjects.put("http://157.91.28.89/checkocxversion.js", new Long(226));
		clusteredObjects.put("http://157.91.28.89/public/styles2.html", new Long(211));
		clusteredObjects.put("http://157.91.28.89/images/ui/incon_reportOff.gif", new Long(226));
		clusteredObjects.put("http://157.91.28.89/public/ie_image.js",new Long(225));
		clusteredObjects.put("http://157.91.28.89/images/ui/down.gif", new Long(226));
		clusteredObjects.put("http://157.91.28.89/public/getarg.js", new Long(226));
		clusteredObjects.put("http://157.91.28.89/callback.js", new Long(225));
		clusteredObjects.put("http://157.91.28.89/tools.js",new Long(225));
		clusteredObjects.put("http://157.91.28.89/public/login.html", new Long(212));
		clusteredObjects.put("http://157.91.28.89/public/help.js",new Long(226));
		clusteredObjects.put("http://157.91.28.89/group.js",new Long(226));
		clusteredObjects.put("http://157.91.28.89/oncall.js",new Long(225));
		clusteredObjects.put("http://157.91.28.89/study.js",new Long(226));
		clusteredObjects.put("http://157.91.28.89/butterfly.js",new Long(226));
		clusteredObjects.put("http://157.91.28.89/images/exam_list_FIText.gif",new Long(225));
		clusteredObjects.put("http://157.91.28.89/images/ui/plus.gif", new Long(226));
		clusteredObjects.put("http://157.91.28.89/images/ui/go1.gif", new Long(226));
		clusteredObjects.put("http://157.91.28.89/public/logout.js", new Long(225));
		clusteredObjects.put("http://157.91.28.89/images/exam_list_mic.gif",new Long(225));
		clusteredObjects.put("http://157.91.28.89/search.js",new Long(227));
		clusteredObjects.put("http://157.91.28.89/imagenav.js",new Long(225));
		clusteredObjects.put("http://157.91.28.89/black.html",new Long(212));
		clusteredObjects.put("http://157.91.28.89/warning_text.js",new Long(226));
		clusteredObjects.put("http://157.91.28.89/public/study_tools.js",new Long(226));
		clusteredObjects.put("http://157.91.28.89/images/ui/up.gif",new Long(226));
		clusteredObjects.put("http://157.91.28.89/mye.js",new Long(226));
		clusteredObjects.put("http://157.91.28.89/public/version.js",new Long(226));
		clusteredObjects.put("http://157.91.28.89/images/ui/incon_studyOn.gif",new Long(226));

	}
	
}
