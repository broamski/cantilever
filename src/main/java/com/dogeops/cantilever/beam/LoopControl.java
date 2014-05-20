package com.dogeops.cantilever.beam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.dogeops.cantilever.utils.ConfigurationSingleton;
import com.dogeops.cantilever.utils.Util;

public class LoopControl {
	private static final Logger logger = Logger.getLogger(LoopControl.class
			.getName());

	public void execute() {
		String inputDateFormat = ConfigurationSingleton.instance
				.getConfigItem("replay.dateformat");
		SimpleDateFormat dateFormat = new SimpleDateFormat(inputDateFormat);

		try {
			String inputStartTime = ConfigurationSingleton.instance
					.getConfigItem("replay.starttime");
			Date tmpStart = dateFormat.parse(inputStartTime);
			Calendar startTime = new GregorianCalendar(
					TimeZone.getTimeZone("GMT"));
			startTime.setTime(tmpStart);

			String inputEndTime = ConfigurationSingleton.instance
					.getConfigItem("replay.endtime");
			Date tmpEnd = dateFormat.parse(inputEndTime);
			Calendar endTime = new GregorianCalendar(
					TimeZone.getTimeZone("GMT"));
			endTime.setTime(tmpEnd);

			// How long to run the scheduler.
			long run_time = (endTime.getTimeInMillis() - startTime
					.getTimeInMillis());

			logger.debug("Staring at: " + startTime.getTime()
					+ " and Ending at: " + endTime.getTime() + ", running for "
					+ run_time + " ms");

			for (int i = 0; i <= (run_time / 1000); i++) {
				new MomentFetcher(startTime);
				Thread.sleep(1000);
			}

		} catch (ParseException e) {
			Util.cease(logger, e.getMessage());
		} catch (InterruptedException e) {
			Util.cease(logger, e.getMessage());
		}
	}
}