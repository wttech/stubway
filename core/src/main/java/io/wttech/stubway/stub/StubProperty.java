package io.wttech.stubway.stub;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class StubProperty {

	private String name;

	private String value;

	public static StubProperty create(String name, String value) {
		return new StubProperty(name, value);
	}

	public static Set<StubProperty> create(String name, String[] value) {
		return Stream.of(value).map(v -> create(name, v)).collect(Collectors.toSet());
	}

	private StubProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		StubProperty that = (StubProperty) o;

		return new EqualsBuilder().append(name, that.name).append(value, that.value).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(name).append(value).toHashCode();
	}
}
