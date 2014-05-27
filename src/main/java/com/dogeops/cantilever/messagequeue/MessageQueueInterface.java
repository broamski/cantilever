package com.dogeops.cantilever.messagequeue;

import com.dogeops.cantilever.truss.HTTPLogMessageListener;

public interface MessageQueueInterface {
	public void connect(String hostname, String queue_name);
	public void disconnect();
	public void deliver(String payload);
	public void consume(HTTPLogMessageListener ml);
}
