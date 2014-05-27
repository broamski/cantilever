package com.dogeops.cantilever.truss;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.dogeops.cantilever.logreader.HTTPLogObject;
import com.dogeops.cantilever.utils.ConfigurationSingleton;

public class HTTPRequest {
	private static final Logger logger = Logger.getLogger(HTTPRequest.class
			.getName());
	
	//public HTTPRequest(String url, String[] headers, String host) {
	public HTTPRequest(HTTPLogObject http_log) {
		
		// I'm unsure why we access these particular config items from the 
		// config singleton and not-prop them like we do in the HTTP Log Object
		// Low priority, but should be reviewed at some point 
		
		String http_port = ConfigurationSingleton.instance.getConfigItem("replay.request.port");
		String http_protocol = ConfigurationSingleton.instance.getConfigItem("replay.request.protocol").toLowerCase();
		
		if (http_port.equals("80") || http_port.equals("443")) {
			http_port = "";
		}
		else {
			http_port = ":"  + http_port;
		}
		
		String request = http_protocol + "://" + http_log.getServerName() + http_log.getRequest() + http_port;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(request);
			
			// Build headers
			String headers[] = http_log.getHeaders();
			for (int i = 0; i < headers.length; i++)
			{
				String header_key = headers[i].split(":")[0];
				String header_value = headers[i].split(":")[1];
				httpGet.addHeader(header_key, header_value);
			}

			CloseableHttpResponse response1 = httpclient.execute(httpGet);
		    logger.debug("GET " + "http://" + http_log.getServerName() + http_log.getRequest() + " - " + response1.getStatusLine());
		    httpclient.close();
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
