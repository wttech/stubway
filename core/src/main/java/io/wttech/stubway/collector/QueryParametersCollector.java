package io.wttech.stubway.collector;

import io.wttech.stubway.stub.StubProperty;
import io.wttech.stubway.request.RequestParameters;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class QueryParametersCollector implements PropertyCollector {

	@Override
	public Set<StubProperty> collectProperties(RequestParameters request) {
		return request.getQueryParameters().stream()
				.map(p -> StubProperty.create(p.getName(), p.getValue().split(","), false))
				.flatMap(Collection::stream).collect(Collectors.toSet());
	}

}
