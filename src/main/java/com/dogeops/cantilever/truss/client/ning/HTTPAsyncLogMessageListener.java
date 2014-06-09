package com.dogeops.cantilever.truss.client.ning;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.dogeops.cantilever.logreader.HTTPLogObject;
import com.google.gson.Gson;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.AsyncHttpClientConfig.Builder;

public class HTTPAsyncLogMessageListener implements MessageListener {
	private static final Logger logger = Logger.getLogger(HTTPAsyncLogMessageListener.class
			.getName());
	
	private Builder builder;
	private AsyncHttpClient client;

    public HTTPAsyncLogMessageListener() {
		builder = new AsyncHttpClientConfig.Builder();
		builder.setCompressionEnabled(true)
			.setMaximumConnectionsPerHost(100)
		    .setRequestTimeoutInMs(15000)
		    .build();
		
		client = new AsyncHttpClient(builder.build());
    }
	
	public enum AcceptableMethods {
		GET, POST, NAN;
		public static AcceptableMethods getValue(String str) {
			try {
				return valueOf(str);
			} catch (Exception e) {
				logger.error(e.getMessage());
				return NAN;
			}
		}
	}
	
	public void onMessage(Message message) {
		 if (message instanceof TextMessage) {
             TextMessage textMessage = (TextMessage) message;
             String text;
			try {
				text = textMessage.getText();
				Gson gson = new Gson();
				HTTPLogObject http_log = gson.fromJson(text, HTTPLogObject.class);
				
				switch (AcceptableMethods.getValue(http_log.getMethod()))
				{
				    case GET:
				    	new HTTPAsyncGet(client, http_log);
				    	break;
				    case POST:
				    	new HTTPAsyncPost(client, http_log);
				    	break;
				    default:
				    	logger.error("Unsupport HTTP Method: " + http_log.getMethod());
				}
				
				logger.trace("Received: " + text);
			} catch (JMSException e) {
				logger.error(e.getMessage());
			}
         } else {
			logger.error("Received an erroneous message: " + message);
		}

	}

}
