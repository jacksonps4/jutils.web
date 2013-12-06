package com.minorityhobbies.util.web.auth;

public interface AuthenticatedUserRepository {
	UserToken getUserToken(String tokenKey);
}
