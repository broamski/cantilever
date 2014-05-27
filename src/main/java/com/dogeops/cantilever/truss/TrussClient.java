package com.dogeops.cantilever.truss;

import org.apache.log4j.Logger;

import com.dogeops.cantilever.messagequeue.MessageQueueFactory;
import com.dogeops.cantilever.messagequeue.MessageQueueInterface;
import com.dogeops.cantilever.utils.Configuration;
import com.dogeops.cantilever.utils.ConfigurationSingleton;

public class TrussClient {
	private static final Logger logger = Logger.getLogger(TrussClient.class
			.getName());

	public TrussClient() {
	}

	public static void main(String[] args) {
		logger.debug("Truss client starting...");
		Configuration config = new Configuration();
		config.getConfigFile(args);

		MessageQueueFactory mqf = new MessageQueueFactory();

		MessageQueueInterface mqi = mqf
				.getQueue(ConfigurationSingleton.instance
						.getConfigItem("replay.queue.type"));
		mqi.connect(ConfigurationSingleton.instance
				.getConfigItem("replay.queue.hostname"),
				ConfigurationSingleton.instance
						.getConfigItem("replay.queue.queuename"));

		HTTPLogMessageListener ml = new HTTPLogMessageListener();
		mqi.consume(ml);

	}

}
