package com.minorityhobbies.util.web;

import org.glassfish.jersey.server.ResourceConfig;

public class WebResourceConfig extends ResourceConfig {
	public WebResourceConfig(String... packages) {
		super();
		packages(packages);
	}

	public void addResource(Object resource) {
		register(resource);
	}
}
