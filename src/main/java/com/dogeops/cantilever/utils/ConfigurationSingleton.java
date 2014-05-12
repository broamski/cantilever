package com.dogeops.cantilever.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import static com.dogeops.cantilever.utils.Util.cease;

public enum ConfigurationSingleton {
	instance;
	private static final Logger logger = Logger
			.getLogger(ConfigurationSingleton.class.getName());

	private Properties props = new Properties();

	private ConfigurationSingleton() {
	}

	public void readConfigFile(String config_path) {
		try {
			InputStream input = new FileInputStream(config_path);
			props.load(input);
			input.close();
		} catch (IOException e) {
			cease(logger, e.getMessage());
		}
	}

	public String getConfigItem(String val) {
		try {
			if (props.getProperty(val) == null) {
				throw new Exception("The property " + val
						+ " is missing from the configurtion file.");
			}
			return props.getProperty(val);
		} catch (Exception e) {
			cease(logger, e.getMessage());
			return null;
		}
	}
}
