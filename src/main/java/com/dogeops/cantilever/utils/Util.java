package com.dogeops.cantilever.utils;

import org.apache.log4j.Logger;

public class Util {
	public static void cease(Logger logger, String message) {
		logger.error(message);
		System.exit(1);
	}
}
