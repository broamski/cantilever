package com.dogeops.cantilever.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum ConfigurationSingleton {
	instance;

	private Properties props = new Properties();

	private ConfigurationSingleton() {
	}

	public void readConfigFile(String config_path) {

		try {
			InputStream input = new FileInputStream(config_path);
			props.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getConfigItem(String val) {
		try {
			if (props.getProperty(val) == null) {
				throw new Exception("Configuration item " + val
						+ " is missing from config");
			}
			return props.getProperty(val);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
			return null;
		}
	}
}
