package com.dogeops.cantilever.logreader;

public class HTTPLogObject {
	private String timestamp, method, request_uri, payload;
	int request_size;
	
	public HTTPLogObject(String timestamp, String method, String request_uri) {
		this.timestamp = timestamp;
		this.method = method;
		this.request_uri = request_uri;
	}
	
	public void setPayload(String payload) {
		this.payload = payload;
	}
}
