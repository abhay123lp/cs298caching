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
public class LRUCache<K,V> {

	private Map<String,String > cacheMap;	// Main Cache Map that holds the Cache of items
	/*private Map<K, Long> cacheRequestMap;	// Holds a map of what item was requested how many times*/	
	private Long cacheHits;					// Counter for the Number of Cache Hits
	private Long cacheMiss;					// Counter for the Number of Cache Misses
	private Long totalNumberOfItems;		// Total Number of Items that were requested from the Cache
	private Long totalRequestedItemSize;	// Tracks the Size of all the items that were requested for the Cache
	private Long totalCacheHitSize;			// Tracks the Size of all the items that were requested from Cache and available in the Cache
	private Long totalCacheMissSize;		// Tracks the Size of all Cache items that were requested Cache did not have
	private LinkedList<String> requestedItemQueue;
	/*private LinkedList<Request> requests;*/
	private int capacity;
		
	public LRUCache(long size)
	{
		capacity = (int) size;
		requestedItemQueue = new LinkedList<String>();
		cacheMap = new HashMap<String, String>(capacity);
		/*cacheRequestMap = new HashMap<K, Long>();*/
		cacheHits = new Long(CacheConstants.ZERO);
		cacheMiss= new Long(CacheConstants.ZERO);
		totalNumberOfItems = new Long(CacheConstants.ZERO);
		totalRequestedItemSize = new Long(CacheConstants.ZERO);
		totalCacheHitSize = new Long(CacheConstants.ZERO);
		totalCacheMissSize = new Long(CacheConstants.ZERO);
		
		/*requests = new LinkedList<Request>();*/
	}
	
	public void cacheItem(Request theRequestedItem)
	{
		/*requests.add(theRequestedItem);*/
		addItem(theRequestedItem);
		/*System.out.println("Request : " + totalNumberOfItems);*/
	}
	
	/**
	 *  
	 * @param requestedItem : This is the item that is requested from the Cache
	 */
	private void addItem(Request theRequestedItem)
	{
		/*//1) Checks if the cache is full(in terms of cache size)?
		//2) Also do a check if the Item exits in the Cache and if it is being
		//	 duplicated.
		if(!checkSize() && !checkItemExistsInCache(requestedItem))
		{
			requestedItemQueue.add(requestedItem);
			cacheMap.put(requestedItem, requestedItem);
		}
		else
		{
			
		}*/
		String requestedItem = theRequestedItem.getRequestedObject();
		int caseId = computeCase(requestedItem);
		
		switch (caseId) {
		case 1:	// Case 1: Cache is Full && Item to be cached is duplicated ---> Cache Hit
				
			requestedItemQueue.remove(requestedItem);	//1)Remove the item from requested LRU Queue
			requestedItemQueue.addLast(requestedItem);	//2)Put the item at the end for LRU Queue
			totalCacheHitSize = totalCacheHitSize + theRequestedItem.requestedItemSize;
			//cacheHits+=1;
			
			cacheHits+=CacheConstants.ONE;
			//System.out.println("Cache Hit.. :" + requestedItem);
			break;
			
		case 2: // Case 2 : Cache is Full && Item to be cached is not duplicated ---> Cache Miss
			
			String itemToBeRemoved = requestedItemQueue.remove();	//1) Remove the LRU item from the LRUQueue
			cacheMap.remove(itemToBeRemoved);						//2) Remove the LRU item from the cacheMap
			requestedItemQueue.addLast(requestedItem);				//3) Add the new Item to be cached at end of LRUQueue
			cacheMap.put(requestedItem, requestedItem);				//4) Cache the new Item in the LRUQueue
			totalCacheMissSize = totalCacheMissSize + theRequestedItem.requestedItemSize;
			//cacheMiss+=1;
			cacheMiss+=CacheConstants.ONE;
			break;
			
		case 3: //Case 3: Cache is not Full && Item to be cached is duplicated ---> Cache Hit
			
			requestedItemQueue.remove(requestedItem);				//1) Remove the requestedItem from the LRUQueue
			requestedItemQueue.addLast(requestedItem);				//2) Add the item to end of LRUQUeue
			/*cacheMap.put(requestedItem, requestedItem);*/
			totalCacheHitSize = totalCacheHitSize + theRequestedItem.requestedItemSize;
			//cacheHits+=1;
			cacheHits+=CacheConstants.ONE;
			//System.out.println("Cache Hit.. :" + requestedItem);
			break;
			
		case 4: //Case 4: Cache is not Full && Item to be cached is not duplicated ---> Cache Miss
			
			requestedItemQueue.addLast(requestedItem);				//1) Add the new Item of end of LRUQueue
			cacheMap.put(requestedItem, requestedItem);				//2) cache the new Item in the cacheMap
			totalCacheMissSize = totalCacheMissSize + theRequestedItem.requestedItemSize;
			//cacheMiss+=1;
			cacheMiss+=CacheConstants.ONE;
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
	
	private int computeCase(String logEntry)
	{

		/*if((cacheMap.size() > capacity || cacheMap.size() == capacity) && requestedItemQueue.contains(logEntry))*/
		if((cacheMap.size() >= capacity) && (requestedItemQueue.contains(logEntry)))
		{	// Case 1: Cache is Full && Item to be cached is duplicated
			return CacheConstants.ONE; 
		}
		/*else if((cacheMap.size() > capacity || cacheMap.size() == capacity) && !requestedItemQueue.contains(logEntry))*/
		else if((cacheMap.size() >= capacity) && (!requestedItemQueue.contains(logEntry)))
		{	// Case 2 : Cache is Full && Item to be cached is not duplicated
			return CacheConstants.TWO;
		}
		else if((cacheMap.size() < capacity) && (requestedItemQueue.contains(logEntry)))
		{	//Case 3: Cache is not Full && Item to be cached is duplicated
			return CacheConstants.THREE;
		}
		else if((cacheMap.size()< capacity) && (!requestedItemQueue.contains(logEntry)))
		{	//Case 4: Cache is not Full && Item to be cached is not duplicated
			return CacheConstants.FOUR;
		}
		
		return CacheConstants.ZERO;
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
	
	/*private boolean checkSize()
	{
		if(cacheMap.size() < capacity)
			return false;
		else if(cacheMap.size() == capacity||cacheMap.size() > capacity)
			return true;
		
		return true;
	}
	
	*//**
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
