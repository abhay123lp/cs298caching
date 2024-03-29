package com.sjsu.edu.cachelfu;

import java.io.BufferedInputStream;


import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.sjsu.edu.commonconstants.CacheConstants;
import com.sjsu.edu.commonhelpers.Request;
import com.sjsu.edu.commonhelpers.IntersiteRequestProcessor;
import com.sjsu.edu.printer.Printer;

public class CacheLFUReader {

	private CacheLFU theLFUCache;

	//public CacheLFUReader(Integer capacity) {
	public CacheLFUReader(Long capacity) {
		// theLFUCache = new CacheLFU(capacity);
		theLFUCache = new CacheLFU(capacity);
	}

	public void executeRequest(String fileName, String outputFileName) {
		readLogFile(fileName, outputFileName);
	}

	@SuppressWarnings({ "deprecation" })
	private void readLogFile(String inputFile, String outputFile) {
		// Create File from which to read
		File fileInput = new File(inputFile);

		// Create File to which to write
		File fileOutput = new File(new String(outputFile));

		// The FileOutputStream
		FileOutputStream fos = null;

		// The FileInputStream
		FileInputStream fis = null;

		// Number of Requests
		long noOfRequests = CacheConstants.ZERO;

		// Runtime Object
		Runtime r = Runtime.getRuntime();

		try {

			// Make sure the File exists - To prevent File not found exception
			if (fileInput.exists()) {

				// FileInputStream
				fis = new FileInputStream(fileInput);

				// FileOutputStream
				fos = new FileOutputStream(fileOutput);

				// BufferedOutputStream
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				// BufferedInputStream
				BufferedInputStream bis = new BufferedInputStream(fis);

				// DataOutputStream
				DataOutputStream dos = new DataOutputStream(bos);

				// DataInputStream
				DataInputStream dis = new DataInputStream(bis);
				
				Long requestedObjectSize = null;

				while (dis.available() != CacheConstants.ZERO)
				// while(noOfRequests < 100)
				{
					// Read a line from the Web Log File
					String line = dis.readLine();

					// Pre-process the request
					//Request request = preProcessRequest(line);
					Request request = IntersiteRequestProcessor.processRequest(line);

					// Push the Object to the Cache

					// LRU Cache
					// theLRUCache.cacheItem(request);

					/*** *********************/
					// Make sure the Item to be Cached is no bigger than
					// the Cache itself
					/*** *********************/
					
					requestedObjectSize = request.getRequestedItemSize();

					//if (request.requestedItemSize < theLFUCache.getMaxCacheSize())
					if (!(requestedObjectSize > theLFUCache.getMaxCacheSize()))
					{
						// LFU Cache
						theLFUCache.cacheItem(request);

						// Once the item is pushed to the Cache set the handle to
						// null
						request = null;

						// Increment the Total Number of requests
						noOfRequests += 1;
						// System.out.println(noOfRequests);
					}
					
					// Every 25000 request clear out the JVM by trying to run
					// GARBAGE Collector
					if (noOfRequests % CacheConstants.TWENTYFIVETHOUSHAND == CacheConstants.ZERO) 
					{
					
						try {
							// Invoke GARBAGE Collector
							r.gc();

							Printer.printString("Putting the thread to sleep:" + noOfRequests 
									+ ",Current Cache Size:" + theLFUCache.getCurrentCacheSize() + ", CacheMapSize:" + theLFUCache.getCacheMapSize());

							// Put the Thread to sleep
							Thread.sleep(CacheConstants.TWOTHOUSAND);

							// Invoke GARBAGE Collector
							r.gc();

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				printLFUStats();
				// Attempt a write to file
				// dos.writeBytes("Total Hits : " + theLRUCache.getCacheHits());

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void printLFUStats() {
		// Once the Complete Processing of all Web Log Entries is done - Print
		// out all the details

		System.out.println("Total Number of Items : "
				+ theLFUCache.getTotalNumberOfItems());
		System.out.println("Total Misses : " + theLFUCache.getCacheMisses());
		System.out.println("Total Hits : " + theLFUCache.getCacheHits());
		System.out.println("Total CacheSize : "
				+ theLFUCache.getTotalRequestedItemSize());
		System.out.println("Cache Map Size : " + theLFUCache.getCacheMapSize());
		System.out.println("Total Cache Hit Size : "
				+ theLFUCache.getTotalCacheHitByteSize());
		System.out.println("Total Cache Miss Size : "
				+ theLFUCache.getTotalCacheMissByteSize());
		Date currentDate = new Date(System.currentTimeMillis());
		System.out.println(" End TimeStamp " + currentDate.getMonth()
				+ currentDate.getDate() + "," + currentDate.getYear()
				+ currentDate.getHours() + ":" + currentDate.getMinutes() + ":"
				+ currentDate.getSeconds());

	}

	/*
	private Request preProcessRequest(String requestString) 
	{
		String[] requestParam = requestString.split(" ");
		// int i=0;
		Request request = new Request();

		 Long requestTime = new Long(requestParam[0].toString()); 
		String requestedItemTimeString = requestParam[0].toString().trim();
		Long requestTime = new Long(requestedItemTimeString);
		Long requestProcessingTime = new Long(requestParam[1]);
		String clientIPAddress = requestParam[2].trim();
		String tcpCode = requestParam[3].trim();
		String requestedItemSizeString = requestParam[4].toString().trim();
		Long requestedItemSize = new Long(requestedItemSizeString);
		String requestedObject = requestParam[6].trim();
		// requestParam[7] --> '-'
		String timeOutRedirectionAttribute = requestParam[8].trim();
		String requestedObjectType = requestParam[9].trim();

		request.setRequestTime(requestTime);
		request.setRequestProcessingTime(requestProcessingTime);
		request.setClientIPAddress(clientIPAddress);
		request.setTcpCode(tcpCode);
		request.setRequestedItemSize(requestedItemSize);
		request.setRequestedObject(requestedObject);
		request.setTimeOutRedirectionAttribute(timeOutRedirectionAttribute);
		request.setRequestedObjectType(requestedObjectType);

		requestParam = null;

		return request;
		 theLRUCache.addToRequests(request2); 
	}
	*/
}
