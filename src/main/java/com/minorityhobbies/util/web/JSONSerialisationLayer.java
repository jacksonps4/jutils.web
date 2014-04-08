package com.minorityhobbies.util.web;

public interface JSONSerialisationLayer {
	<T> T fromJSON(String json, Class<T> type) throws JSONSerialisationException;
	String toJSON(Object obj) throws JSONSerialisationException;
}
