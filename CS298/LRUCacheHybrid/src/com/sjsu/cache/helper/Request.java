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
	private String requestType; 
	
	/** URL of the Requested Object as String */
	private String requestedObject; 
	
	/** URL to which the request is forwarded in case of timeout */
	private String timeOutRedirectionAttribute; 
	
	/** meta information of the requested Object eg: text/html */
	private String requestedObjectType; 
	
	/** Size of the Requested Item in terms of byte */
	private Integer requestedItemSize;
	
	private String domainName;

	public Float getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Float requestTime) {
		this.requestTime = requestTime;
	}

	public Long getRequestProcessingTime() {
		return requestProcessingTime;
	}

	public void setRequestProcessingTime(Long requestProcessingTime) {
		this.requestProcessingTime = requestProcessingTime;
	}

	public String getClientIPAddress() {
		return clientIPAddress;
	}

	public void setClientIPAddress(String clientIPAddress) {
		this.clientIPAddress = clientIPAddress;
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

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public void setRequestedObject(String requestedObject) {
		this.requestedObject = requestedObject;
	}

	public String getRequestedObject() {
		return requestedObject;
	} 

}
