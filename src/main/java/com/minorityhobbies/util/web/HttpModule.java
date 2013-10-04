package com.minorityhobbies.util.web;

import java.util.ServiceLoader;

import com.minorityhobbies.util.Dependencies;

@Deprecated
public class HttpModule extends Dependencies {
	private final ServiceLoader<HttpServer> serverFactory = ServiceLoader
			.load(HttpServer.class);
	private final HttpHandlers httpHandlers;
	private HttpServer server;

	public HttpModule(HttpHandlers handlers) {
		this.httpHandlers = handlers;
	}

	public final HttpServer getHttpServer() {
		if (server == null) {
			server = serverFactory.iterator().next();
			for (HttpRequestHandler handler : httpHandlers.getHandlers()) {
				server.addRequestHandler(handler);
			}
		}
		return server;
	}
}
