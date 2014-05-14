package com.dogeops.cantilever.beam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

import static org.elasticsearch.node.NodeBuilder.*;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;

import com.dogeops.cantilever.logreader.HTTPLogReader;
import com.dogeops.cantilever.utils.ConfigurationSingleton;

public class BeamServer {
	private static final Logger logger = Logger.getLogger(BeamServer.class
			.getName());

	private static void usage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("Usage options:", options);
	}

	public static void main(String[] args) {
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

		String pickup_dir = ConfigurationSingleton.instance
				.getConfigItem("log.pickupdir");
		// Boolean convert =
		// Boolean.valueOf(ConfigurationSingleton.instance.getConfigItem("log.convert"));
		// String convert_type =
		// ConfigurationSingleton.instance.getConfigItem("log.convert.type").toLowerCase();
		//
		// if (convert) {
		// HTTPLogConverter converter = new HTTPLogConverter(convert_type);
		// converter.convert(pickup_dir);
		// }

		HTTPLogReader log_reader = new HTTPLogReader();
		ArrayList<String> parsed_logs = log_reader.readLogs(pickup_dir);
		
		// Launch ES and index parsed_logs
		//Node node = nodeBuilder().clusterName("cantilever").node();
		//Client client = node.client();

		//for (String s: parsed_logs) {
		//	logger.debug(s);
		//	IndexResponse response = client.prepareIndex("httplogs", "request")
		//	        .setSource(s)
		//	        .execute()
		//	        .actionGet();
		//}
		
		// According to replay.starttime and replay.endtime, read from index
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
