package me.jacksonps4.web.auth;

public interface AuthenticatedUserRepository {
	UserToken getUserToken(String tokenKey);
}
