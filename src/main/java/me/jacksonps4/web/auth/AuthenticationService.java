package me.jacksonps4.web.auth;

import java.io.InputStream;

public interface AuthenticationService {
	String getApiCallbackUrl(String key);

	UserToken getRequestToken();

	String getAuthorisationUrl(UserToken requestToken);

	UserToken getAccessToken(UserToken requestToken, String verifier);

	HttpResponse sendSignedRequest(String verb, String url,
			UserToken accessToken);

	HttpResponse sendSignedRequest(String verb, String url,
			UserToken accessToken, String body);
	
	InputStream sendSignedRequestForStream(String verb, String url,
			UserToken accessToken, String requestBody);
}
