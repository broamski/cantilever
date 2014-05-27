package com.dogeops.cantilever.beam;

import org.apache.log4j.Logger;

import com.dogeops.cantilever.logreader.HTTPLogReader;
import com.dogeops.cantilever.utils.Configuration;
import com.dogeops.cantilever.utils.ConfigurationSingleton;

public class BeamServer {
	private static final Logger logger = Logger.getLogger(BeamServer.class
			.getName());

	public static void main(String[] args) {
		Configuration config = new Configuration();
		config.getConfigFile(args);

		String pickup_dir = ConfigurationSingleton.instance
				.getConfigItem("log.pickupdir");

		HTTPLogReader log_reader = new HTTPLogReader();
		log_reader.readLogs(pickup_dir);
		
		// According to replay.starttime and replay.endtime, read from cache
		// Craft transaction payload and drop it on queue, AMQ first, AWS SQS later
		LoopControl lc = new LoopControl();
		lc.execute();
		
		// Other Notes:
		// Need a config section to reference POST payload file
		// Need to figure out:
		// /some/url/${var1}/${var2}
		// Whether var1 or 2 will be stored in an external file?

	}
}
