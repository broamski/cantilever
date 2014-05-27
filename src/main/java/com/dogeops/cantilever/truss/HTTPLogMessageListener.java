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
	
	public void onMessage(Message message) {
		 if (message instanceof TextMessage) {
             TextMessage textMessage = (TextMessage) message;
             String text;
			try {
				text = textMessage.getText();
				Gson gson = new Gson();
				HTTPLogObject http_log = gson.fromJson(text, HTTPLogObject.class);
				new HTTPRequest(http_log);
				logger.debug("Received: " + text);
			} catch (JMSException e) {
				e.printStackTrace();
			}
         } else {
             logger.error("Received an erroneous message: " + message);
         }

	}

}
