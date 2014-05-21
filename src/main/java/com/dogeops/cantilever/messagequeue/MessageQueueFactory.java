package com.dogeops.cantilever.messagequeue;

import org.apache.log4j.Logger;

import com.dogeops.cantilever.utils.Util;

public class MessageQueueFactory {
	private static final Logger logger = Logger.getLogger(MessageQueueFactory.class
			.getName());

	public enum QueueTypes {
		AMQ, SQS, NAN;
		public static QueueTypes toQueue(String str) {
			try {
				return valueOf(str);
			} catch (Exception ex) {
				return NAN;
			}
		}
	}

	public MessageQueueInterface getQueue(String type) {
		
		switch (QueueTypes.toQueue(type))
		{
		    case AMQ:
		    	return new ActiveMQ();
		    case SQS:
		    	return new AmazonSQS();
		    default:
		    	Util.cease(logger, "Invalid Message Queue. Quitting.");
		        return null;
		}
	}
}
