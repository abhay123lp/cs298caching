package com.sjsu.edu.domainprocessor.helpers;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Helper to process the Cluster
 * 
 * DEPENDS ON : 
 * 1) LCSAlgoImpl
 * 2) ProbablityComponent
 * 
 * 
 * @author Administrator
 *
 */
public class ClusterProcessor {
	
	private ProbabilityProcessor probabilityProcessor;
	private SequenceProcessor sequenceProcessor;
	
	public ClusterProcessor()
	{
		probabilityProcessor = new ProbabilityProcessor();
		sequenceProcessor = new SequenceProcessor();
	}

	/**
	 * Entry point for Cluster Processor
	 */
	public Map<String,Integer> computeCluster(List<StringBuffer> sessionList,Map<String,Integer> uniqueObjSet)
	{
		//findSequence(sessionList);
		
		return processProbablity(sessionList,uniqueObjSet);
	}
	
	/**
	 * Helper to invoke Sequence Processor 
	 * 
	 * @param sessionList
	 * @return
	 */
	private List<String> findSequence(List<StringBuffer> sessionList)
	{
		return sequenceProcessor.
			computeSequence(sessionList);
	}
	
	/**
	 * Helper to process probability
	 * 
	 * @param sessionList
	 */
	private Map<String,Integer> processProbablity(List<StringBuffer> sessionList,Map<String,Integer> uniqueObjSet)
	{
		return probabilityProcessor.
				computProbabilisticCluster(sessionList,uniqueObjSet);
	}
	
}
