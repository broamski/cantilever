package com.dogeops.cantilever.truss;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import com.dogeops.cantilever.logreader.HTTPLogObject;
import com.dogeops.cantilever.utils.ConfigurationSingleton;

public class HTTPGetRequest extends Thread{
	private static final Logger logger = Logger.getLogger(HTTPGetRequest.class
			.getName());
    
	private CloseableHttpClient httpClient;
    private HttpContext context;
    private HttpGet httpget;
    private HTTPLogObject http_log;
    
	public HTTPGetRequest(HTTPLogObject http_log, CloseableHttpClient httpClient) {
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
			this.httpget = new HttpGet(request);
			
			// Build headers
			String headers[] = http_log.getHeaders();
			for (int i = 0; i < headers.length; i++)
			{
				String header_key = headers[i].split(":")[0];
				String header_value = headers[i].split(":")[1];
				httpget.addHeader(header_key, header_value);
			}
			httpget.addHeader("User-Agent", http_log.getUseragent());

			CloseableHttpResponse response = httpClient.execute(httpget, context);
			
		    logger.debug("GET " + "http://" + http_log.getServerName() + http_log.getRequest() + " - " + response.getStatusLine());
		    response.close();
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
