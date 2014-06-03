package com.dogeops.cantilever.truss;

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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.dogeops.cantilever.logreader.HTTPLogObject;
import com.dogeops.cantilever.utils.ConfigurationSingleton;

public class HTTPPostRequest {
	private static final Logger logger = Logger.getLogger(HTTPPostRequest.class
			.getName());

	public HTTPPostRequest(HTTPLogObject http_log) {
		
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
			HttpPost httpPost = new HttpPost(request);
			// ToDo: Populate this externally. Config?
			List <NameValuePair> nvps = new ArrayList <NameValuePair>();
			nvps.add(new BasicNameValuePair("username", "us3r"));
			nvps.add(new BasicNameValuePair("password", "p@ss"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			logger.debug("POST " + http_protocol + " - " + response.getStatusLine());
		    httpclient.close();
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
