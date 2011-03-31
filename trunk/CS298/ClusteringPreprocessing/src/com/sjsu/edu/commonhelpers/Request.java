package com.sjsu.edu.commonhelpers;

/**
 * HTTP Request Class attributes
 * @author Akshay
 *
 */
public class Request {
	
	/** requestTime: UNIX Timestamp for requestTime for the the Object */
	private String requestTime;
	/*protected Long requestTime;*/ 
	
	/** Processing time for the request in miliseconds */
	private Long requestProcessingTime;
	
	/** The IP Address of the Client */
	private String clientIPAddress; 

	/** TCP Miss/ TCP Hit */
	private String tcpCode; 
	
	/** Request Type : GET / POST */
	private String requestType; 
	
	/** URL of the Requested Object as String */
	private String requestedURL; 
	
	/** URL to which the request is forwarded in case of timeout */
	private String timeOutRedirectionAttribute; 
	
	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public Long getRequestProcessingTime() {
		return requestProcessingTime;
	}

	public void setRequestProcessingTime(Long requestProcessingTime) {
		this.requestProcessingTime = requestProcessingTime;
	}

	public String getTcpCode() {
		return tcpCode;
	}

	public void setTcpCode(String tcpCode) {
		this.tcpCode = tcpCode;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getTimeOutRedirectionAttribute() {
		return timeOutRedirectionAttribute;
	}

	public void setTimeOutRedirectionAttribute(String timeOutRedirectionAttribute) {
		this.timeOutRedirectionAttribute = timeOutRedirectionAttribute;
	}

	public String getRequestedObjectType() {
		return requestedObjectType;
	}

	public void setRequestedObjectType(String requestedObjectType) {
		this.requestedObjectType = requestedObjectType;
	}

	public Integer getRequestedItemSize() {
		return requestedItemSize;
	}

	public void setRequestedItemSize(Integer requestedItemSize) {
		this.requestedItemSize = requestedItemSize;
	}

	/** meta information of the requested Object eg: text/html */
	private String requestedObjectType; 
	
	/** Size of the Requested Item in terms of byte */
	private Integer requestedItemSize;

	public void setRequestedURL(String requestedObject) {
		this.requestedURL = requestedObject;
	}

	public String getRequestedURL() {
		return requestedURL;
	}
	
	public String getClientIPAddress() {
		return clientIPAddress;
	}

	public void setClientIPAddress(String clientIPAddress) {
		this.clientIPAddress = clientIPAddress;
	}

}
