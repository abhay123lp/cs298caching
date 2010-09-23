package com.sjsu.cache.lru;

import com.sjsu.cache.helper.LRULogReader2;
import com.sjsu.edu.cache.constants.CacheConstants;

public class LRUTest2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String inputFile = 
			new String("E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\" +
			"sv.sanitized-access.20070109");
		String outputFile = new String("E:\\CentralServerLogDump\\results2.txt");
		LRULogReader2 theLRULogReader = new LRULogReader2(CacheConstants.Seven916);
		theLRULogReader.executeRequest(inputFile,outputFile);		
	}

}
