package com.minorityhobbies.util.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

/**
 * A simple web server context. Allows the addition of servlets and handlers.
 */
public final class HttpServletContext {
	private final String contextPath;
	private final Map<String, HttpServlet> servlets = new HashMap<String, HttpServlet>();
	private final Map<String, Filter> filters = new HashMap<String, Filter>();

	public HttpServletContext(String contextPath) {
		super();
		this.contextPath = contextPath;
	}

	public void addServlet(HttpServlet servlet, String pathSpec) {
		servlets.put(pathSpec, servlet);
	}

	public void addFilter(Filter filter, String pathSpec) {
		filters.put(pathSpec, filter);
	}

	public String getContextPath() {
		return contextPath;
	}

	public Map<String, HttpServlet> getServlets() {
		return new HashMap<String, HttpServlet>(servlets);
	}

	public Map<String, Filter> getFilters() {
		return new HashMap<String, Filter>(filters);
	}
}
