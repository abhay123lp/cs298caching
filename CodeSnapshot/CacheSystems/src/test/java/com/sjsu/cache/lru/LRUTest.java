package com.sjsu.cache.lru;

import com.sjsu.cache.helper.LRULogReader;
import com.sjsu.edu.cache.constants.CacheConstants;

public class LRUTest{
	
	public static void main(String[] args)
	{
		String inputFile = 
			new String("E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\" +
			"sv.sanitized-access.20070109");
		String outputFile = new String("E:\\CentralServerLogDump\\results.txt");
		LRULogReader theLRULogReader = new LRULogReader(CacheConstants.Seven916);
		theLRULogReader.executeRequest(inputFile,outputFile);
	}
	
}