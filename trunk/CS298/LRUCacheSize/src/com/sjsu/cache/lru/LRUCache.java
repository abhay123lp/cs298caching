package com.sjsu.cache.lru;

import java.util.HashMap;

import java.util.LinkedList;
import java.util.Map;

import com.sjsu.cache.helper.Request;
import com.sjsu.edu.cache.constants.CacheConstants;

/**
 * This is implementation of the LRU Cache<br/>
 * @author <b>Akshay Shenoy</b><br/>
 * 
 * @copyrights <b>reserved</b> by Akshay Shenoy
 *  
 */
public class LRUCache {

	// Main Cache Map that holds the Cache of items
	//private Map<String,String> cacheMap;	
	
	// Main Cache Map that holds the Cache of items
	private Map<String,Long> cacheMap;		
	
	// Holds a map of what item was requested how many times*/
	/*private Map<K, Long> cacheRequestMap;*/
	
	// Counter for the Number of Cache Hits
	private Long cacheHits;					
	
	// Counter for the Number of Cache Misses
	private Long cacheMiss;					
	
	// Total Number of Items that were requested from the Cache
	private Long totalNumberOfItems;		
	
	// Tracks the Size of all the items that were requested for the Cache
	private Long totalRequestedItemSize;	
	
	// Tracks the Size of all the items that were requested from Cache and available in the Cache
	private Long totalCacheHitSize;			
	
	// Tracks the Size of all Cache items that were requested Cache did not have
	private Long totalCacheMissSize;		
	
	private LinkedList<String> requestedItemQueue;
	private int capacity;
	private Long currentCacheSize;
	private Long maxCacheSize;
		
	public LRUCache(Long size)	
	{
		//capacity = (int) size;
		requestedItemQueue = new LinkedList<String>();
		//cacheMap = new HashMap<String, Long>(capacity);
		cacheMap = new HashMap<String, Long>();
		cacheHits = new Long(CacheConstants.ZERO);
		cacheMiss= new Long(CacheConstants.ZERO);
		totalNumberOfItems = new Long(CacheConstants.ZERO);
		totalRequestedItemSize = new Long(CacheConstants.ZERO);
		totalCacheHitSize = new Long(CacheConstants.ZERO);
		totalCacheMissSize = new Long(CacheConstants.ZERO);
		maxCacheSize = size;
		currentCacheSize = new Long(0);
	}
	
	public Long getMaxCacheSize() {
		return maxCacheSize;
	}

	public Long getCurrentCacheSize() {
		return currentCacheSize;
	}

	public void cacheItem(Request theRequestedItem)
	{
		addItem(theRequestedItem);
	}
	
	/**
	 *  
	 * @param requestedItem : This is the item that is requested from the Cache
	 */
	private void addItem(Request theRequestedItem)
	{
		String requestedItem = theRequestedItem.getRequestedObject();
		
		int caseId = computeCase(theRequestedItem);
			
		switch (caseId) 
		{				
			case 2: // Case 2 : Cache is Full && Item to be cached is not duplicated ---> Cache Miss
				
				//1) Remove the LRU item from the LRUQueue
				//itemToBeRemoved = requestedItemQueue.remove();			
				
				//2) Remove the LRU item from the cacheMap
				//cacheMap.remove(itemToBeRemoved);							
				
				//Edit the Cache
				editCache(theRequestedItem);
				
				//3) Add the new Item to be cached at end of LRUQueue
				requestedItemQueue.addLast(requestedItem);									
				
				//4) Cache the new Item in the LRUQueue
				cacheMap.put(requestedItem, new Long(theRequestedItem.requestedItemSize));	
				
				//Update the parameter for the Cache Miss Size
				totalCacheMissSize = totalCacheMissSize 
									+ theRequestedItem.requestedItemSize;
				
				//Update the parameter for the number of Cache Misses
				cacheMiss+=CacheConstants.ONE;
		
				//Update the current Cache Size reflect the item added to the Cache
				currentCacheSize = currentCacheSize 
									+ theRequestedItem.requestedItemSize;
				
				break;
				
			case 1:	// Case 1: Cache is Full && Item to be cached is duplicated ---> Cache Hit
				
				//1)Remove the item from requested LRU Queue
				requestedItemQueue.remove(requestedItem);	
				
				//2)Put the item at the end for LRU Queue
				requestedItemQueue.addLast(requestedItem);	
				
				//Update the Parameter for the total Cache Hit Size
				totalCacheHitSize = totalCacheHitSize 
								   + theRequestedItem.requestedItemSize;
				
				//Update the parameter for the number of Cache Hits
				cacheHits+=CacheConstants.ONE;
				
				/*//Update the current Cache Size reflect the item added to the Cache
				currentCacheSize = currentCacheSize 
								  + theRequestedItem.requestedItemSize;*/
				
				break;
				
			/*case 3: //Case 3: Cache is not Full && Item to be cached is duplicated ---> Cache Hit
				
				//1) Remove the requestedItem from the LRUQueue
				requestedItemQueue.remove(requestedItem);				
				
				//2) Add the item to end of LRUQUeue
				requestedItemQueue.addLast(requestedItem);				
				
				//Update the parameter that keeps track of Total Cache Hit Size
				totalCacheHitSize = totalCacheHitSize 
									+ theRequestedItem.requestedItemSize;
				
				//Update the parameter that tracks the number of Cache Hits
				cacheHits+=CacheConstants.ONE;
				
				//Since the item to be Cached is already in cache we do not need to update
				// the current Cache Size
				
				break;*/
				
			case 4: //Case 4: Cache is not Full && Item to be cached is not duplicated ---> Cache Miss
				
				//1) Add the new Item of end of LRUQueue
				requestedItemQueue.addLast(requestedItem);				
				
				//2) cache the new Item in the cacheMap
				//cacheMap.put(requestedItem, requestedItem);			
				
				//2) cache the new Item in the cacheMap
				cacheMap.put(requestedItem, new Long(theRequestedItem.requestedItemSize));		
				
				//Update the Parameter that keeps track of the Cache Miss Size
				totalCacheMissSize = totalCacheMissSize 
								+ theRequestedItem.requestedItemSize;
				
				//Update the parameter that keeps track of the number
				//of Cache Misses
				cacheMiss+=CacheConstants.ONE;
				
				//Update the current Cache Size reflect the item added to the Cache
				currentCacheSize = currentCacheSize
								+ theRequestedItem.requestedItemSize;
				
				break;
				
			case 0: // default case
				System.out.println("Case 0 : LRUCache 98 -> addItem()");
				break;
	
			default: //default case
				System.out.println("Defualt case: LRUCache 102-> addItem()");
				break;
		}
		
		totalRequestedItemSize = totalRequestedItemSize + theRequestedItem.requestedItemSize;
		totalNumberOfItems +=CacheConstants.ONE;		
	}
	
	/**
	 * Computes the Use Case to Be Executed
	 * 
	 * @param theRequestedItem
	 * @return
	 */
	//private int computeCase(String logEntry)
	private int computeCase(Request theRequestedItem)
	{
		String logEntry = theRequestedItem.getRequestedObject();
		
		Long tempSize = theRequestedItem.requestedItemSize 
											+ currentCacheSize;
				
		//if((cacheMap.size() >= maxCacheSize) && (requestedItemQueue.contains(logEntry)))
		//if((tempSize >= maxCacheSize) && (requestedItemQueue.contains(logEntry)))
		//if((tempSize > maxCacheSize) && (requestedItemQueue.contains(logEntry)))
		if((requestedItemQueue.contains(logEntry)))
		{	// Case 1: Cache is Full && Item to be cached is duplicated
			return CacheConstants.ONE;
		}
		//else if((cacheMap.size() >= capacity) && (!requestedItemQueue.contains(logEntry)))
		//else if((tempSize >= maxCacheSize) && (!requestedItemQueue.contains(logEntry)))
		else if((tempSize > maxCacheSize) && (!requestedItemQueue.contains(logEntry)))
		{	// Case 2 : Cache is Full && Item to be cached is not duplicated
			return CacheConstants.TWO;
		}
		//else if((cacheMap.size() < capacity) && (requestedItemQueue.contains(logEntry)))
		//else if((tempSize < maxCacheSize) && (requestedItemQueue.contains(logEntry)))
		/*else if((tempSize <= maxCacheSize) && (requestedItemQueue.contains(logEntry)))
		{	//Case 3: Cache is not Full && Item to be cached is duplicated
			return CacheConstants.THREE;
		}*/
		//else if((cacheMap.size()< capacity) && (!rwequestedItemQueue.contains(logEntry)))
		//else if((tempSize < maxCacheSize) && (!requestedItemQueue.contains(logEntry)))
		else if((tempSize <= maxCacheSize) && (!requestedItemQueue.contains(logEntry)))
		{	//Case 4: Cache is not Full && Item to be cached is not duplicated
			return CacheConstants.FOUR;
		}
		
		return CacheConstants.ZERO;
	}

	/**
	 * 
	 * Edits the Cache to reduce the size to include the new item
	 * to be cached
	 * 
	 */
	private void editCache(Request theRequestedItem)
	{
		Long tempSize = null;
		String theFirstItem = null;
		Long currentItemSize = null;
		
		tempSize = currentCacheSize + theRequestedItem.requestedItemSize; 
		
		while(tempSize > maxCacheSize)
		{
			if(requestedItemQueue.size() > 0)
			{
				theFirstItem = requestedItemQueue.remove();
				
				currentItemSize = cacheMap.get(theFirstItem);
				
				cacheMap.remove(theFirstItem);
				
				currentCacheSize = currentCacheSize - currentItemSize;
				
				tempSize = currentCacheSize + theRequestedItem.requestedItemSize;

			}
			else
			{				
				break;
			}
		}
		
	}
		
	public Long getCacheHits() {
		return cacheHits;
	}

	public Long getCacheMiss() {
		return cacheMiss;
	}

	public Long getTotalNumberOfItems() {
		return totalNumberOfItems;
	}

	public Long getTotalRequestedItemSize() {
		return totalRequestedItemSize;
	}

	public Long getTotalCacheHitSize() {
		return totalCacheHitSize;
	}

	public Long getTotalCacheMissSize() {
		return totalCacheMissSize;
	}
	
	public int getCacheMapSize()
	{
		return cacheMap.size();
	}
	
	/**
	 * 
	 * 1)Check if the Item already exists in the cache.<br/>
	 * 2)If exists then, remove the item from the Queue of Cache that is handle<br/> 
	 * 	 of cache entries in temporal order and add the item at end of the queue<br/>
	 * 	 to keep the LRU item queue updated
	 * 
	 * @param logEntry :
	 * @return
	 *//*
	protected boolean checkItemExistsInCache(String logEntry)
	{
		if(requestedItemQueue.contains(logEntry) && cacheMap.containsKey(logEntry))
		{
			requestedItemQueue.remove(logEntry);
			requestedItemQueue.add(logEntry);
			return true;
		}
		else
			return false;
	}*/
}

