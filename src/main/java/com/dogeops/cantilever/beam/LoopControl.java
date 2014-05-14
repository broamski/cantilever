package com.dogeops.cantilever.beam;

import static java.util.concurrent.TimeUnit.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;

import com.dogeops.cantilever.utils.ConfigurationSingleton;
import com.dogeops.cantilever.utils.Util;

public class LoopControl {
	private static final Logger logger = Logger.getLogger(LoopControl.class
			.getName());
	private final static ScheduledExecutorService scheduler = Executors
			.newSingleThreadScheduledExecutor();

	public void execute() {
		String inputDateFormat = ConfigurationSingleton.instance
				.getConfigItem("replay.dateformat");
		SimpleDateFormat dateFormat = new SimpleDateFormat(inputDateFormat);

		// get start and stop times from config singleton
		// and convert them to calendar objects

		try {
			String inputStartTime = ConfigurationSingleton.instance
					.getConfigItem("replay.starttime");
			Date tmpStart = dateFormat.parse(inputStartTime);
			Calendar startTime = new GregorianCalendar();
			startTime.setTime(tmpStart);
			
			String inputEndTime = ConfigurationSingleton.instance
					.getConfigItem("replay.endtime");
			Date tmpEnd = dateFormat.parse(inputEndTime);
			Calendar endTime = new GregorianCalendar();
			endTime.setTime(tmpEnd);
			
			// How long to run the scheduler.
			long run_time = (endTime.getTimeInMillis() - startTime
					.getTimeInMillis());

			logger.debug("Staring at: " + startTime.getTime()
					+ " and Ending at: " + endTime.getTime() + ", running for "
					+ run_time + " ms");

			timeTravel(startTime, run_time);
		} catch (ParseException e) {
			Util.cease(logger, e.getMessage());
		}
	}

	public static void timeTravel(Calendar startTime, long run_time) {

		final ScheduledFuture<?> timeTravelHandle = scheduler.scheduleAtFixedRate(
				new MomentFetcher(startTime), 0, 1, SECONDS);
		scheduler.schedule(new Runnable() {
			public void run() {
				timeTravelHandle.cancel(true);
				scheduler.shutdown();
			}
		}, run_time, MILLISECONDS);
	}
}