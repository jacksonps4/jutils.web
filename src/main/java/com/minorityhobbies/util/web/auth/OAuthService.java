package com.minorityhobbies.util.web.auth;

import java.io.InputStream;

public class OAuthService {
	private final AuthenticationService authService;
	private final AuthenticatedUserRepository userRepository;

	public OAuthService(AuthenticationService authService,
			AuthenticatedUserRepository userRepository) {
		super();
		this.authService = authService;
		this.userRepository = userRepository;
	}

	public InputStream sendRequest(String verb, String url, String tokenKey,
			String payload) {
		UserToken accessToken = userRepository.getUserToken(tokenKey);
		return authService.sendSignedRequestForStream(verb, url, accessToken,
				payload);
	}
}
