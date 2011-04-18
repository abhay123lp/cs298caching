package com.sjsu.edu.domainprocessor.helpers;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.sjsu.edu.commonconstants.CacheConstants;
import com.sjsu.edu.commonhelpers.GarbageCollectorHelper;

/**
 * Component that computes the sequence for requested objects 
 * 
 * @author Administrator
 *
 */
public class SequenceProcessor {
	
	/**
	 * Entry point for sequence processor
	 */
	public List<String> computeSequence(List<StringBuffer> sessionList)
	{
		//Convert the Session List to an ArrayList of String[]
		ArrayList<String[]> webObjectList 
			= computeArr(sessionList);
		
		//Invoke Garbage Collector
		invokeGC();
		
		//Compute the Longest Common Subsequence
		List<String> theSessionList = computeLCS(webObjectList);
		
		return theSessionList;
	}
	
	/**
	 * Computes the Longest common subsequence for
	 */
	private List<String> computeLCS(List<String[]> sessionObjectArrList)
	{
		Iterator<String[]> iterator = sessionObjectArrList.iterator();
		List<String> theSequenceList = null;
		int i = 0;
		int j = 0;
		
		//Get the first Sequence (Session of Web Objects)
		String[] sequenceArr = (String[]) iterator.next();
		String[] candidateArr = null;
		List<String> sequenceList = null;
		
		while(iterator.hasNext()) 
		{
			//Get the next Sequence(Session of Web Objects)
			candidateArr = (String[]) iterator.next();
		
			//Get the Sequence of Common Web Objects from the Two Sequences
			sequenceList = 
				LCSAlgoImpl.LongestCommonSubsequence(sequenceArr, candidateArr);
			
			//Assign null so that the object is up for GC 
			sequenceArr = null;
			
			//Convert the Common Sequence from List to Array
			sequenceArr = new String[sequenceList.size()];
			
			//Traverse through the Sequence List
			for (String string : sequenceList) {
				sequenceArr[j] = string;
				j++;
			}
			
			j=CacheConstants.ZERO;
			
			//Assign null so that Object is up for GC
			sequenceList = null;
			
			i++;
		}
		
		//Invoke the Garbage Collector
		invokeGC();
		
		//Helper to convert Array to List
		theSequenceList = convertArrayToList(sequenceArr);
		
		return theSequenceList;
	}
	
	/**
	 * Helper to convert a String Array to a List
	 * 
	 * @param sequenceArr
	 * @return
	 */
	private List<String> convertArrayToList(String[] sequenceArr)
	{
		LinkedList<String> sequenceList 
							= new LinkedList<String>();
		
		//Iterate through the array to convert the Array to a List
		for (int i = 0; i < sequenceArr.length; i++) {
			sequenceList.add(sequenceArr[i]);
		}
		
		return sequenceList;
	}
	
	/**
	 * Helper to invoke Garbage Collector
	 */
	private void invokeGC()
	{
		GarbageCollectorHelper.invokeGC();
	}
	
	/**
	 * Helper to convert individual Sessions stored in List of StringBuffer 
	 * objects to Arrays and store them in a list and send back a list
	 * 
	 * @param sessionList
	 * @return
	 */
	private ArrayList<String[]> computeArr(List<StringBuffer> sessionList)
	{
		ArrayList<String[]> list = new ArrayList<String[]>();
		
		//Iterator to iterate over the Session List
		Iterator<StringBuffer> iterator = sessionList.iterator();
		
		StringBuffer stringBuffer = null;
		
		String sessionObjects = null;
		
		String[] sessionObjectArr = null;
		
		//Read Web Objects in each session and store them in Array 
		while(iterator.hasNext())
		{	
			stringBuffer = (StringBuffer) iterator.next();
			
			//Convert the StringBuffer object to String object
			sessionObjects = stringBuffer.toString();
			
			//Split the Web Objects in the Session
			sessionObjectArr = sessionObjects.split(",");
			
			list.add(sessionObjectArr);
			
			stringBuffer = null;
		}
		
		sessionList = null;
		
		//Invoke Garbage Collector - To prevent Heap Overflow
		invokeGC();
		
		return list;
	}
}