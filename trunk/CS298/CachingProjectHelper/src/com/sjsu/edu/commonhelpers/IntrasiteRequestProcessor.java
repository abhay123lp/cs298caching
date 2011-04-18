package com.sjsu.edu.commonhelpers;

import com.sjsu.edu.commonconstants.CacheConstants;

public class IntrasiteRequestProcessor {
	
	public static Request processRequest(String logLine)
	{
		
		Request request = null;
		
		String[] fields = logLine.split(" ");
		
		request = createRequestObject(fields);
			
		return request;
		
	}
	
	private static Request createRequestObject(String[] fields)
	{
		Request request = new Request();
		
		//Long requestTime = new Long(fields[0].toString());
		String requestTimeString = fields[0].trim();
		
		Long requestTime = processRequestTime(requestTimeString);
		
		Long requestProcessingTime = new Long(fields[1]);
		String clientIPAddress = fields[2].trim();
		String tcpCode = fields[3].trim();
		Long requestedItemSize = new Long(fields[4].toString());
		String requestedObject = fields[6].trim();
		String URL = processURL(requestedObject);
		 
		 
		//requestParam[7] --> '-'
		String timeOutRedirectionAttribute = fields[8].trim();
		String requestedObjectType = fields[9].trim();
		
		request.setRequestTime(requestTime);
		request.setRequestProcessingTime(requestProcessingTime);
		request.setClientIPAddress(clientIPAddress);
		request.setTcpCode(tcpCode);
		request.setRequestedItemSize(requestedItemSize);
		request.setRequestedURL(URL);
		request.setTimeOutRedirectionAttribute(timeOutRedirectionAttribute);
		request.setRequestedObjectType(requestedObjectType);
	
		return request;
	}
	
	/**
	 * Get the request time 
	 * 
	 * @param requestTimeStr
	 * @return
	 */
	private static Long processRequestTime(String requestTimeStr)
	{		
		Double d = new Double(requestTimeStr);
		
		Long l = new Long((long) (d * CacheConstants.THOUSAND));
	    
		return l;
	}
	
	/**
	 * Helper to process the URL
	 */
	private static String processURL(String URL)
	{
		//URL : http://www.gmail.com/skd/
		URL = URL.trim();
		
		int index = URL.indexOf("//");
		int index2 = -1;
		
		//www.gmail.com/skd/
		URL = URL.substring(index + CacheConstants.TWO);

		/*index2 = URL.indexOf("/");
		
		if(index2>-1)
		{
			URL = 
				URL.substring(CacheConstants.ZERO,index2);
		}
		else
		{
			URL = 
				URL.substring(CacheConstants.ZERO);
		}		
		
		if(URL.contains(":"))
		{
			int index3 = URL.indexOf(":");
			
			URL = 
				URL.substring(CacheConstants.ZERO,index3);
		}*/
		
	/*	if (uniqueDomainNames.containsKey(URL)) {
			
			Long count = uniqueDomainNames.get(URL);
			count+=1;
			uniqueDomainNames.put(URL, count);
			
		}else{
			
			Long count = new Long(CacheConstants.ONE);
			uniqueDomainNames.put(URL, count);
		}*/
		
		return URL;
	}
	

}
