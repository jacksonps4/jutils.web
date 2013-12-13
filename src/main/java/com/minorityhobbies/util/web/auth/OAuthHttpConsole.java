package com.minorityhobbies.util.web.auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class OAuthHttpConsole {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = null;
		Properties props = new Properties();
		FileInputStream fin = null;
		String authProperties = args[0];
		String tokenKey = args[1];
		try {
			fin = new FileInputStream(new File(authProperties));
			props.load(fin);

			ObjectMapper mapper = new ObjectMapper();

			OAuthService service = new OAuthService(
					new ScribeAuthenticationService(),
					new AuthenticatedUserPropertiesFileRepository(props));
			reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("> ");
			for (String line = null; (line = reader.readLine()) != null;) {
				line = line.trim();

				if ("exit".equals(line.toLowerCase())) {
					break;
				} else if (line.startsWith("user")) {
					StringTokenizer st = new StringTokenizer(line, " ,");
					if (st.hasMoreTokens()) {
						st.nextToken();

						StringBuilder usernames = new StringBuilder();
						while (st.hasMoreTokens()) {
							usernames.append(st.nextToken());
							if (st.hasMoreTokens()) {
								usernames.append(",");
							}
						}

						InputStream stream = service.sendRequest("GET",
								"https://api.twitter.com/1.1/users/lookup.json?screen_name="
										+ usernames.toString(), tokenKey, null);
						JsonNode node = mapper.readTree(stream);
						Iterator<JsonNode> elements = node.elements();
						while (elements.hasNext()) {
							JsonNode userNode = elements.next();
							String screenName = userNode.get("screen_name")
									.asText();
							System.out.print(screenName);
							if (elements.hasNext()) {
								System.out.print(",");
							}
						}
						System.out.println();
					}
					break;
				} else if ("home".equals(line)) {
					InputStream stream = null;
					try {
						stream = service.sendRequest("GET", "https://userstream.twitter.com/1.1/user.json", tokenKey, null);
						byte[] b = new byte[1024 * 64];
						for (int read = 0; (read = stream.read(b)) > -1;) {
							System.out.print(new String(b, 0, read));
						}
					} finally {
						if (stream != null) {
							try {
								stream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} else {
					StringTokenizer st = new StringTokenizer(line, " ");
					if (st.hasMoreTokens()) {
						String verb = st.nextToken();
						String url = st.nextToken();
						String data = null;

						if ("POST".equals(verb.toUpperCase())) {
							StringBuilder payload = new StringBuilder();
							for (String payloadLine = null; (payloadLine = reader
									.readLine()) != null;) {
								if (payloadLine.length() == 0) {
									break;
								}
								payload.append(String.format("%s%n",
										payloadLine));
							}
							data = payload.toString();
						}

						InputStream stream = service.sendRequest(verb, url,
								tokenKey, data);

						byte[] b = new byte[1024 * 64];
						StringBuilder response = new StringBuilder();
						for (int read = 0; (read = stream.read(b)) > -1;) {
							response.append(new String(b, 0, read));
						}

						System.out.println(response.toString());
					}
				}
				System.out.print("> ");
			}
		} finally {
			if (fin != null) {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
