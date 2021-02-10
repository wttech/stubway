package io.wttech.stubway.matcher;

import io.wttech.stubway.request.HttpMethod;
import java.util.EnumMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(service = MatchersRegistry.class)
public class MatchersRegistry {

	private final Map<HttpMethod, Matcher> matchers = new EnumMap<>(HttpMethod.class);

	public Matcher getMatcher(HttpMethod method) {
		return matchers.get(method);
	}

	@Activate
	public void activate() {
		matchers.put(HttpMethod.GET, new QueryParametersMatcher());
		matchers.put(HttpMethod.POST, new RequestBodyMatcher());
	}

}
