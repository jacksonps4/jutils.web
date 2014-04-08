package com.minorityhobbies.util.web;

/**
 * Thrown if an error occurred during serialisation or deserialisation
 */
public class JSONSerialisationException extends Exception {
	private static final long serialVersionUID = -3288907987230033746L;

	public JSONSerialisationException() {
		super();
	}

	public JSONSerialisationException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public JSONSerialisationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public JSONSerialisationException(String arg0) {
		super(arg0);
	}

	public JSONSerialisationException(Throwable arg0) {
		super(arg0);
	}
}
