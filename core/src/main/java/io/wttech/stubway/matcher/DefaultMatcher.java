package io.wttech.stubway.matcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.wttech.stubway.collector.PropertyCollector;
import io.wttech.stubway.request.RequestParameters;
import io.wttech.stubway.stub.Stub;
import io.wttech.stubway.stub.StubProperty;

public class DefaultMatcher implements Matcher {

	List<PropertyCollector> collectors;

	public DefaultMatcher(PropertyCollector... collectors) {
		this.collectors = Stream.of(collectors).collect(Collectors.toList());
	}

	@Override
	public boolean matches(Stub stub, RequestParameters requestParameters) {
		Set<StubProperty> requestProperties = new HashSet<>();
		collectors.forEach(collector -> requestProperties.addAll(collector.collectProperties(requestParameters)));

		if (stub.getProperties().size() != requestProperties.size()) {
			return false;
		}

		return stub.getProperties().stream()
				.map(p -> PropertyPattern.create(p.getName(), p.getValue()))
				.allMatch(propertyPattern -> anyPropMatches(propertyPattern, requestProperties));
	}

	private boolean anyPropMatches(PropertyPattern propertyPattern, Set<StubProperty> requestProperties) {
		return requestProperties.stream()
				.anyMatch(propertyPattern::matches);
	}

}
