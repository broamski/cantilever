package com.dogeops.cantilever.truss;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.dogeops.cantilever.logreader.HTTPLogObject;
import com.google.gson.Gson;

public class HTTPLogMessageListener implements MessageListener {
	private static final Logger logger = Logger.getLogger(HTTPLogMessageListener.class
			.getName());
	
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
				    	new HTTPGetRequest(http_log);
				    	break;
				    case POST:
				    	logger.info("Unimplemented HTTP Method...as of now");
				    	break;
				    default:
				    	logger.error("Unsupport HTTP Method: " + http_log.getMethod());
				}
				
				logger.debug("Received: " + text);
			} catch (JMSException e) {
				logger.error(e.getMessage());
			}
         } else {
			logger.error("Received an erroneous message: " + message);
		}

	}

}
