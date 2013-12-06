package com.minorityhobbies.util.web.auth;

import java.util.Map;

public interface HttpResponse {
	String getHttpMessage();
	Map<String, String> getHeaders();
	String getBody();
}