package io.wttech.stubway.request;

public class QueryParameter {

	private String name;
	private String value;

	private QueryParameter(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public static QueryParameter create(String name, String value) {
		return new QueryParameter(name, value);
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
