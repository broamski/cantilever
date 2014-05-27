package com.dogeops.cantilever.utils;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Configuration {
	private static void usage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Usage options:", options);
	}
	
	public void getConfigFile(String[] args) {
		Options opt = new Options();
		Option op = new Option("config", true,
				"Full path to config file: /opt/cantilever/cantilever.config");
		op.setRequired(true);
		opt.addOption(op);

		CommandLineParser parser = new BasicParser();
		CommandLine cmd;

		try {
			cmd = parser.parse(opt, args);
			ConfigurationSingleton.instance.readConfigFile(cmd
					.getOptionValue("config"));

		} catch (ParseException pe) {
			usage(opt);
		}
	}
}
