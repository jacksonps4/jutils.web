package com.minorityhobbies.util.web.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class DummyFilterChain implements FilterChain {
	private volatile boolean invoked;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response)
			throws IOException, ServletException {
		invoked = true;
	}

	public boolean isInvoked() {
		return invoked;
	}
}
