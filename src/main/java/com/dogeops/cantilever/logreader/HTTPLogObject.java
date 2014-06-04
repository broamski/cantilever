package com.dogeops.cantilever.logreader;

import com.dogeops.cantilever.utils.ConfigurationSingleton;
import com.google.gson.Gson;

public class HTTPLogObject {
	private String timestamp, method, request, bytes, useragent;
	private String requesting_resver, request_payload;
	private String[] headers;
	
	public HTTPLogObject(String timestamp, String method, String request,
			String bytes, String useragent) {
		this.timestamp = timestamp;
		this.method = method;
		this.request = request;
		this.bytes = bytes;
		this.useragent = useragent;
		
		buildRequestPayload();
	}
	
	private void buildHeaderArray() {
		String header_input = ConfigurationSingleton.instance
				.getConfigItem("replay.request.headers");
		String header_delimiter = ConfigurationSingleton.instance
				.getConfigItem("replay.request.headers.delimiter");
		this.headers = header_input.split(header_delimiter);
	}
	
	private void buildRequestPayload() {
		this.requesting_resver = ConfigurationSingleton.instance
				.getConfigItem("replay.request.servername");
		buildHeaderArray();
		Gson gson = new Gson();
		this.request_payload = gson.toJson(this);
	}
	
	public String getTimestamp() {
		return this.timestamp;
	}
	
	public String getPayload() {
		return this.request_payload;
	}
	
	public String getRequest() {
		return this.request;
	}
	
	public String[] getHeaders() {
		return this.headers;
	}
	
	public String getServerName() {
		return this.requesting_resver;
	}
	
	public String getMethod() {
		return this.method;
	}
	
	public String getUseragent() {
		return this.useragent;
	}
}
