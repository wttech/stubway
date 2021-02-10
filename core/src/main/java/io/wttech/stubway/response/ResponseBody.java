package io.wttech.stubway.response;

class ResponseBody {

	private String message;
	private int statusCode;

	public ResponseBody() {
	}

	public ResponseBody(String message, int statusCode) {
		this.message = message;
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
