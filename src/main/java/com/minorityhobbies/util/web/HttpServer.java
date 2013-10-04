package com.minorityhobbies.util.web;

import java.io.Closeable;

@Deprecated
public interface HttpServer extends Runnable, Closeable {
	void addRequestHandler(HttpRequestHandler handler);
	void setPort(int port);
	void setRootPath(String root);
}
