package io.wttech.stubway.matcher;

import java.util.regex.Pattern;

import io.wttech.stubway.stub.StubProperty;

class PropertyPattern {

	private String propertyName;
	private Pattern pattern;

	private PropertyPattern(String propertyName, Pattern pattern) {
		this.propertyName = propertyName;
		this.pattern = pattern;
	}

	public static PropertyPattern create(String propertyName, String value) {
		return new PropertyPattern(propertyName, Pattern.compile(value));
	}

	public boolean matches(StubProperty stubProperty) {
		return this.propertyName.equals(stubProperty.getName()) && pattern.matcher(stubProperty.getValue()).matches();
	}

}
