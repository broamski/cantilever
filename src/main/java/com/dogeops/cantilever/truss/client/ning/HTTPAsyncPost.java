package com.dogeops.cantilever.truss.client.ning;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.dogeops.cantilever.logreader.HTTPLogObject;
import com.dogeops.cantilever.truss.PostPayloadCache;
import com.dogeops.cantilever.utils.ConfigurationSingleton;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHandler;
import com.ning.http.client.AsyncHandler.STATE;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;

public class HTTPAsyncPost {
	private static final Logger logger = Logger.getLogger(HTTPAsyncPost.class
			.getName());
	private String url_request;

	public HTTPAsyncPost(AsyncHttpClient client, HTTPLogObject http_log) {
		String http_port = ConfigurationSingleton.instance
				.getConfigItem("replay.request.port");
		String http_protocol = ConfigurationSingleton.instance.getConfigItem(
				"replay.request.protocol").toLowerCase();

		if (http_port.equals("80") || http_port.equals("443")) {
			http_port = "";
		} else {
			http_port = ":" + http_port;
		}

		url_request = http_protocol + "://" + http_log.getServerName()
				+ http_log.getRequest() + http_port;

		RequestBuilder builder = new RequestBuilder("POST");

		// Build headers
		String headers[] = http_log.getHeaders();
		for (int i = 0; i < headers.length; i++) {
			String header_key = headers[i].split(":")[0];
			String header_value = headers[i].split(":")[1];
			builder.addHeader(header_key, header_value);
		}
		builder.addHeader("User-Agent", http_log.getUseragent());
		
		String one_time_payload = PostPayloadCache.instance.fetchPayload(http_log.getRequest());
		logger.debug("Here is the randomized payload: " + one_time_payload);
		builder.setBody(one_time_payload);

		Request request = builder.setUrl(url_request).build();

		AsyncHandler<Response> asyncHandler = new AsyncHandler<Response>() {
			private final Response.ResponseBuilder builder = new Response.ResponseBuilder();

			public STATE onBodyPartReceived(final HttpResponseBodyPart content)
					throws Exception {
				builder.accumulate(content);
				return STATE.CONTINUE;
			}

			public STATE onStatusReceived(final HttpResponseStatus status)
					throws Exception {
				builder.accumulate(status);
				return STATE.CONTINUE;
			}

			public STATE onHeadersReceived(final HttpResponseHeaders headers)
					throws Exception {
				builder.accumulate(headers);
				return STATE.CONTINUE;	
			}

			public Response onCompleted() throws Exception {
				logger.debug("POST - " + url_request + " - "
						+ builder.build().getStatusCode());
				return builder.build();
			}

			@Override
			public void onThrowable(Throwable t) {
				logger.error(t.getMessage());
			}
		};

		try {
			client.executeRequest(request, asyncHandler);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
