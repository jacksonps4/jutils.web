package com.minorityhobbies.util.web.auth;

import com.google.inject.AbstractModule;

public class AuthenticationModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(AuthenticationService.class).to(ScribeAuthenticationService.class);
	}
}
