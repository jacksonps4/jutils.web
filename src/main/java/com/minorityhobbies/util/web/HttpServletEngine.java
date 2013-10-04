package com.minorityhobbies.util.web;

import java.io.Closeable;

import javax.servlet.Filter;

public interface HttpServletEngine extends Runnable, Closeable {
	void addContext(HttpServletContext context);
	void addRootFilter(Filter filter, String pathSpec);
	void setPort(int port);
	void setHtmlDocRoot(String root);
}
