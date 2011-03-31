package com.sjsu.edu.domainprocessor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class LogProcessorTest {

	public static void main(String[] args)
	{
		String baseDir = "D:\\CustomizedLogFile";
		
		File baseDirectory = new File(baseDir);
		
		String listOfDir[] = baseDirectory.list();
		
		for (String string : listOfDir) {
			
			File subDirFiles = new File(baseDir + "\\" + string);
			
			String[] listOfSubDir = subDirFiles.list();
			
			if(listOfSubDir!=null)
				for (String string2 : listOfSubDir) {
					
					String pathToFile = subDirFiles + "\\" + string2;
					LogProcessorTest.processCluster(pathToFile);
					System.out.println(pathToFile);
					
				}
		}
		
	
	}
	
	public static void processCluster(String inputWebLogFile)
	{
		
		String inputFile = 
			new String(inputWebLogFile);
		//new String("D:\\junk\\sprint.txt");
		//new String("D:\\junk\\sprint1.txt");
		//new String("D:\\CustomizedLogFile\\www.sprint.com\\www.sprint.com.txt");
	
		//"D:\\CustomizedLogFile\\157.91.28.89\\157.91.28.89.txt";
		//String outputFile = new String("E:\\CentralServerLogDump\\sprint.txt");
		
		String baseDir = "D:\\CustomizedLogFile";
		Map<String,Integer> clusteredObjects = null;
		Set<String> clusteredObjectSet = null;
		LogProcessor logFileReader = null;
		FileWriter fstream = null;
		BufferedWriter out = null;
		
		int index = inputFile.lastIndexOf("\\") + 1;
		int lastIndexOfDot = inputFile.lastIndexOf(".");
		
		String dmnString = inputFile.substring(index, lastIndexOfDot);
		String domainName = "http://" + dmnString ;
		String outputFile = "D:\\ClusteredWebObjects" + "\\" + dmnString + ".txt";
		
		logFileReader = new LogProcessor(inputFile, outputFile, baseDir);
		
		try {
			
			clusteredObjects = logFileReader.readFile();
			
			if(clusteredObjects!=null && clusteredObjects.size() > 0)
			{
				clusteredObjectSet = clusteredObjects.keySet();
				fstream = new FileWriter(outputFile);
			    out = new BufferedWriter(fstream);
			    
			    for (String string : clusteredObjectSet) {
					out.write(domainName + string + "," + clusteredObjects.get(string) +"\n");
				}
			     
			    //Close the output stream
			    out.close();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
			
	}
	
}
