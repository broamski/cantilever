package com.dogeops.cantilever.beam;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;

import com.dogeops.cantilever.logreader.HTTPLogObject;
import com.dogeops.cantilever.logreader.ReplayCache;
import com.dogeops.cantilever.messagequeue.MessageQueueFactory;
import com.dogeops.cantilever.messagequeue.MessageQueueInterface;
import com.dogeops.cantilever.utils.ConfigurationSingleton;

public class MomentFetcher {
	private static final Logger logger = Logger.getLogger(MomentFetcher.class
			.getName());
	private Calendar cal;

	public MomentFetcher(Calendar startTime) {
		this.cal = startTime;
		String timestamp = DateFormatUtils.format(this.cal,
				ConfigurationSingleton.instance
						.getConfigItem("replay.dateformat"));

		ArrayList<HTTPLogObject> thing = ReplayCache.instance
				.gimmieCache(timestamp);

		logger.debug(thing.size() + " events for " + timestamp);
		
		// ZZZ - This needs to be re-worked to NOT reconnect every second.
		// ### - Maybe move to LoopControl and have MomentFecter return
		// ### - the ArrayList?
		MessageQueueFactory mqf = new MessageQueueFactory();
		
		MessageQueueInterface mqi = mqf.getQueue(ConfigurationSingleton.instance
				.getConfigItem("replay.queue.type"));
		mqi.connect(ConfigurationSingleton.instance
				.getConfigItem("replay.queue.hostname"), ConfigurationSingleton.instance
				.getConfigItem("replay.queue.queuename"));
		
		for (HTTPLogObject s : thing) {
			mqi.deliver(s.toString());
			logger.debug(s.request_uri);
		}
		mqi.disconnect();
		this.cal.add(Calendar.SECOND, 1);
	}
}
