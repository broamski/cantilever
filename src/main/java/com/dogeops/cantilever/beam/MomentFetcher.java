package com.dogeops.cantilever.beam;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;

import com.dogeops.cantilever.logreader.HTTPLogObject;
import com.dogeops.cantilever.logreader.ReplayCache;
import com.dogeops.cantilever.messagequeue.MessageQueueInterface;
import com.dogeops.cantilever.utils.ConfigurationSingleton;

public class MomentFetcher {
	private static final Logger logger = Logger.getLogger(MomentFetcher.class
			.getName());
	private Calendar cal;

	public MomentFetcher(Calendar startTime, MessageQueueInterface mqi) {
		this.cal = startTime;
		String timestamp = DateFormatUtils.format(this.cal,
				ConfigurationSingleton.instance
						.getConfigItem("replay.dateformat"));

		ArrayList<HTTPLogObject> thing = ReplayCache.instance
				.gimmieCache(timestamp);

		logger.info(thing.size() + " events for " + timestamp);
		
		for (HTTPLogObject s : thing) {
			mqi.deliver(s.getPayload());
			logger.debug(s.getPayload());
		}
		this.cal.add(Calendar.SECOND, 1);
	}
}
