package com.minorityhobbies.util.web.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

public class AuthCallbackServletTest {
	private DummyAuthenticationService authenticationService;
	private String userPropertiesFile;
	private HttpServlet handler;

	@Before
	public void setUp() throws IOException {
		authenticationService = new DummyAuthenticationService();
		String prefix = "userConfig";
		userPropertiesFile = File.createTempFile(prefix, ".conf")
				.getAbsolutePath();
	}

	@Test
	public void shouldPersistAccessTokenOnCallback() throws IOException,
			ServletException {
		handler = new AuthCallbackServlet(authenticationService,
				userPropertiesFile);

		String requestToken = "requestToken";
		String requestTokenSecret = "requestTokenSecret";
		String oauthVerifier = "tokenVerifier";

		authenticationService.setExpectedRequestToken(new SimpleUserToken(
				requestToken, requestTokenSecret));
		authenticationService.setExpectedVerifier(oauthVerifier);

		DummyRequest req = new DummyRequest();
		req.setRequestURI("/blah/blah");
		HttpSession session = req.getSession();
		session.setAttribute("twitter.requestToken", requestToken);
		session.setAttribute("twitter.requestToken.secret", requestTokenSecret);

		req.setParameter("oauth_verifier", oauthVerifier);

		DummyResponse res = new DummyResponse();

		handler.service(req, res);
		assertNull(res.getRedirect());

		assertEquals("validAccessToken",
				session.getAttribute("twitter.accessToken"));
		assertEquals("validAccessTokenSecret",
				session.getAttribute("twitter.accessToken.secret"));
		assertEquals("test_user", session.getAttribute("twitter.user"));
	}
}
