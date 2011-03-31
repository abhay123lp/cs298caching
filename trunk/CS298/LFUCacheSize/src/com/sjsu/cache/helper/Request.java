package com.sjsu.cache.helper;

/**
 * HTTP Request Class attributes
 * @author Akshay
 *
 */
public class Request {
	
	/** requestTime: UNIX Timestamp for requestTime for the the Object */
	private Float requestTime;
	/*protected Long requestTime;*/ 
	
	/** Processing time for the request in miliseconds */
	private Long requestProcessingTime;
	
	/** The IP Address of the Client */
	private String clientIPAddress; 
	
	/** TCP Miss/ TCP Hit */
	private String tcpCode; 
	
	/** Request Type : GET / POST */
	protected String requestType; 
	
	/** URL of the Requested Object as String */
	public String requestedObject; 
	
	/** URL to which the request is forwarded in case of timeout */
	private String timeOutRedirectionAttribute; 
	
	/** meta information of the requested Object eg: text/html */
	private String requestedObjectType; 
	
	/** Size of the Requested Item in terms of byte */
	public Integer requestedItemSize;

	public void setRequestedObject(String requestedObject) {
		this.requestedObject = requestedObject;
	}

	public String getRequestedObject() {
		return requestedObject;
	}

	public void setTcpCode(String tcpCode) {
		this.tcpCode = tcpCode;
	}

	public String getTcpCode() {
		return tcpCode;
	}

	public void setRequestProcessingTime(Long requestProcessingTime) {
		this.requestProcessingTime = requestProcessingTime;
	}

	public Long getRequestProcessingTime() {
		return requestProcessingTime;
	}

	public void setClientIPAddress(String clientIPAddress) {
		this.clientIPAddress = clientIPAddress;
	}

	public String getClientIPAddress() {
		return clientIPAddress;
	}

	public void setTimeOutRedirectionAttribute(
			String timeOutRedirectionAttribute) {
		this.timeOutRedirectionAttribute = timeOutRedirectionAttribute;
	}

	public String getTimeOutRedirectionAttribute() {
		return timeOutRedirectionAttribute;
	}

	public void setRequestedObjectType(String requestedObjectType) {
		this.requestedObjectType = requestedObjectType;
	}

	public String getRequestedObjectType() {
		return requestedObjectType;
	}

	public void setRequestTime(Float requestTime) {
		this.requestTime = requestTime;
	}

	public Float getRequestTime() {
		return requestTime;
	} 

}
