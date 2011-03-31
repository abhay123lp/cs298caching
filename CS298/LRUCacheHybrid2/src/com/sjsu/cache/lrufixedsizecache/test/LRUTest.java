package com.sjsu.cache.lrufixedsizecache.test;

import com.sjsu.edu.cache.constants.CacheConstants;
import com.sjsu.fixedsizecache.helper.LRULogReader;

public class LRUTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
		/*String inputFile =
			new String("E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\abcd.txt");*/
		
		
		/*String inputFile =
			new String("E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\" +
			"HundredRequests.txt");*/
		
		
			/*new String("E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\" +
			"abcd.txt");*/
		
		//Main Testing Content
		/*String inputFile =
			new String("E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\" +
			"sv.sanitized-access.20070109");*/
		
		String outputFile = new String("E:\\CentralServerLogDump\\results2.txt");
		
		//LRULogReader theLRULogReader = new LRULogReader(CacheConstants.Seven916);
		//LRULogReader theLRULogReader = new LRULogReader(CacheConstants.ONE);
		//
		String inputFile = "D:\\CustomizedLogFile\\157.91.28.89\\157.91.28.89.txt";
		LRULogReader theLRULogReader = new LRULogReader(82);
		
		theLRULogReader.executeRequest(inputFile,outputFile);
		
	}

}
