package com.minorityhobbies.util.web.auth;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Named;
import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Provides;

public class AuthenticationModule extends AbstractModule {
	/**
	 * Annotates the authentication filter.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
	@BindingAnnotation
	public @interface AuthFilter {
	}
	
	/**
	 * Annotates the authentication callback servlet.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
	@BindingAnnotation
	public @interface AuthCallback {
	}

	@Override
	protected void configure() {
		bind(AuthenticationService.class).to(ScribeAuthenticationService.class);
	}

	@Provides
	@AuthFilter
	public Filter getAuthenticationFilter(
			AuthenticationService authenticationService,
			@Named("jacksonps4.web.userstore") String userPropertiesFile)
			throws IOException {
		return new AuthenticationFilter(authenticationService,
				userPropertiesFile);
	}

	@Provides
	@AuthCallback
	public HttpServlet getOAuthCallbackServlet(
			AuthenticationService authenticationService,
			@Named("jacksonps4.web.userstore") String userPropertiesFile)
			throws IOException {
		return new AuthCallbackServlet(authenticationService,
				userPropertiesFile);
	}
}
