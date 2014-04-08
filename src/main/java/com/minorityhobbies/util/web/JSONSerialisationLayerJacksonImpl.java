package com.minorityhobbies.util.web;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class JSONSerialisationLayerJacksonImpl implements
		JSONSerialisationLayer {
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public <T> T fromJSON(String json, Class<T> type) throws JSONSerialisationException {
		try {
			return mapper.readValue(json, type);
		} catch (JsonParseException e) {
			throw new JSONSerialisationException(e);
		} catch (JsonMappingException e) {
			throw new JSONSerialisationException(e);
		} catch (IOException e) {
			throw new JSONSerialisationException(e);
		}
	}

	@Override
	public String toJSON(Object obj) throws JSONSerialisationException {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new JSONSerialisationException(e);
		}
	}
}
