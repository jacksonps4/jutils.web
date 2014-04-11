package com.minorityhobbies.util.web;

import java.util.EnumSet;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;

/**
 * Used to adapt a created {@link HttpServletContext} which is independent
 * of the type of web server being used, to a ServletContextListener, which
 * will register all of the servlets and filters from the 
 * {@link HttpServletContext}.
 */
public class HttpServletContextAdapter implements ServletContextListener {
	private final HttpServletContext httpServletContext;

	public HttpServletContextAdapter(HttpServletContext httpServletContext) {
		super();
		this.httpServletContext = httpServletContext;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		for (Map.Entry<String, Filter> filter : httpServletContext.getFilters()
				.entrySet()) {
			FilterRegistration.Dynamic registration = context.addFilter(filter
					.getValue().toString(), filter.getValue());
			registration.addMappingForUrlPatterns(
					EnumSet.allOf(DispatcherType.class), true, filter.getKey());
		}
		
		for (Map.Entry<String, HttpServlet> servlet : httpServletContext.getServlets()
				.entrySet()) {
			ServletRegistration.Dynamic registration = context.addServlet(servlet
					.getValue().toString(), servlet.getValue());
			registration.addMapping(servlet.getKey());
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
}
