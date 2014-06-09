package com.dogeops.cantilever.messagequeue;

import com.dogeops.cantilever.truss.client.ning.HTTPAsyncLogMessageListener;

public interface MessageQueueInterface {
	public void connect(String hostname, String queue_name);
	public void disconnect();
	public void deliver(String payload);
	public void consume(HTTPAsyncLogMessageListener ml);
}
