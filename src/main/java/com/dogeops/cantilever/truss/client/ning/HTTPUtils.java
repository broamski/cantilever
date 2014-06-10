package com.dogeops.cantilever.truss.client.ning;

import com.dogeops.cantilever.logreader.HTTPLogObject;
import com.ning.http.client.RequestBuilder;

public class HTTPUtils {
	public RequestBuilder buildHeaders(RequestBuilder request, HTTPLogObject http_log) {
		String headers[] = http_log.getHeaders();
		for (int i = 0; i < headers.length; i++) {
			String header_key = headers[i].split(":")[0];
			String header_value = headers[i].split(":")[1];
			request.addHeader(header_key, header_value);
		}
		return request;
	}
}
