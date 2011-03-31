package com.sjsu.edu.cachelfu;

public class CacheLFUTest {

	public static void main(String[] args)
	{
		
		/*String inputFile = 
			new String("E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\" +
			"testsetlfu.txt");
			
		String outputFile = new String("E:\\CentralServerLogDump\\results.txt");
		CacheLFUReader theLRULogReader = new CacheLFUReader(new Long(17953));*/
		
		/*//Main Testing Content 2
		String inputFile =
			"E:\\CentralServerLogDump\\uc.sanitized-access.20070110\\uc.sanitized-access.20070110";
		
		LRULogReader theLRULogReader = new LRULogReader(new Long(238922354));*/
		
		//Main Testing Content - Error Throwing Data Set
		String inputFile =
			new String("E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\" +
			"sv.sanitized-access.20070109");
		
		CacheLFUReader theLRULogReader = new CacheLFUReader(new Long(95433150));
		
		//Edited Cache Size
		//CacheLFUReader theLRULogReader = new CacheLFUReader(new Long(77875413));
		
		/*//Main Testing Content 2
		String inputFile =
			"E:\\CentralServerLogDump\\uc.sanitized-access.20070110\\uc.sanitized-access.20070110";
		
		CacheLFUReader theLRULogReader = new CacheLFUReader(new Long(238922354));*/
		/*LRULogReader theLRULogReader = new LRULogReader(new Long(238922354));*/
		
		/*String inputFile = "D:\\CustomizedLogFile\\157.91.28.89\\157.91.28.89.txt";*/
		/*String inputFile = "D:\\CustomizedLogFile\\157.91.28.89\\157.91.28.892.txt";
		
		CacheLFUReader theLRULogReader = new CacheLFUReader(new Long(1307182));*/
		/*LRULogReader theLRULogReader = new LRULogReader(new Long(1307182));*/
		
		/*String inputFile = "D:\\CustomizedLogFile\\2jz.org\\2jz.org.txt";*/
		
		/*//Testing LFU
		String inputFile = 
			new String("E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\" +
			"testsetlfu.txt");
		
		CacheLFUReader theLRULogReader = new CacheLFUReader(new Long(20));*/
		
		String outputFile = new String("E:\\CentralServerLogDump\\results2.txt");
		
		theLRULogReader.executeRequest(inputFile,outputFile);
	}
	
}
