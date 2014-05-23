package com.dogeops.cantilever.logreader;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;

public enum ReplayCache {
	instance;
	
	private static final Logger logger = Logger
			.getLogger(ReplayCache.class.getName());
	
	private Hashtable<String, ArrayList<HTTPLogObject>> cache = new Hashtable<String, ArrayList<HTTPLogObject>>();
	
	public void addToCache(HTTPLogObject input_request) {
		logger.debug("Adding " + input_request.getTimestamp() + " to cache");
		if (cache.containsKey(input_request.getTimestamp())) {
			cache.get(input_request.getTimestamp()).add(input_request);
		} else {
			ArrayList<HTTPLogObject> temp = new ArrayList<HTTPLogObject>();
			temp.add(input_request);
			cache.put(input_request.getTimestamp(), temp);
		}
	}
	
	public ArrayList<HTTPLogObject> gimmieCache(String input_timestamp)
	{
		if (cache.containsKey(input_timestamp))
		{
			return cache.get(input_timestamp);
		}
		else
		{
			return new ArrayList<HTTPLogObject>();
		}
	}
}
