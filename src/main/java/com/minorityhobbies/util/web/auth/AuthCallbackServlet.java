package me.jacksonps4.web.auth;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class AuthCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = -5285388575134136332L;

	private final Logger logger = Logger.getLogger(getClass().getName());

	private final AuthenticationService authenticationService;
	private final String userPropertiesFile;
	private final Properties userAuth;
	private final ObjectMapper mapper = new ObjectMapper();
	
	public AuthCallbackServlet(AuthenticationService authenticationService,
			String userPropertiesFile) throws IOException {
		super();
		this.authenticationService = authenticationService;
		this.userPropertiesFile = userPropertiesFile;
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
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		String oauthVerifier = request.getParameter("oauth_verifier");

		String token = (String) session
				.getAttribute("twitter.requestToken");
		String secret = (String) session
				.getAttribute("twitter.requestToken.secret");

		UserToken requestToken = new SimpleUserToken(token, secret);
		UserToken accessToken = authenticationService.getAccessToken(
				requestToken, oauthVerifier);

		session.setAttribute("twitter.accessToken", accessToken.getTokenId());
		session.setAttribute("twitter.accessToken.secret",
				accessToken.getTokenSecret());

		String jkpuid = UUID.randomUUID().toString();
		Cookie cookie = new Cookie("jkpuid", jkpuid);
		cookie.setMaxAge(60 * 60 * 24 * 365);
		response.addCookie(cookie);
		userAuth.put(String.format("%s.twitter.accessToken", jkpuid),
				accessToken.getTokenId());
		userAuth.put(String.format("%s.twitter.secret", jkpuid),
				accessToken.getTokenSecret());

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(userPropertiesFile);
			userAuth.store(fos, "");
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		HttpResponse result = authenticationService.sendSignedRequest("GET",
				"https://api.twitter.com/1.1/account/verify_credentials.json",
				accessToken);
		JsonNode node = mapper.readTree(result.getBody());
		String screenName = node.get("screen_name").asText();
		session.setAttribute("twitter.user", screenName);
		session.setAttribute("twitter.auth", node.toString());

		logger.info(String.format("Authenticated user details: %s",
				result.getBody()));
		String redirect = (String) session.getAttribute("preauth.target");
		if (redirect != null) {
			session.removeAttribute("preauth.target");
			response.sendRedirect(redirect);
			return;
		}
	}
}
