package com.dogeops.cantilever.truss;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;

public enum PostPayloadCache {
	instance;
	private static final Logger logger = Logger
			.getLogger(PostPayloadCache.class.getName());

	private Hashtable<String, ArrayList<String>> cache = new Hashtable<String, ArrayList<String>>();
	
	public void addToCache(String key, String value) {
		if (cache.containsKey(key)) {
			cache.get(key).add(value);
		} else {
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(value);
			cache.put(key, temp);
		}
	}
	
	public String fetchPayload(String key){
		if (cache.containsKey(key))
		{
			ArrayList<String> tmp_fetch = cache.get(key);
			int random = 0 + (int)(Math.random()*tmp_fetch.size() -1); 
			return tmp_fetch.get(random);
		}
		else
		{
			return "";
		}
	}
	
	// Maybe we'll need to return the entire array?
	public ArrayList<String> fetchPayloadArray(String key){
		if (cache.containsKey(key))
		{
			return cache.get(key);
		}
		else
		{
			return new ArrayList<String>();
		}
	}
}
