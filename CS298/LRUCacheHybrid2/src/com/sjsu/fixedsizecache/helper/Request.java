package com.sjsu.fixedsizecache.helper;

/**
 * HTTP Request Class attributes
 * @author Akshay
 *
 */
public class Request {
	
	/** requestTime: UNIX Timestamp for requestTime for the the Object */
	protected Float requestTime;
	/*protected Long requestTime;*/ 
	
	/** Processing time for the request in miliseconds */
	protected Long requestProcessingTime;
	
	/** The IP Address of the Client */
	protected String clientIPAddress; 
	
	/** TCP Miss/ TCP Hit */
	protected String tcpCode; 
	
	/** Request Type : GET / POST */
	protected String requestType; 
	
	/** URL of the Requested Object as String */
	public String requestedObject; 
	
	/** URL to which the request is forwarded in case of timeout */
	protected String timeOutRedirectionAttribute; 
	
	/** meta information of the requested Object eg: text/html */
	protected String requestedObjectType; 
	
	/** Size of the Requested Item in terms of byte */
	public Integer requestedItemSize;

	public void setRequestedObject(String requestedObject) {
		this.requestedObject = requestedObject;
	}

	public String getRequestedObject() {
		return requestedObject;
	} 

}
