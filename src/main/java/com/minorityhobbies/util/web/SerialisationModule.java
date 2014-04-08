package com.minorityhobbies.util.web;

import com.google.inject.AbstractModule;

public class SerialisationModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(JSONSerialisationLayer.class).to(JSONSerialisationLayerJacksonImpl.class);
	}
}
