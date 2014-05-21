package com.dogeops.cantilever.messagequeue;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import com.dogeops.cantilever.utils.Util;

public class ActiveMQ implements MessageQueueInterface {
	private static final Logger logger = Logger.getLogger(ActiveMQ.class
			.getName());

	public ActiveMQConnectionFactory connectionFactory;
	public Connection connection;
	public Session session;
	public Destination destination;
	public MessageProducer producer;

	public void connect(String hostname, String queue_name) {
		try {
			logger.debug("Connecting to ActiveMQ Broker: " + hostname + " - Queue: " + queue_name);
			connectionFactory = new ActiveMQConnectionFactory(hostname);
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queue_name);
		} catch (JMSException e) {
			Util.cease(logger, e.getMessage());
		}
	}

	public void disconnect() {
		try {
			// We need to get some more details in this log message...
			logger.debug("Disconnecting from ActiveMQ Broker");
			connection.close();
		} catch (JMSException e) {
			Util.cease(logger, e.getMessage());
		}
	}

	public void deliver(String payload) {
		try {
			// We need to get some more details in this log message...
			logger.debug("Writing message to queue.");
			// Create a MessageProducer from the Session to the Topic or Queue
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// Create a message
			TextMessage message;
			message = session.createTextMessage(payload);

			// Tell the producer to send the message
			producer.send(message);
		} catch (JMSException e) {
			Util.cease(logger, e.getMessage());
		}
	}

}
