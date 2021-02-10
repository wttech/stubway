package io.wttech.stubway.request;

import static org.apache.sling.api.servlets.HttpConstants.METHOD_GET;
import static org.apache.sling.api.servlets.HttpConstants.METHOD_POST;

public enum HttpMethod {

	GET(METHOD_GET), POST(METHOD_POST);

	private final String method;

	HttpMethod(String method) {
		this.method = method;
	}

	public static HttpMethod getMethod(String method) throws MissingSupportedMethodException {
		try {
			if (method == null) {
				throw new MissingSupportedMethodException();
			}
			return HttpMethod.valueOf(method);
		} catch (IllegalArgumentException e) {
			throw new MissingSupportedMethodException(method, e);
		}
	}

}
