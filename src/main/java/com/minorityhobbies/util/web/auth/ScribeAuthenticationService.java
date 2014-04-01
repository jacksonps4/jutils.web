package com.minorityhobbies.util.web.auth;

import java.io.InputStream;
import java.util.Map;
import java.util.ResourceBundle;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

final class ScribeAuthenticationService implements
		AuthenticationService {
	private final OAuthService service;
	private final ResourceBundle auth;
	private final String apiCallbackUrl;

	public ScribeAuthenticationService() {
		super();
		auth = ResourceBundle.getBundle("me.jacksonps4.web.auth");
		String apiKey = auth.getString("twitter.api.key");
		String apiSecret = auth.getString("twitter.api.secret");
		apiCallbackUrl = auth.getString("twitter.callback.url");

		service = new ServiceBuilder().provider(TwitterApi.SSL.class)
				.apiKey(apiKey).apiSecret(apiSecret)
				.callback(apiCallbackUrl).build();
	}

	public String getApiCallbackUrl(String key) {
		return apiCallbackUrl;
	}
	
	@Override
	public UserToken getRequestToken() {
		Token token = service.getRequestToken();
		return new SimpleUserToken(token.getToken(), token.getSecret());
	}

	@Override
	public String getAuthorisationUrl(UserToken requestToken) {
		return service.getAuthorizationUrl(new Token(requestToken
				.getTokenId(), requestToken.getTokenSecret()));
	}

	@Override
	public UserToken getAccessToken(UserToken requestToken, String verifier) {
		Token accessToken = service.getAccessToken(
				new Token(requestToken.getTokenId(), requestToken
						.getTokenSecret()), new Verifier(verifier));
		return new SimpleUserToken(accessToken.getToken(),
				accessToken.getSecret());
	}

	@Override
	public HttpResponse sendSignedRequest(String verb, String url,
			UserToken accessToken) {
		return sendSignedRequest(verb, url, accessToken, null);
	}

	@Override
	public HttpResponse sendSignedRequest(String verb, String url,
			UserToken accessToken, String requestBody) {
		OAuthRequest credReq = new OAuthRequest(Verb.valueOf(verb),
		url);
		if (requestBody != null) {
			String[] pairs = requestBody.split("&");
			for (String pair : pairs) {
				String key = pair.substring(0, pair.indexOf("=")).trim();
				String value = pair.substring(pair.indexOf("=") + 1, pair.length()).trim();
				credReq.addBodyParameter(key, value);
			}
		}
		service.signRequest(
				new Token(accessToken.getTokenId(), accessToken
						.getTokenSecret()), credReq);
		Response result = credReq.send();
		
		String body = result.getBody();
		Map<String, String> headers = result.getHeaders();
		String message = result.getMessage();
		return new SimpleHttpResponse(message, headers, body);
	}
	
	@Override
	public InputStream sendSignedRequestForStream(String verb, String url,
			UserToken accessToken, String requestBody) {
		OAuthRequest credReq = new OAuthRequest(Verb.valueOf(verb),
		url);
		if (requestBody != null) {
			String[] pairs = requestBody.split("&");
			for (String pair : pairs) {
				String key = pair.substring(0, pair.indexOf("=")).trim();
				String value = pair.substring(pair.indexOf("=") + 1, pair.length()).trim();
				credReq.addBodyParameter(key, value);
			}
		}
		service.signRequest(
				new Token(accessToken.getTokenId(), accessToken
						.getTokenSecret()), credReq);
		Response result = credReq.send();
		
		return result.getStream();
	}
}