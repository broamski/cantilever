package com.dogeops.cantilever.truss;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException; 

import org.apache.log4j.Logger;

import com.dogeops.cantilever.utils.ConfigurationSingleton;

public class POSTHeaderCacheBuilder {
	private static final Logger logger = Logger.getLogger(POSTHeaderCacheBuilder.class
			.getName());
	private final String post_config_key = "truss.post.headers";

	public POSTHeaderCacheBuilder() {
		File pattern_file = new File(ConfigurationSingleton.instance
				.getConfigItem(post_config_key));
		
		if (pattern_file.exists())
		{
			logger.debug("Inspecting Pattern File: " + pattern_file);
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(
						pattern_file.getAbsolutePath()));
				String line = null;
				while ((line = br.readLine()) != null) {
					
					try {
						String line2 = null;
						BufferedReader truth_reader = new BufferedReader(new FileReader(
								line.split(":")[1]));
						logger.debug("Processing for Request: " + line.split(":")[0] + " - File: " + line.split(":")[1]);
						while ((line2 = truth_reader.readLine()) != null) {
							POSTHeaderCache.instance.addToCache(line.split(":")[0], line2);
							logger.debug("Adding Key: " + line.split(":")[0] + ", Value: " + line2 + ", to POST cache.");
						}
						truth_reader.close();
					} catch (FileNotFoundException e){
						logger.error(e.getMessage());
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		else {
			logger.error("Value " + post_config_key + " was empty, so I didn't process any data. Your results may vary.");
		}
	}
}
