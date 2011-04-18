package com.sjsu.edu.clientipsplit;

import java.io.IOException;

import com.sjsu.edu.helpers.logfilereader.clientIPsplitter.WebLogClientIPSplitter;

public class LogFileReaderTest {
	
	public static void main(String[] args)
	{
		String inputFile = 
			"E:\\CentralServerLogDump\\rtp.sanitized-access.20070110\\" +
			"processed.rtp.sanitized-access.20070110";
			/*new String("E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\" +
					//"abcd.txt");
			"sv.sanitized-access.20070109");*/
		String outputFile = new String("E:\\CentralServerLogDump\\results.txt");
		String baseDir = "D:\\ClientIPCustomizedLogFile";
		
		WebLogClientIPSplitter logFileReader = new WebLogClientIPSplitter();
		logFileReader.initializeFileName(inputFile,outputFile,baseDir);
		
		try 
		{
			logFileReader.readFile();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}

}
