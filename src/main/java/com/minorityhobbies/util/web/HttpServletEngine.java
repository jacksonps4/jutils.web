package com.minorityhobbies.util.web;

import java.io.Closeable;

import javax.servlet.http.HttpServlet;

public interface HttpServletEngine extends Runnable, Closeable {
	void addServlet(HttpServlet servlet, String contextPath);
	void setPort(int port);
	void setHtmlDocRoot(String root);
}
