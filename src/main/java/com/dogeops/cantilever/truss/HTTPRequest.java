package com.dogeops.cantilever.truss;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

public class HTTPRequest {
	private static final Logger logger = Logger.getLogger(HTTPRequest.class
			.getName());
	
	public HTTPRequest(String url, String[] headers, String host) {
		String request = "http://" + host + url;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(request);
			
			// Build headers
			for (int i = 0; i < headers.length; i++)
			{
				String header_key = headers[i].split(":")[0];
				String header_value = headers[i].split(":")[1];
				httpGet.addHeader(header_key, header_value);
			}

			CloseableHttpResponse response1 = httpclient.execute(httpGet);
		    logger.debug("GET " + "http://" + host + url + " - " + response1.getStatusLine());
		    httpclient.close();
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
