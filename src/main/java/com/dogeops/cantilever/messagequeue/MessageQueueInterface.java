package com.dogeops.cantilever.messagequeue;

public interface MessageQueueInterface {
	public void connect (String hostname, String queue_name);
	public void disconnect ();
	public void deliver(String payload);
}
