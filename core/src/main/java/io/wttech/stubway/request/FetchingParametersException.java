package io.wttech.stubway.request;

public class FetchingParametersException extends Exception {

	FetchingParametersException(Throwable err) {
		super("Fetching request parameters failed", err);
	}
}
