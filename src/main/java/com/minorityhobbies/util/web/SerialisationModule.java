package com.minorityhobbies.util.web;

public class SerialisationModule {
	public JSONSerialisationLayer getJSONSerialisationLayer() {
		return new JSONSerialisationLayerJacksonImpl();
	}
}
