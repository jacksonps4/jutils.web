package me.jacksonps4.web.auth;

import java.util.Map;

public interface HttpResponse {
	String getHttpMessage();
	Map<String, String> getHeaders();
	String getBody();
}