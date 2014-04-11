package com.minorityhobbies.util.web.auth;

public class AuthenticationModule {
	private final AuthenticationService authenticationService;

	public AuthenticationModule() {
		super();
		this.authenticationService = new ScribeAuthenticationService();
	}

	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}
}
