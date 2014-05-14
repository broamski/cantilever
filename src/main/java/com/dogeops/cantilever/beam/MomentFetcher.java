package com.dogeops.cantilever.beam;

import java.util.Calendar;

import org.apache.log4j.Logger;

public class MomentFetcher implements Runnable {
	private static final Logger logger = Logger.getLogger(MomentFetcher.class
			.getName());
	private Calendar cal;

	public MomentFetcher(Calendar startTime) {
		this.cal = startTime;
	}

	public void run() {
		logger.debug("Fetching all events for " + this.cal.getTime());

		// Here is where we'll fetch the logs matching this time stamp from
		// ElasticSearch, craft a pay load, then place it on a message bus

		// This simulates the passing of time.
		this.cal.add(Calendar.SECOND, 1);
	}
}
