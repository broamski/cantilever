package com.dogeops.cantilever.truss.client.ning;

import org.apache.log4j.Logger;

import com.dogeops.cantilever.logreader.HTTPLogObject;
import com.dogeops.cantilever.truss.GETHeaderCache;
import com.dogeops.cantilever.truss.POSTHeaderCache;
import com.dogeops.cantilever.utils.ConfigurationSingleton;
import com.ning.http.client.RequestBuilder;

public class HTTPUtils {
	private static final Logger logger = Logger.getLogger(HTTPUtils.class
			.getName());
	
	public RequestBuilder buildDefaultHeaders(RequestBuilder request, HTTPLogObject http_log) {
		String headers[] = http_log.getHeaders();
		for (int i = 0; i < headers.length; i++) {
			try {
				String header_key = headers[i].split(":")[0].trim();
				String header_value = headers[i].split(":")[1].trim();
				request.addHeader(header_key, header_value);
			}
			catch (Exception e) {
				logger.trace("No or invalid header detected for " + http_log.getRequest() + ".");
			}
		}
		return request;
	}
	
	public RequestBuilder buildCustomGETHeaders(RequestBuilder request, HTTPLogObject http_log) {
		String one_time_GET_header = GETHeaderCache.instance.fetchPayload(http_log.getRequest());
		String header_delimiter = ConfigurationSingleton.instance
				.getConfigItem("replay.request.headers.delimiter");
		String[] headers = one_time_GET_header.split(header_delimiter);
		for (int i = 0; i < headers.length; i++) {
			try {
				String header_key = headers[i].split(":")[0].trim();
				String header_value = headers[i].split(":")[1].trim();
				request.addHeader(header_key, header_value);
			}
			catch (Exception e) {
				logger.trace("No or invalid header detected for  " + http_log.getRequest() + ".");
			}
		}
		return request;
	}
	
	public RequestBuilder buildCustomPOSTHeaders(RequestBuilder request, HTTPLogObject http_log) {
		String one_time_POST_header = POSTHeaderCache.instance.fetchPayload(http_log.getRequest());
		String header_delimiter = ConfigurationSingleton.instance
				.getConfigItem("replay.request.headers.delimiter");
		String[] headers = one_time_POST_header.split(header_delimiter);
		for (int i = 0; i < headers.length; i++) {
			try {
				String header_key = headers[i].split(":")[0].trim();
				String header_value = headers[i].split(":")[1].trim();
				request.addHeader(header_key, header_value);
			}
			catch (Exception e) {
				logger.trace("No or invalid header detected for  " + http_log.getRequest() + ".");
			}
		}
		return request;
	}
}
