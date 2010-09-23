package com.sjsu.cache.lfu;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.sjsu.cache.helper.Request;
import com.sjsu.edu.cache.constants.CacheConstants;
import com.sjsu.edu.printer.Printer;

/**
 * 
 * The logic of the cache is to replace the least frequently occurring items from the cache
 *  
 *  +++++++++++++++++++++++++
 *  Important Data Structures
 *  +++++++++++++++++++++++++
 *  1) lFUCache : Cache Stores the items, with the frequency of how many times was requested from the cache
 *  
 *  // The below data structures store information only about the Least Frequently Used Item
 *  
 *  1) lFUCacheReplica : Cache stores mapping of frequency of Least Frequent 
 *  	Items and Frequency of the Item
 *  2) lFCacheEntryTime : Cache keeps store of least frequent item and time at 
 *  					  which it first entered the cache
 * 	+++++++++++++++++++++++++
 * @author Akshay Shenoy
 *
 */
public class LFUCache {
	
	//Constant to define the size for the cache
	private Integer cacheCapacity;
	
	//The total number of cache hits
	private long cacheHits;
	
	//The total number of cache misses
	private long cacheMisses;
	
	//The total cache hit size in terms of bytes
	private long totalCacheHitByteSize;
	
	//The total cache miss size in terms of bytes
	private long totalCacheMissByteSize;
	
	//The LFU Cache that stores the items and numbers of times the item was hit
	//private Map<Integer,String> lFUCache;
	private Map<String,Integer> lFUCache;
	
	/******** Mirror of the LFU Cache *************/
	private Map<Integer,String> lFUCacheReplica;
	/******** Mirror of the LFU Cache *************/
	
	/**For every item this stores the item to be cached 
		with the time at which the item was entered in the cache*/ 
	//private Map<String,Long> lFUCacheEntryTime;
	//private SortedMap<String,Long> lFUCacheEntryTime;
	private SortedMap<Long,String> lFUCacheEntryTime;
	
	//Handle used to get the current time
	private Calendar calender;
	
	//Least count for least frequent item
	private int leastFrequentCount;
	
	// Total Number of Items that were requested from the Cache
	private Long totalNumberOfItems;		
	
	// Tracks the Size of all the items that were requested for the Cache
	private Long totalRequestedItemSize;	
	
	public LFUCache(long capacity)
	{
		cacheCapacity = new Integer((int)capacity);
		lFUCache = new HashMap<String, Integer>(cacheCapacity);
		lFUCacheEntryTime = new TreeMap<Long,String>();
		lFUCacheReplica = new HashMap<Integer, String>(cacheCapacity);	
		calender = new GregorianCalendar();
		cacheHits = 0;
		cacheMisses = 0;
		totalCacheHitByteSize=0;
		totalCacheMissByteSize=0;
		leastFrequentCount=0;
		totalNumberOfItems= new Long(0);		
		totalRequestedItemSize = new Long(0);	
	}
	
	
	/**
	 * The interface to cache Objects
	 * 
	 */
	//public void cacheItem(String itemToBeCached)
	public void cacheItem(Request theItemToBeCached)
	{
		addItemToCache(theItemToBeCached);
	}
	
	/**
	 *  Method to actually add items to cache
	 */
	private void addItemToCache(Request theItemToBeCached)
	{
		String itemToBeCached = theItemToBeCached.getRequestedObject();
		int actionToBeExecuted = computeCase(itemToBeCached);
		Integer itemCount;
		
		switch(actionToBeExecuted)
		{
			case 1:
				//Case 1: Cache Already contains the item and cache is not full
				//Actions:
				//1) Update count on existing entry in LFU Cache
				//2) If the LFU Replica contains an entry; REMOVE IT
				//3) If the LFU EntryTime contains an entry; REMOVE IT
				
				//Get the current count for already cached item
				itemCount = lFUCache.get(itemToBeCached);
				
				//Increment the count by one
				itemCount+=CacheConstants.ONE;
				
				//Update the LFU Cache
				lFUCache.put(itemToBeCached, itemCount);
							
				// Below Code Removes the item from the caches as this item is not 
				// Least Frequently Used Item anymore

				//Update the LFUCache Entry
				if(lFUCacheEntryTime.containsValue(itemToBeCached))
					removeLFUCacheEntryTime(itemToBeCached);
				
				//Update the LFUCache Replica
				if(lFUCacheReplica.containsValue(itemToBeCached))
					removeLFUCacheReplica(itemToBeCached);
				
				//Update the Cache Hit/Cache Miss Statistics
				cacheHits +=CacheConstants.ONE;
				
				totalCacheHitByteSize = totalCacheHitByteSize 
											+ theItemToBeCached.requestedItemSize;
				
				/** *** */
				Printer.printString("Cache Hit ::" + itemToBeCached );
				
				break;
				
			case 3:
				//Case 3: Cache does not contain the item, but cache is not full
				
				//Actions:
				//1) Make an entry in LFU Cache about the new item
				//2) Make an entry in the LFUCacheEntryTime
				//3) Make an entry in the LFUCacheReplica
				
				itemCount = new Integer(CacheConstants.ONE);
				
				//Reset the least Frequent item count
				leastFrequentCount = itemCount;
				
				// 			NEW ITEM IN THE CACHE
				// Remove the item from the caches as this item is not 
				// Least Frequently Used Item anymore
				
				//Update LFUCacheEntryTime
				lFUCacheEntryTime.put(new Long(calender.getTimeInMillis()),itemToBeCached);
				
				//put the item to be cached - UPDATE the main Cache
				lFUCache.put(itemToBeCached, itemCount);
				
				//Update the LFUCacheReplica
				lFUCacheReplica.put(itemCount, itemToBeCached);
				
				//Set the handle to null
				itemCount = null;
				
				//Update the Cache Hit/Cache Miss Statistics
				cacheMisses +=CacheConstants.ONE;
				
				totalCacheMissByteSize = totalCacheMissByteSize 
											+ theItemToBeCached.requestedItemSize;
				/** *** */
				Printer.printString("Cache Miss ::" + itemToBeCached );
				
				break;
				
			case 2:
				//Case 2: Cache contains the item but cache is full
				
				//Actions:
				//1) Update count on existing entry in LFU Cache
				//2) If the LFU Replica contains an entry; REMOVE IT
				//3) If the LFU EntryTime contains an entry; REMOVE IT
				
				//Get the current count for already cached item
				itemCount = lFUCache.get(itemToBeCached);
				
				//Increment the count by one
				itemCount+=1;
				
				//Update the LFU Cache
				lFUCache.put(itemToBeCached, itemCount);
				
				if(lFUCache.size() > cacheCapacity){
					Printer.printString(new Boolean(lFUCache.containsKey(itemToBeCached)).toString());
					Printer.printInteger(lFUCache.size());
				}
				
				// Remove the item from the caches as this 
				// item is not Least Frequently Used Item anymore
				
				//Update the LFUCache Entry
				if(lFUCacheEntryTime.containsValue(itemToBeCached))
					removeLFUCacheEntryTime(itemToBeCached);
				
				//Update the LFUCache Replica
				if(lFUCacheReplica.containsValue(itemToBeCached))
					removeLFUCacheReplica(itemToBeCached);
				
				//Update the Cache Hit/Cache Miss Statistics
				cacheHits +=CacheConstants.ONE;
				
				totalCacheHitByteSize = totalCacheHitByteSize 
											+ theItemToBeCached.requestedItemSize;
				
				/** *** */
				Printer.printString("Cache Hit ::" + itemToBeCached );
				
				break;
				
			case 4:
				//Case 4: Cache does not contain the item, but cache is full
				
				//Actions:
				//1) Figure out which is the Least Frequently occurring item - Using Time as a factor
				//2) Remove LFU item from the cache - Make space for one item
				//3) Place the new item in the LFUCache, LFUCacheEntryTime, LFUCacheReplica
				
				/**** Figure the LFU item and throw it out ****/
				
				//Printer.printInteger(lFUCacheEntryTime.size());
				//Printer.printInteger(lFUCacheReplica.size());
				
				if(lFUCache.size() > cacheCapacity){
					Printer.printString(new Boolean(lFUCache.containsKey(itemToBeCached)).toString());
					Printer.printInteger(lFUCache.size());
				}
				
				if(lFUCacheEntryTime.size() > CacheConstants.ZERO){
					
					// 1) 2) STARTS
					//Get the earliest LFUKey
					Long lFUFirstKey = lFUCacheEntryTime.firstKey();
					
					//Get the LFU Entry/Item
					String lFUEntry = lFUCacheEntryTime.get(lFUFirstKey);
					
					//1) - Remove Item from LFUCache
					//lFUCache.remove(lFUEntry);
					Integer abc = lFUCache.remove(lFUEntry);
					
					//2)- Remove Item from lFUCacheEntryTime
					String time = lFUCacheEntryTime.remove(lFUFirstKey);
					
					//3)- Remove Item from lFU
					removeLFUCacheReplica(lFUEntry);
					// 1) 2) ENDS
				}
				/**** Figure the LFU item and throw it out ****/
				
				//Details about entry for the new item
				itemCount = new Integer(CacheConstants.ONE);
				
				//Reset the least Frequent item count
				leastFrequentCount = itemCount;				
				
				//put the item to be cached in main cache
				lFUCache.put(itemToBeCached, itemCount);
				
				if(lFUCache.size() > cacheCapacity){
					Printer.printString(new Boolean(lFUCache.containsKey(itemToBeCached)).toString());
					Printer.printInteger(lFUCache.size());
				}
				
				// 			NEW ITEM IN THE CACHE
				lFUCacheEntryTime.put(new Long(calender.getTimeInMillis()),itemToBeCached);
								
				lFUCacheReplica.put(itemCount, itemToBeCached);
				
				//Set the handle to null
				itemCount = null;
				
				//Update the Cache Hit/Cache Miss Statistics
				cacheMisses += CacheConstants.ONE;
				
				totalCacheMissByteSize = totalCacheMissByteSize 
								+ theItemToBeCached.requestedItemSize;
			
				/** *** */
				Printer.printString("Cache Miss ::" + itemToBeCached );
				
				break;
				
			default:
				//Printer.printString("Execution of Default Case");
				break;
				
			
		}
		
		//Update Statistics of Total Number of Items
		totalNumberOfItems +=CacheConstants.ONE;
		
		//Update Statistics of Total Requested Item Size
		totalRequestedItemSize = totalRequestedItemSize 
										+ theItemToBeCached.requestedItemSize; 
	
	}
	
	/**
	 * Computes which use case is to be executed
	 * 
	 * @param itemToBeCached
	 * @return
	 */
	private int computeCase(String itemToBeCached)
	{
		
		Printer.printString("LFU Cache Size : "  + lFUCache.size());
		
		if(lFUCache.size() >= cacheCapacity){
			Printer.printString(new Boolean(lFUCache.containsKey(itemToBeCached)).toString());
			Printer.printInteger(lFUCache.size());
		}
		
		if(lFUCache.containsKey(itemToBeCached)&& lFUCache.size() >= cacheCapacity)
		{//Case 2: Cache contains item and cache is full
			return CacheConstants.TWO;
		}
		else if(!lFUCache.containsKey(itemToBeCached)&&lFUCache.size() >= cacheCapacity)
		{//Case 4: Cache does not contain item, but cache is full
			return CacheConstants.FOUR;
		}		
		else if(!lFUCache.containsKey(itemToBeCached)&&lFUCache.size() < cacheCapacity)
		{//Case 3: Cache does not contain item, but cache is not full
			return CacheConstants.THREE;
		}
		else if(lFUCache.containsKey(itemToBeCached)&& lFUCache.size() < cacheCapacity)
		{//Case 1: Cache Already contains item & Cache is not full
			return CacheConstants.ONE;
		}
		
		return CacheConstants.ZERO;
		
	}
	
	/**
	 * Determines least frequent item from cache
	 * 
	 * @param leastFrequentItemCount
	 * @return
	 */
	private String removeLeastFrequentItem(Integer leastFrequentItemCount)
	{
		Long firstLeastFrequentItemKey = lFUCacheEntryTime.firstKey();
		
		//Least Frequent Item entered in the cache at an earliest time
		String itemToBeRemoved = lFUCacheEntryTime.get(firstLeastFrequentItemKey);
	
		return null;
	}
	
	/**
	 * 
	 * @param itemToBeRemoved
	 */
	private void removeLFUCacheEntryTime(String itemToBeRemoved)
	{
		//Get the Key Set for LFUCacheEntryTime
		Set<Long> lfuCETKeySet =  lFUCacheEntryTime.keySet();
		
		//Iterate through each entry in Map with key as handle
		for (Long long1 : lfuCETKeySet) {
			
			//If the Map entry matches with the item to be removed then,
			//remove the entry and break from the loop
			if(lFUCacheEntryTime.get(long1).equals(itemToBeRemoved))
			{	
				lFUCacheEntryTime.remove(long1);
				break;
			}
		}
	}
	
	/**
	 * 
	 * @param itemToBeRemoved
	 */
	private void removeLFUCacheReplica(String itemToBeRemoved)
	{
		//Get the Key Set for LFUCacheEntryTime
		Set<Integer> lfuCETKeySet =  lFUCacheReplica.keySet();
		
		//Iterate through each entry in Map with key as handle
		for (Integer long1 : lfuCETKeySet) {
			
			//If the Map entry matches with the item to be removed then,
			//remove the entry and break from the loop
			if(lFUCacheReplica.get(long1).equals(itemToBeRemoved))
			{	
				lFUCacheReplica.remove(long1);
				break;
			}
		}
	}
	
	public Integer getCacheCapacity() {
		return cacheCapacity;
	}


	public long getCacheHits() {
		return cacheHits;
	}


	public long getCacheMisses() {
		return cacheMisses;
	}


	public long getTotalCacheHitByteSize() {
		return totalCacheHitByteSize;
	}


	public long getTotalCacheMissByteSize() {
		return totalCacheMissByteSize;
	}


	public Map<String, Integer> getLFUCache() {
		return lFUCache;
	}


	public Map<Integer, String> getLFUCacheReplica() {
		return lFUCacheReplica;
	}


	public SortedMap<Long, String> getLFUCacheEntryTime() {
		return lFUCacheEntryTime;
	}


	public Calendar getCalender() {
		return calender;
	}


	public int getLeastFrequentCount() {
		return leastFrequentCount;
	}


	public Long getTotalNumberOfItems() {
		return totalNumberOfItems;
	}


	public Long getTotalRequestedItemSize() {
		return totalRequestedItemSize;
	}

	public Integer getCacheMapSize()
	{
		return lFUCache.size();
	}

}
