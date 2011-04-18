package com.sjsu.cache.lru.test;

import junk.LRULogReader;


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
		
		/*String inputFile = "D:\\CustomizedLogFile\\157.91.28.89\\157.91.28.89.txt";
		
		LRULogReader theLRULogReader = new LRULogReader(new Long(1307182));*/
		
		/*//Main Testing Content 2
		String inputFile =
			"E:\\CentralServerLogDump\\uc.sanitized-access.20070110\\uc.sanitized-access.20070110";
		
		LRULogReader theLRULogReader = new LRULogReader(new Long(238922354));*/
		
		//Main Testing Content - That would throw error on LFU
		String inputFile = "E:\\CentralServerLogDump\\rtp.sanitized-access.20070110\\" +
							"processed.rtp.sanitized-access.20070110";
				//"processed.rtp.sanitized-access.20070110.txt";
			
			/*"E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\" +
			"processed.sv.sanitized-access.20070109.txt";*/

		
		LRULogReader theLRULogReader = new LRULogReader(new Long(788127966));
		
		/*LRULogReader theLRULogReader = new LRULogReader(new Long(77875413));*/
		
		/*String inputFile = "D:\\CustomizedLogFile\\2jz.org\\2jz.org.txt";*/
		
		String outputFile = new String("E:\\CentralServerLogDump\\results2.txt");
		
		//LRULogReader theLRULogReader = new LRULogReader(CacheConstants.Seven916);
		
		theLRULogReader.executeRequest(inputFile,outputFile);
		
		//System.out.println("Max Item Size: " + theLRULogReader.getMaxItemSize().toString());
		
	}

}
