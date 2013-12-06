package com.minorityhobbies.util.web.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationFilterTest {
	private DummyAuthenticationService authenticationService;
	private List<String> securedUrls;
	private AuthenticationFilter handler;
	private String userPropertiesFile;

	@Before
	public void setUp() throws IOException {
		authenticationService = new DummyAuthenticationService();
		securedUrls = new LinkedList<String>();
		securedUrls.add("/private");
		String prefix = "userConfig";
		userPropertiesFile = File.createTempFile(
				prefix, ".conf").getAbsolutePath();
	}

	@After
	public void cleanUp() {
		assertTrue(new File(userPropertiesFile).delete());
	}

	@Test
	public void shouldCheckSessionForAuthenticationForSecuredUrl() throws IOException, ServletException {
		handler = new AuthenticationFilter(authenticationService,
				userPropertiesFile);
		DummyRequest req = new DummyRequest();
		req.setRequestURI("/private");
		
		HttpSession session = req.getSession();
		session.setAttribute("twitter.accessToken", "anAccessToken");
		DummyResponse res = new DummyResponse();
		
		DummyFilterChain chain = new DummyFilterChain();
		
		handler.doFilter(req, res, chain);
		assertTrue(chain.isInvoked());
		assertNull(res.getRedirect());		
	}
	
	@Test
	public void shouldCheckAuthDbForAuthenticationForSecuredUrl() throws IOException, ServletException {		
		String testAccessToken = "simpleAccessToken";
		String testTokenSecret = "simpleTokenSecret";
		String jkpuid = "userKey";
		Properties userProperties = new Properties();
		userProperties.setProperty(String.format("%s.twitter.accessToken", jkpuid), testAccessToken);
		userProperties.setProperty(String.format("%s.twitter.secret", jkpuid), testTokenSecret);
		FileWriter writer = null;
		try {
			writer = new FileWriter(userPropertiesFile);
			userProperties.store(writer, "");
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		handler = new AuthenticationFilter(authenticationService,
				userPropertiesFile);

		DummyRequest req = new DummyRequest();
		req.addCookie(new Cookie("jkpuid", jkpuid));
		req.setRequestURI("/private");
		DummyResponse res = new DummyResponse();
		
		DummyFilterChain chain = new DummyFilterChain();
		
		// check no redirection
		handler.doFilter(req, res, chain);
		assertTrue(chain.isInvoked());
		assertNull(res.getRedirect());
		
		// check auth details are in session
		HttpSession session = req.getSession();
		assertEquals(testAccessToken, session.getAttribute("twitter.accessToken"));
		assertEquals(testTokenSecret, session.getAttribute("twitter.accessToken.secret"));
	}
}
