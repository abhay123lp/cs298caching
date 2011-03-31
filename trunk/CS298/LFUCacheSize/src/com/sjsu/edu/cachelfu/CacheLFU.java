package com.sjsu.edu.cachelfu;

import java.util.Collection;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.sjsu.cache.helper.Request;
import com.sjsu.edu.commonconstants.CacheConstants;

public class CacheLFU {

	/**
	 * Map of Cached Item and Size of the Cached Item
	 */
	private Map<String,Long> cacheMap;
	
	/**
	 * Cached Item and 	Number of Times it was requested
	 */
	private Map<String,Integer> lFUCacheMap;
	
	/**
	 * Cached Item and the Time at which it was requested
	 */
	private Map<String,Long> cacheTimeMap;
	
	/**
	 * Cache capacity in terms of the number of items it can store
	 */
	private Integer capacity;
	
	/**
	 * The Max Cache Size for the Cache
	 */
	//private Integer maxCacheSize;
	private Long maxCacheSize;
	
	public Long getMaxCacheSize() {
		return maxCacheSize;
	}

	public void setMaxCacheSize(Long maxCacheSize) 
	{
		this.maxCacheSize = maxCacheSize;
	}

	/**
	 * The total Size of the requested Item from the cache : Parameter<br/>
	 * used to compute BHR(Byte Hit Ratio)
	 */
	private Long totalRequestedItemSize;
	/*private Integer totalRequestedItemSize;*/
	
	/**
	 * Total number of Items in the Cache
	 */
	private Long totalNumberOfItems;
	/*private Integer totalNumberOfItems;*/
	
	/**
	 * Total Cache Hit Size in terms of the Number of Bytes:
	 * <br/> Parameter used to compute the BHR(Byte Hit Ratio)
	 */
	private Long totalCacheHitByteSize;
	//private Integer totalCacheHitByteSize;
	
	/**
	 * Total Cache Miss Size in terms of the Number of Bytes:
	 * <br/> Parameter used to compute the BHR(Byte Hit Ratio)
	 */
	private Long totalCacheMissByteSize;
	//private Integer totalCacheMissByteSize;
	
	/**
	 * Absolute Number of the Cache Hits
	 * <br/> Parameter used to compute the HR(Hit Ratio)
	 */
	private Long cacheHits;
	/*private Integer cacheHits;*/
	
	/**
	 * Absolute Number of the Cache Misses
	 * <br/> Parameter used to compute the BHR(Hit Ratio)
	 */
	private Long cacheMisses;
	//private Integer cacheMisses;
	
	/**
	 * Parameter used to keep track of the current cache size
	 * <br/>in terms of the number of bytes. 
	 * <br/><b> Parameter Used to prevent Cache Overshooting the Size</b>
	 */
	private Long currentCacheSize;
	//private Integer currentCacheSize;
	
	public Long getCurrentCacheSize() {
		return currentCacheSize;
	}

	public void setCurrentCacheSize(Long currentCacheSize) {
		this.currentCacheSize = currentCacheSize;
	}

	//public CacheLFU(Integer cacheCap)
	public CacheLFU(Long maxCacheCapacity)
	{
		lFUCacheMap = new HashMap<String, Integer>();
		cacheTimeMap = new HashMap<String, Long>();
		cacheMap = new HashMap<String, Long>();
		totalRequestedItemSize = new Long(0);
		totalNumberOfItems = new Long(0);
		totalCacheHitByteSize = new Long(0);
		totalCacheMissByteSize = new Long(0);
		cacheHits = new Long(0);
		cacheMisses = new Long(0);
		currentCacheSize = new Long(0);
		maxCacheSize = maxCacheCapacity;
	}
	
	//public Integer getTotalRequestedItemSize() 
	public Long getTotalRequestedItemSize()
	{
		return totalRequestedItemSize;
	}
	
	//public void setTotalRequestedItemSize(Integer totalRequestedItemSize)
	public void setTotalRequestedItemSize(Long totalRequestedItemSize)
	{
		this.totalRequestedItemSize = totalRequestedItemSize;
	}

	//public Integer getTotalNumberOfItems()
	public Long getTotalNumberOfItems()
	{
		return totalNumberOfItems;
	}

	//public void setTotalNumberOfItems(Integer totalNumberOfItems) 
	public void setTotalNumberOfItems(Long totalNumberOfItems)
	{
		this.totalNumberOfItems = totalNumberOfItems;
	}

	//public Integer getTotalCacheHitByteSize()
	public Long getTotalCacheHitByteSize()
	{
		return totalCacheHitByteSize;
	}

	//public void setTotalCacheHitByteSize(Integer totalCacheHitByteSize)
	public void setTotalCacheHitByteSize(Long totalCacheHitByteSize)
	{
		this.totalCacheHitByteSize = totalCacheHitByteSize;
	}

	//public Integer getTotalCacheMissByteSize()
	public Long getTotalCacheMissByteSize()
	{
		return totalCacheMissByteSize;
	}

	//public void setTotalCacheMissByteSize(Integer totalCacheMissByteSize)
	public void setTotalCacheMissByteSize(Long totalCacheMissByteSize)
	{
		this.totalCacheMissByteSize = totalCacheMissByteSize;
	}
	
	//public Integer getCacheHits() 
	public Long getCacheHits()
	{
		return cacheHits;
	}

	//public void setCacheHits(Integer cacheHits) 
	public void setCacheHits(Long cacheHits)
	{
		this.cacheHits = cacheHits;
	}

	//public Integer getCacheMisses() 
	public Long getCacheMisses()
	{
		return cacheMisses;
	}

	//public void setCacheMisses(Integer cacheMisses) 
	public void setCacheMisses(Long cacheMisses)
	{
		this.cacheMisses = cacheMisses;
	}
	
	//public Integer getCacheMapSize()
	public Integer getCacheMapSize()
	{
		return cacheMap.size();
		//return capacity;
	}
	
	/*public void setCacheCapacity(Integer cacheCap)
	{
		capacity = cacheCap;
	}*/
	
	/**
	 * The interface to cache Objects
	 */
	public void cacheItem(Request theItemToBeCached)
	{
		addItemToCache(theItemToBeCached);
	}
	
	private void addItemToCache(Request theItemToBeCached)
	{	
		
		//The actual Object to be cached
		String itemToBeCached = theItemToBeCached.getRequestedObject().trim();
		
		//The item to be cached's size
		Long requestedItemSize = new Long(theItemToBeCached.requestedItemSize.toString());
		
		//Use case to be executed
		int actionToBeExecuted = computeCase(theItemToBeCached);
		//int actionToBeExecuted = computeCase(itemToBeCached);
		
		Integer itemCount=null;
		
		Long currentTime=null;
		
		switch(actionToBeExecuted)
		{
			case 1:
				//Case 1: Cache contains item and cache is full
				//1) Get the existing Item say 'X' from the main queue and Update its count
				//2) If the item 'X' exists on the leastFrequentItemMap remove it
				//3) If the item 'X' exists on the lFUCacheEntryTime remove it 
				
				/**
				 * 	- cacheMap<String,Long> : Cached Item and Size

					- lFUCacheMap<String,Integer> : Cached Item and 
													Number of Times it was requested

					- cacheTimeMap<String,Long> : Cached Item and at
													time it was requested
				 */
				
				//Get the item count used to increase the requested Item count
				itemCount = lFUCacheMap.get(itemToBeCached);
				
				//Increase the Item count by one
				itemCount+=CacheConstants.ONE;
				
				//Get the Current System Time
				currentTime = System.currentTimeMillis();
				
				//Number of time the item was requested
				lFUCacheMap.put(itemToBeCached, itemCount);
				
				//Time the item was requested
				cacheTimeMap.put(itemToBeCached, currentTime);
				
				//Requested Item 
				//cacheMap.put(itemToBeCached, requestedItemSize);
				
				//Use case of Cache Hit : Increase the Cache Hit count by One
				cacheHits += CacheConstants.ONE;
				
				//Increase the parameter for cache hit size parameter
				totalCacheHitByteSize = totalCacheHitByteSize + requestedItemSize;
				
				//Edit the Total Cache Size
				/**
				currentCacheSize = currentCacheSize 
									+ theItemToBeCached.requestedItemSize;
									*/
				
				break;
				
			case 2:
				//Case 2: Cache does not contain item, but cache is full
				//Actions:
				//1) Remove LFU item from LFUCache
				//2) Also remove LFU item from LFU replica & LFU time; if they exist on the cache
				//3) Place the new item on the LFUCache
				//4) Update the LFU Replica & LFU Time	
				
				//Remove the Least frequent Item
				removeLeastFrequentItem(theItemToBeCached);
				
				//Initialize the requested Item Size to One
				itemCount = new Integer(CacheConstants.ONE);
				
				//Get the current Time from the System
				currentTime = new Long(System.currentTimeMillis());
				
				//LFUCacheMap - Item to be cached and Item Count
				lFUCacheMap.put(itemToBeCached, itemCount);
				
				//Log the Time at which the item was pushed on to the cache
				cacheTimeMap.put(itemToBeCached, currentTime);

				//Record the Entry into the Cache Map
				//cacheMap.put(itemToBeCached, new Long(theItemToBeCached.requestedItemSize));
				cacheMap.put(itemToBeCached, requestedItemSize);
				
				//Increment the Cache Miss by One
				cacheMisses += CacheConstants.ONE;
				
				//totalCacheMissByteSize += theItemToBeCached.requestedItemSize;
				totalCacheMissByteSize = totalCacheMissByteSize + requestedItemSize;
				
				//Update the Current Cache Size to reflect the current cache Size
				currentCacheSize = currentCacheSize 
						+ requestedItemSize;
				/*currentCacheSize = currentCacheSize 
								+ theItemToBeCached.requestedItemSize;*/
				
				break;
				
			case 4:
				//Case 4: Cache does not contain item & Cache is not full
				//Actions:
				//1) Push the Item on the LFU Queue
				//2) Register Item on the LFU Replica
				//3) Register Item on the LFUCacheTime
				//4) Also If there are any other LFU items in the leastFrequentMap, lFUCacheReplica remove those items
				
				//Count of the Item
				itemCount = new Integer(CacheConstants.ONE);
				
				//Get the current Time
				currentTime = new Long(System.currentTimeMillis());
				
				//LFUCacheMap - Item to be cached and Frequency
				lFUCacheMap.put(itemToBeCached, itemCount);
				
				//CacheTimeMap - Item to be cached and the Current Time at which the Cache was filled
				cacheTimeMap.put(itemToBeCached, currentTime);
				
				//Log the Item to be cached in the CacheMap - Item to be cached & Size of the Item
				//cacheMap.put(itemToBeCached,new Long(theItemToBeCached.requestedItemSize));
				cacheMap.put(itemToBeCached,requestedItemSize);
				
				//Increment the Cache Misses
				cacheMisses += CacheConstants.ONE;
				
				//Increment the Cache Miss Size parameter
				totalCacheMissByteSize = totalCacheMissByteSize + requestedItemSize;
				//totalCacheMissByteSize += theItemToBeCached.requestedItemSize;

				//Update the Current Cache Size
				currentCacheSize = currentCacheSize + requestedItemSize;
				/*currentCacheSize = currentCacheSize + theItemToBeCached.requestedItemSize;*/
				
				break;
				
				/*case 3:
				//Case 3: Cache Already contains item & Cache is not full
				//Actions:
				//1) Get the existing Item say 'X' from the main queue and Update its count
				//2) If the item 'X' exists on the leastFrequentItemMap remove it
				//3) If the item 'X' exists on the lFUCacheEntryTime remove it
				
				itemCount = lFUCacheMap.get(itemToBeCached);
				
				itemCount +=CacheConstants.ONE;
				
				lFUCacheMap.put(itemToBeCached, itemCount);
				
				currentTime = new Long(System.currentTimeMillis());
				
				cacheTimeMap.put(itemToBeCached, currentTime);
				
				cacheHits += CacheConstants.ONE;
				
				totalCacheHitByteSize += theItemToBeCached.requestedItemSize;
				
				break;*/
				
			default:
				
				break;
			
		}
		
		/*totalRequestedItemSize = totalRequestedItemSize 
									+ theItemToBeCached.requestedItemSize;*/
		totalRequestedItemSize = totalRequestedItemSize + requestedItemSize;
		
		totalNumberOfItems = totalNumberOfItems + CacheConstants.ONE;
	}
	
	/**
	 * Computes which use case is to be executed
	 * 
	 * @param itemToBeCached
	 * @return
	 */
	//private int computeCase(String itemToBeCached)
	private int computeCase(Request theItemToBeCached)
	{
		
		Long tempCacheSize = null;
		String itemToBeCached = null;
		
		//Object to be cached
		itemToBeCached = theItemToBeCached.getRequestedObject();
		
		Long itemToBeCachedSize = new Long(theItemToBeCached.requestedItemSize.toString());
		
		//Get the temporary Cache Size
		tempCacheSize = currentCacheSize + itemToBeCachedSize;
		
		//if(lFUCacheMap.containsKey(itemToBeCached) && lFUCacheMap.size() >=capacity )
		//if(lFUCacheMap.containsKey(itemToBeCached) && tempCacheSize >= maxCacheSize)
		//if(lFUCacheMap.containsKey(itemToBeCached) && tempCacheSize > maxCacheSize)
		if(lFUCacheMap.containsKey(itemToBeCached))
		{//Case 1: Cache contains item and cache is full
			return CacheConstants.ONE;
		}
		//else if(!lFUCacheMap.containsKey(itemToBeCached)&& lFUCacheMap.size() >=capacity)
		//else if(!lFUCacheMap.containsKey(itemToBeCached)&& tempCacheSize >= maxCacheSize)
		else if(!lFUCacheMap.containsKey(itemToBeCached)&& tempCacheSize > maxCacheSize)
		{//Case 2: Cache does not contain item, but cache is full
			return CacheConstants.TWO;
		}		
		//else if(lFUCacheMap.containsKey(itemToBeCached)&&lFUCacheMap.size() < capacity)
		//else if(lFUCacheMap.containsKey(itemToBeCached)&& tempCacheSize < maxCacheSize)
		/*else if(lFUCacheMap.containsKey(itemToBeCached)&& tempCacheSize <= maxCacheSize)
		{//Case 3: Cache Already contains item & Cache is not full
			return CacheConstants.THREE;
		}*/
		//else if(!lFUCacheMap.containsKey(itemToBeCached)&&lFUCacheMap.size()<capacity)
		//else if(!lFUCacheMap.containsKey(itemToBeCached)&& tempCacheSize < maxCacheSize)
		else if(!lFUCacheMap.containsKey(itemToBeCached)&& tempCacheSize <= maxCacheSize)
		{//Case 4: Cache does not contain item, but cache is not full
			return CacheConstants.FOUR;
		}
		
		return CacheConstants.ZERO;	
	}

	/**
	 * Helper function to remove the least frequent Item
	 * <br/>
	 * <b>
	 * 	<br/>	In case there are multiple items that are least frequently occurring
	 *  <br/>   remove the item that was first inserted into the cache in terms of the
	 *  <br/>   time it was entered in the cache
	 * </b>
	 * 
	 */
	public void removeLeastFrequentItem(Request theRequestedItem)
	{
		Integer leastCount=null;
		
		//Variable to get the first key or least count
		Long firstKey = null;
		
		//The least frequent item reference object
		String leastFrequentItem = null;
		
		Runtime r = null;
		
		Long entryTime = null;
		
		//Set of Least Frequently Used Item's values
		Collection<Integer> countValues = null;
		
		//Set of Least Frequently Used Item's values
		Set<String> cachedItemSet = null;
		
		SortedMap<Long, String> sortedMap = null;
		
		TreeSet<Integer> countValuesSet = null;
		
		// Stores the current Cache Size after adding 
		// current Cache size and size of item to be cached 
		Long tempCacheSize = null;
		
		//Size of the Item to be removed
		Long itemToBeRemovedSize = null;
		
		Long theRequestedItemSize = new Long(theRequestedItem.requestedItemSize.toString());
		
		/*tempCacheSize = currentCacheSize 
						+ theRequestedItem.requestedItemSize;*/
		tempCacheSize = currentCacheSize 
								+ theRequestedItemSize;
		
		/*******************************************************/
		// Remove Item from the cache till the cache has enough space to 
		// accommodate the item to be cached
		/*******************************************************/
		//while(tempCacheSize >= maxCacheSize)
		while(tempCacheSize > maxCacheSize)
		{
			// ++++++++++++++++++++++++++++++++++++++++++
			// Traverse over the counts of items in 
			// the Map to get the Least Count
			// ++++++++++++++++++++++++++++++++++++++++++
			
			//Set of Least Frequently Used Item's values
			countValues = lFUCacheMap.values();
			
			//Set of actual Cached Items items
			cachedItemSet = lFUCacheMap.keySet();
			
			//Sorted Map 
			sortedMap = new TreeMap<Long, String>();
			
			//TreeSet - To Hold Least Frequent count for Items in sorted order
			countValuesSet = new TreeSet<Integer>();
			
			/*** *********************************** */
			// 1. -- Add the various count values 
			//         for all items in the Cache  --
			/*** *********************************** */
			countValuesSet.addAll(countValues);
			
			/*** *********************************** */
			//Get the Least Count value
			/*** *********************************** */
			try
			{
				leastCount = countValuesSet.first();
			}
			catch(Exception ex)
			{
				System.out.println("Exception");
				System.out.println(this.totalNumberOfItems);
			}
			
			/*** *********************************** */
			// 2. --- Get all the Items who have least count ---
			// 3. --- Create a Map of 
			//    (Entry Time for the Least Frequent Item, Least Frequent Item itself) 
			/*** *********************************** */
			for (String string : cachedItemSet) 
			{
				// Get the items that has leastCount
				if(lFUCacheMap.get(string) == leastCount)
				{
					entryTime = cacheTimeMap.get(string);
					sortedMap.put(entryTime, string);
				}
			}
			
			/*** *********************************** */
			// 3. --- Get all the Item who have least count 
			// 		  & inserted first into the Cache ---
			/*** *********************************** */
			firstKey = sortedMap.firstKey();

			if(firstKey==null)
			{
				System.out.println("First Key Empty");
			}
			
			/*** *********************************** */
			//4. --- Get the least frequent Item ---
			/*** *********************************** */
			leastFrequentItem = sortedMap.get(firstKey);
			
			//System.out.println("Throwing out + " + leastFrequentItem);
			
			/*System.out.println("Removing :: " + leastFrequentItem + " Current Cache Size :" + this.currentCacheSize);*/
			
			//Get the Item to be removed's size in bytes
			itemToBeRemovedSize = cacheMap.get(leastFrequentItem);
			
			//Remove the Item from the Cache Map 
			// - A map of item and size of the item
			cacheMap.remove(leastFrequentItem);
				
			//Remove the item from the lfu cache map 
			// - A map of item and number of times it was requested
			lFUCacheMap.remove(leastFrequentItem);
			
			//Remove the item from the cacheTimeMap 
			// - A map of item and the time it was inputed into the cache
			cacheTimeMap.remove(leastFrequentItem);
			
			//Edit the current Cache Size
			currentCacheSize = currentCacheSize - itemToBeRemovedSize;
			
			// +++++++++++++++++++++++++++++
				/******************************/
				// Initialize the local variables to null
				/******************************/
			// +++++++++++++++++++++++++++++
			cachedItemSet = null;
			
			sortedMap = null;
			
			firstKey = null;
			
			itemToBeRemovedSize = null;
			
			leastCount = null;
			
			leastFrequentItem = null;
			
			sortedMap = null;
			
			countValuesSet = null;
			
			countValues = null;
			// +++++++++++++++++++++++++++++
			
			// Compute the tempCacheSize to make sure the size after inserting
			// the item to be cached the cache size does not overshoot.
			tempCacheSize = currentCacheSize + theRequestedItemSize;
			
			//tempCacheSize = currentCacheSize + theRequestedItem.requestedItemSize;
		}
		/*******************************************************/
		//While Ends
		/*******************************************************/
		
		r = Runtime.getRuntime();
	
		// +++++++++++++++++++++++++++++
		// Try to do a garbage collection
		// +++++++++++++++++++++++++++++
		try 
		{
			// +++++++++++++++++++++++++
			// Invoke Garbage Collection
			// +++++++++++++++++++++++++
			r.gc();
			
			// +++++++++++++++++++++++++++++++++++++++++++
			// Put the currently executing thread to sleep
			// +++++++++++++++++++++++++++++++++++++++++++
			Thread.sleep(100);
			
			// +++++++++++++++++++++++++
			// Invoke Garbage Collection
			// +++++++++++++++++++++++++
			r.gc();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}