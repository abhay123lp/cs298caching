package com.sjsu.edu.logpreprocessor;

import java.io.IOException;

import com.sjsu.edu.weblogpreprocessor.WebLogPreprocessor;

public class LogFileReaderTest {

	public static void main(String[] args)
	{
		
		String inputFile = 
			/*new String("E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\" +
					//"abcd.txt");
			"sv.sanitized-access.20070109");*/
		"E:\\CentralServerLogDump\\rtp.sanitized-access.20070110\\rtp.sanitized-access.20070110";
		
		String outputFile = 
			"E:\\CentralServerLogDump\\rtp.sanitized-access.20070110\\" +
			"processed.rtp.sanitized-access.20070110";
			
			/*"E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\" +
				"processed.sv.sanitized-access.20070109.txt";*/
		
		String baseDir = "D:\\CustomizedLogFile";
		String datasetDetailsFileName = "E:\\CentralServerLogDump\\sv.sanitized-access.20070109\\" +
		"datasetDetailsFile.txt"; 
		
		WebLogPreprocessor logFileReader = new WebLogPreprocessor();
		logFileReader.initializeFileName(inputFile,outputFile,baseDir,datasetDetailsFileName);
		
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
