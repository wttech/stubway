package io.wttech.stubway.response;

class ErrorResponseBody {

	private String message;
	private int statusCode;

	public ErrorResponseBody(int statusCode, String message) {
		this.message = message;
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}

}
