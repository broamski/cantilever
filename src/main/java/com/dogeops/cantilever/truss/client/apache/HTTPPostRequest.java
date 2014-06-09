package com.dogeops.cantilever.truss.client.apache;

/* 
 * This is an extremely basic implementation until it is determined
 * how to differentiate and insert the POST entity using the config file
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import com.dogeops.cantilever.logreader.HTTPLogObject;
import com.dogeops.cantilever.utils.ConfigurationSingleton;

public class HTTPPostRequest extends Thread {
	private static final Logger logger = Logger.getLogger(HTTPPostRequest.class
			.getName());
	
	private CloseableHttpClient httpClient;
    private HttpContext context;
    private HttpPost httppost;
    private HTTPLogObject http_log;

    public HTTPPostRequest(HTTPLogObject http_log, CloseableHttpClient httpClient) {
    	this.http_log = http_log;
    	this.httpClient = httpClient;
    	this.context = new BasicHttpContext();
    }
    
	public void run() {
		
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
			this.httppost = new HttpPost(request);
			
			// Build headers
			String headers[] = http_log.getHeaders();
			for (int i = 0; i < headers.length; i++)
			{
				String header_key = headers[i].split(":")[0];
				String header_value = headers[i].split(":")[1];
				httppost.addHeader(header_key, header_value);
			}
			
			httppost.addHeader("User-Agent", http_log.getUseragent());
			
			// ToDo: Populate this externally. Config?
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("username", "us3r"));
			nvps.add(new BasicNameValuePair("password", "p@ss"));
			httppost.setEntity(new UrlEncodedFormEntity(nvps));
			
			CloseableHttpResponse response = httpClient.execute(httppost);
			
			logger.debug("POST " + request + " - " + response.getStatusLine());
			response.close();
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
