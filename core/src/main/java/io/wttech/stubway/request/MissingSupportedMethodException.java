package io.wttech.stubway.request;

import io.wttech.stubway.StubConstants;

public class MissingSupportedMethodException extends Exception {

	MissingSupportedMethodException() {
		super(String.format("Missing '%s' property. Check properties of your stub", StubConstants.METHOD));
	}

	MissingSupportedMethodException(String method, Throwable err) {
		super(String.format("Method %s is not supported", method), err);
	}

}
