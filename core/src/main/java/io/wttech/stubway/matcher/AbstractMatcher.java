package io.wttech.stubway.matcher;

import java.util.Set;

import io.wttech.stubway.request.RequestParameters;
import io.wttech.stubway.stub.Stub;
import io.wttech.stubway.stub.StubProperty;

public abstract class AbstractMatcher implements Matcher {

	protected abstract Set<StubProperty> collectProperties(RequestParameters request);

	@Override
	public boolean matches(Stub stub, RequestParameters requestParameters) {
		Set<StubProperty> requestProperties = collectProperties(requestParameters);

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
