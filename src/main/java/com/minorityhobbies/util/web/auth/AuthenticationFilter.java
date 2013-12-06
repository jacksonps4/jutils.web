package me.jacksonps4.web.auth;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class AuthenticationFilter implements Filter {
	private final AuthenticationService authenticationService;
	private final Properties userAuth;
	private final String devMode;

	public AuthenticationFilter(AuthenticationService authenticationService,
			String userPropertiesFile) throws IOException {
		super();

		this.authenticationService = authenticationService;

		userAuth = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(userPropertiesFile);
			userAuth.load(in);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		this.devMode = userAuth.getProperty("devmode");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain filterChain) throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		HttpSession session = request.getSession();
		if (devMode != null) {
			String user = (String) session.getAttribute("twitter.user");
			if (user == null) {
				session.setAttribute("twitter.user", devMode);
			}
			filterChain.doFilter(req, res);
			return;
		}

		String target = request.getRequestURI();

		// authentication required: is it in the session already?
		String twitterUser = (String) session
				.getAttribute("twitter.accessToken");
		if (twitterUser != null) {
			// got an access token already
			filterChain.doFilter(req, res);
			return;
		}

		// if not, is it stored?
		// cookie named 'jkpuid' gives the key to access the store for
		// the user
		Cookie[] cookies = request.getCookies();
		Cookie userCookie = null;
		for (Cookie cookie : cookies) {
			if ("jkpuid".equals(cookie.getName())) {
				userCookie = cookie;
				break;
			}
		}
		if (userCookie != null) {
			String jkpuid = userCookie.getValue();
			String token = (String) userAuth.get(String.format(
					"%s.twitter.accessToken", jkpuid));
			String tokenSecret = (String) userAuth.get(String.format(
					"%s.twitter.secret", jkpuid));

			// put details into the session
			session.setAttribute("twitter.accessToken", token);
			session.setAttribute("twitter.accessToken.secret", tokenSecret);
			session.setMaxInactiveInterval(-1);
		} else {
			// no token: request from OAuth API
			UserToken requestToken = authenticationService.getRequestToken();
			session.setAttribute("twitter.requestToken",
					requestToken.getTokenId());
			session.setAttribute("twitter.requestToken.secret",
					requestToken.getTokenSecret());

			response.sendRedirect(authenticationService
					.getAuthorisationUrl(requestToken));
			String preAuthTarget = target;
			String queryString = request.getQueryString();
			if (queryString != null && queryString.trim().length() > 0) {
				preAuthTarget = preAuthTarget.concat("?").concat(queryString);
			}
			session.setAttribute("preauth.target", preAuthTarget);
			return;
		}

		filterChain.doFilter(req, res);
	}
}
