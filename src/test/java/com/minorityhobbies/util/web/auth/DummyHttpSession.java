package com.minorityhobbies.util.web.auth;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public class DummyHttpSession implements HttpSession {
	private final long creationTime = System.currentTimeMillis();
	private final String id = UUID.randomUUID().toString();
	private final Map<String, Object> sessionData = new HashMap<String, Object>();
	private long lastAccessed = -1;
	
	@Override
	public long getCreationTime() {
		return creationTime;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public long getLastAccessedTime() {
		return lastAccessed;
	}

	@Override
	public ServletContext getServletContext() {
		return null;
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
	}

	@Override
	public int getMaxInactiveInterval() {
		return 0;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return null;
	}

	@Override
	public Object getAttribute(String name) {
		lastAccessed = System.currentTimeMillis();
		return sessionData.get(name);
	}

	@Override
	public Object getValue(String name) {
		return getAttribute(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		lastAccessed = System.currentTimeMillis();
		final Iterator<String> itr = sessionData.keySet().iterator();
		return new Enumeration<String>() {
			@Override
			public boolean hasMoreElements() {
				return itr.hasNext();
			}

			@Override
			public String nextElement() {
				return itr.next();
			}
		};
	}

	@Override
	public String[] getValueNames() {
		lastAccessed = System.currentTimeMillis();
		return sessionData.keySet().toArray(new String[sessionData.size()]);
	}

	@Override
	public void setAttribute(String name, Object value) {
		lastAccessed = System.currentTimeMillis();
		sessionData.put(name, value);
	}

	@Override
	public void putValue(String name, Object value) {
		setAttribute(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		lastAccessed = System.currentTimeMillis();
		sessionData.remove(name);
	}

	@Override
	public void removeValue(String name) {
		removeAttribute(name);
	}

	@Override
	public void invalidate() {
	}

	@Override
	public boolean isNew() {
		return false;
	}
}
