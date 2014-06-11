package com.dogeops.cantilever.truss.client.ning;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.dogeops.cantilever.logreader.HTTPLogObject;
import com.dogeops.cantilever.truss.PostPayloadCache;
import com.dogeops.cantilever.utils.ConfigurationSingleton;
import com.ning.http.client.AsyncHandler;
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

		RequestBuilder builder = new RequestBuilder("POST");
		url_request = http_log.getURLRequest();

		HTTPUtils util = new HTTPUtils();
		builder = util.buildHeaders(builder, http_log);
		builder.addHeader("User-Agent", http_log.getUseragent());
		
		String one_time_payload = PostPayloadCache.instance.fetchPayload(http_log.getRequest());
		logger.debug("Here is the randomized payload: " + one_time_payload);
		builder.setBody(one_time_payload);

		Request request = builder.setUrl(http_log.getURLRequest()).build();

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
