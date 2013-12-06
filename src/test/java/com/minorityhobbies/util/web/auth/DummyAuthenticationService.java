package com.minorityhobbies.util.web.auth;

import java.io.InputStream;
import java.util.HashMap;

import com.minorityhobbies.util.web.auth.AuthenticationService;
import com.minorityhobbies.util.web.auth.HttpResponse;
import com.minorityhobbies.util.web.auth.SimpleHttpResponse;
import com.minorityhobbies.util.web.auth.SimpleUserToken;
import com.minorityhobbies.util.web.auth.UserToken;

class DummyAuthenticationService implements AuthenticationService {
	private UserToken expectedRequestToken;
	private String expectedVerifier;
	private UserToken validToken;
	
	@Override
	public String getApiCallbackUrl(String key) {
		return "http://foo.bar/blah/blah";
	}

	@Override
	public UserToken getRequestToken() {
		return new SimpleUserToken("testToken", "testTokenSecret");
	}

	@Override
	public String getAuthorisationUrl(UserToken requestToken) {
		return "http://foo.bar/auth";
	}

	public void setExpectedRequestToken(UserToken expectedRequestToken) {
		this.expectedRequestToken = expectedRequestToken;
	}
	
	public void setExpectedVerifier(String expectedVerifier) {
		this.expectedVerifier = expectedVerifier;
	}

	@Override
	public UserToken getAccessToken(UserToken requestToken, String verifier) {
		if (expectedRequestToken == null || !expectedRequestToken.equals(requestToken)) {
			throw new IllegalStateException("Incorrect request token");
		}
		
		if (expectedVerifier == null || !expectedVerifier.equals(verifier)) {
			throw new IllegalStateException("Invalid verifier");
		}
		
		validToken = new SimpleUserToken("validAccessToken", "validAccessTokenSecret");
		return validToken;
	}

	@Override
	public HttpResponse sendSignedRequest(String verb, String url,
			UserToken accessToken) {
		return sendSignedRequest(verb, url, accessToken, null);
	}

	@Override
	public HttpResponse sendSignedRequest(String verb, String url,
			UserToken accessToken, String body) {
		if (!accessToken.equals(validToken)) {
			return new SimpleHttpResponse("Forbidden", new HashMap<String, String>(), "");
		}
		
		return new SimpleHttpResponse("OK", new HashMap<String, String>(),
				"{ \"screen_name\": \"test_user\" }");

	}

	@Override
	public InputStream sendSignedRequestForStream(String verb, String url,
			UserToken accessToken, String requestBody) {
		return null;
	}
}
