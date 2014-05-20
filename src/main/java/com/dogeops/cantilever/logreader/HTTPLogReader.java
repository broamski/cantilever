package com.dogeops.cantilever.logreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.dogeops.cantilever.utils.ConfigurationSingleton;
import com.dogeops.cantilever.utils.Timer;

import static com.dogeops.cantilever.utils.Util.cease;

public class HTTPLogReader {
	private static final Logger logger = Logger.getLogger(HTTPLogReader.class
			.getName());
	private int fileCount = 0;

	public void readLogs(String path) {
		logger.info("Reading logs in " + path + "....");
		String log_regex = ConfigurationSingleton.instance
				.getConfigItem("log.convert.regex");
		Timer t = new Timer();
		t.start();
		File folder = new File(path);
		if (folder.exists()) {
			for (File file : folder.listFiles()) {
				fileCount++;
				logger.info("Pasring file: " + file.getAbsolutePath());
				try {
					BufferedReader br = new BufferedReader(new FileReader(
							file.getAbsolutePath()));
					String line = null;
					while ((line = br.readLine()) != null) {
						logger.debug(line);
						line = line.trim();

						Pattern p = Pattern.compile(log_regex);
						Matcher m = p.matcher(line);
						m.find();
						
						HTTPLogObject http_log = new HTTPLogObject(
								m.group("TIMESTAMP"), m.group("METHOD"),
								m.group("REQUEST"));
						http_log.setPayload(m.group("bytes"));
						
						ReplayCache.instance.addToCache(http_log);
					
						logger.debug(http_log);
					}
				} catch (FileNotFoundException e) {
					cease(logger, e.getMessage());
				} catch (IOException e) {
					cease(logger, e.getMessage());
				}
			}
			t.stop();
			logger.info(fileCount + " files parsed in " + t.getElapsed() + " ms");
		} else {
			cease(logger, "Could not find folder " + path);	
		}
	}
}
