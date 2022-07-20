package io.wttech.stubway;

import java.util.regex.Pattern;

public class StubConstants {

	public static final String STUB_RESOURCE_TYPE = "stubway/stub";
	public static final String JCR_NAMESPACE = "jcr:";
	public static final String NAMESPACE = "stub.";
	public static final String STATUS_CODE = NAMESPACE + "statusCode";
	public static final String METHOD = NAMESPACE + "method";
	public static final String REGEX_SUFFIX = ".regex";
	public static final String BODY_PREFIX = "body.%s";

	private StubConstants() {
		throw new IllegalStateException("Utility class");
	}

}
