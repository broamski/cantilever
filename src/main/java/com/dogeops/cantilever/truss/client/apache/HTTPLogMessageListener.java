package com.dogeops.cantilever.truss.client.apache;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

import com.dogeops.cantilever.logreader.HTTPLogObject;
import com.dogeops.cantilever.utils.ConfigurationSingleton;
import com.google.gson.Gson;

public class HTTPLogMessageListener implements MessageListener {
	private static final Logger logger = Logger.getLogger(HTTPLogMessageListener.class
			.getName());
	
	public PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    
	public CloseableHttpClient httpclient = HttpClients.custom()
            .setConnectionManager(cm)
            .build();
	
    public HTTPLogMessageListener() {
    	String str_truss_threads = ConfigurationSingleton.instance.getConfigItem("truss.threads");
    	int truss_threads = Integer.parseInt(str_truss_threads);
    	cm.setMaxTotal(truss_threads);
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
				    	new HTTPGetRequest(http_log, httpclient).start();
				    	break;
				    case POST:
				    	new HTTPPostRequest(http_log, httpclient).start();
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
