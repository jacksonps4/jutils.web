package com.minorityhobbies.util.web.auth;

import java.util.Properties;

public class AuthenticatedUserPropertiesFileRepository implements
		AuthenticatedUserRepository {
	private final Properties userRepository;
	
	public AuthenticatedUserPropertiesFileRepository(Properties userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public UserToken getUserToken(String tokenKey) {
		String tokenId = userRepository.getProperty(String.format("%s.twitter.accessToken", tokenKey));
		String tokenSecret = userRepository.getProperty(String.format("%s.twitter.secret", tokenKey));
		return new SimpleUserToken(tokenId, tokenSecret);
	}
}
