package io.wttech.stubway.matcher;

import io.wttech.stubway.collector.QueryParametersCollector;
import io.wttech.stubway.collector.RequestBodyCollector;
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
		matchers.put(HttpMethod.GET, new DefaultMatcher(QueryParametersCollector.create()));
		matchers.put(HttpMethod.POST, new DefaultMatcher(RequestBodyCollector.create(),
				QueryParametersCollector.create()));
		matchers.put(HttpMethod.PUT, new DefaultMatcher(RequestBodyCollector.create(),
				QueryParametersCollector.create()));
		matchers.put(HttpMethod.DELETE, new DefaultMatcher(RequestBodyCollector.create(),
				QueryParametersCollector.create()));
	}

}
