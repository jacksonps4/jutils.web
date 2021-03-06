package com.minorityhobbies.util.web.auth;

import java.util.Map;

public class SimpleHttpResponse implements HttpResponse {
	private final String message;
	private final Map<String, String> headers;
	private final String body;

	public SimpleHttpResponse(String message, Map<String, String> headers,
			String body) {
		super();
		this.message = message;
		this.headers = headers;
		this.body = body;
	}

	public final String getHttpMessage() {
		return message;
	}

	public final Map<String, String> getHeaders() {
		return headers;
	}

	public final String getBody() {
		return body;
	}

	@Override
	public String toString() {
		StringBuilder resp = new StringBuilder();
		resp.append(String.format("%s%n", message));
		for (Map.Entry<String, String> header : headers.entrySet()) {
			resp.append(String.format("%s: %s%n", header.getKey(),
					header.getValue()));
		}
		resp.append("\n");
		resp.append(body);
		return resp.toString();
	}
}