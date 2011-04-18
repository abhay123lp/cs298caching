package com.sjsu.edu.filewriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import com.sjsu.edu.node.Node;

/**
 * 
 * @author Administrator
 *
 */
public class FileWriterForCluster 
{
	String baseDir;
	FileWriter fstream;
	BufferedWriter out;
	
	public FileWriterForCluster(String baseDirectory)
	{
		baseDir = baseDirectory;
	}
	
	/**
	 * 
	 * 
	 * @param clusters
	 * @param clusterNum
	 * @throws IOException 
	 */
	public void writeToFile(Set<Node> clusters,int clusterNum,String clientIPAddress) throws IOException
	{		
		String topDirValue = baseDir + "\\" + clientIPAddress;
		
		String fileName = topDirValue + "\\cluster" + clusterNum + ".txt";
		
		File topDir = new File(topDirValue);

		if(!topDir.exists())
			topDir.mkdir();
		
		File filename = new File(fileName);
		
		fstream = new FileWriter(filename);
        out = new BufferedWriter(fstream);
		
		for (Node node : clusters) 
		{
			out.write(node.getURL() + "\n");
		}
		
		out.close();
	}
}
