package com.minorityhobbies.util.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Deprecated
public interface HttpRequestHandler {
	boolean serviceRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;
}
