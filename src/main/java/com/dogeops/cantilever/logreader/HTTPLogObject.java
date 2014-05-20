package com.dogeops.cantilever.logreader;

public class HTTPLogObject {
	public String timestamp, method, request_uri, request_size;
	
	public HTTPLogObject(String timestamp, String method, String request_uri) {
		this.timestamp = timestamp;
		this.method = method;
		this.request_uri = request_uri;
	}
	
	public void setPayload(String string) {
		this.request_size = string;
	}
	
	public String toString() {
		return "Timestamp: " + this.timestamp + " Method: " + this.method + " Request: " + this.request_uri;
		
	}
}
