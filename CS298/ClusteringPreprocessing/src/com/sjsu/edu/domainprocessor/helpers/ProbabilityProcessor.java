package com.sjsu.edu.domainprocessor.helpers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sjsu.edu.commonconstants.CacheConstants;
import com.sjsu.edu.commonhelpers.GarbageCollectorHelper;

/**
 * 
 * Component to compute Probability driven Clusters 
 * 
 * @author Administrator
 *
 */
public class ProbabilityProcessor {

	//private Map<String,Double> uniqueObjectProbability;
	private Map<String,Integer> uniqueObjectProbability;
	
	public ProbabilityProcessor()
	{
		//uniqueObjectProbability = new LinkedHashMap<String, Double>();
		uniqueObjectProbability = new LinkedHashMap<String, Integer>();
	}
	
	/**
	 * Entry point for processor for Probability 
	 * driven Clustering
	 */
	public Map<String,Integer> computProbabilisticCluster
			(List<StringBuffer> sessionList,Map<String,Integer> uniqueObjSet)
	{
		return computeCluster(sessionList,uniqueObjSet);
	}
	
	/**
	 * Helper to Compute the probabilistic Cluster
	 */
	private Map<String,Integer> computeCluster(List<StringBuffer> sessionList,
			Map<String,Integer> uniqueObjMap)
	{
		Set<String> uniqueObjSet = uniqueObjMap.keySet();
		
		//Unique Objects Iterator
		Iterator<String> setIterator = 
			uniqueObjSet.iterator();

		Integer uniqueObjectCounter = CacheConstants.ZERO;
		
		//Session List Iterator
		Iterator<StringBuffer> sessionListIterator 
								= sessionList.iterator();
		
		Integer noOfSessions = sessionList.size();
		
		Double numOfSessions = new Double(noOfSessions.toString());
		
		StringBuffer stringBuffer = null;
		
		String webObject = null;
		
		String[] sessionObjects = null;
		
		String sessionString = null;
		
		Double theObjectCount = null;
		
		Double probabilityOfObject = null;
		
		int i =0;
	
		//Iterate through the Unique Object List
		while(setIterator.hasNext())
		{
			webObject = setIterator.next();
			
			i = i + 1;
			
			if(i%100==0)
				System.out.println("Unique Object Number :" + i);
			
			sessionListIterator = sessionList.iterator();
			
			uniqueObjectCounter = CacheConstants.ZERO;
	
			//Try to see if the Unique Object Occurs in how many sessions
			while (sessionListIterator.hasNext()) {
		
				//
				stringBuffer = 
					(StringBuffer) sessionListIterator.next();
				
				//Convert the String Buffer Session String to a Session String
				sessionString = stringBuffer.toString();
				
				//Array of Session's Web Objects
				sessionObjects = sessionString.split(",");
		
				//Traverse through all of the Session's Objects
				for (String string : sessionObjects) 
				{	
					if(string.equals(webObject))
					{
						uniqueObjectCounter += 
								CacheConstants.ONE;
					}
				}
			}
			
			//The Web Object has been checked for in all sessions.
			//Now compute the probability
			theObjectCount 
						= new Double(uniqueObjectCounter.toString());
			
			probabilityOfObject = theObjectCount/numOfSessions;
			
			if(probabilityOfObject > 0.30)
			{
				uniqueObjectProbability.put(webObject, uniqueObjMap.get(webObject));
			}
			
			stringBuffer = null;
			sessionString = null;
			sessionObjects = null;
			probabilityOfObject = null;
			theObjectCount = null;
			
			//Invoke the GC
			invokeGC();	
		}
			
		return uniqueObjectProbability;
	}
	
	private void invokeGC()
	{
		GarbageCollectorHelper.invokeGC();
	}
	
}
