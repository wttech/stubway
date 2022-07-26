package io.wttech.stubway.collector;

import io.wttech.stubway.stub.StubProperty;
import io.wttech.stubway.request.RequestParameters;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class QueryParametersCollector implements PropertiesCollector {

	private static QueryParametersCollector instance;

	private QueryParametersCollector() {}

	public static PropertiesCollector createCollector() {
		if (instance == null) {
			instance = new QueryParametersCollector();
		}

		return instance;
	}

	@Override
	public Set<StubProperty> collectProperties(RequestParameters request) {
		return request.getQueryParameters().stream()
				.map(p -> StubProperty.create(p.getName(), p.getValue().split(","), false))
				.flatMap(Collection::stream).collect(Collectors.toSet());
	}

}
