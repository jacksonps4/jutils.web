package com.minorityhobbies.util.web.auth;

final class SimpleUserToken implements
		UserToken {
	private final String tokenId;
	private final String tokenSecret;

	public SimpleUserToken(String tokenId, String tokenSecret) {
		super();
		this.tokenId = tokenId;
		this.tokenSecret = tokenSecret;
	}

	public String getTokenId() {
		return tokenId;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokenId == null) ? 0 : tokenId.hashCode());
		result = prime * result
				+ ((tokenSecret == null) ? 0 : tokenSecret.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleUserToken other = (SimpleUserToken) obj;
		if (tokenId == null) {
			if (other.tokenId != null)
				return false;
		} else if (!tokenId.equals(other.tokenId))
			return false;
		if (tokenSecret == null) {
			if (other.tokenSecret != null)
				return false;
		} else if (!tokenSecret.equals(other.tokenSecret))
			return false;
		return true;
	}
}