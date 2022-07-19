package io.wttech.stubway.stub;

import io.wttech.stubway.request.HttpMethod;
import io.wttech.stubway.request.MissingSupportedMethodException;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import io.wttech.stubway.StubConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import static org.apache.http.HttpStatus.SC_OK;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Stub {

	@Self
	private Resource resource;

	@Inject
	@Named(StubConstants.METHOD)
	private String method;

	@Inject
	@Named(StubConstants.STATUS_CODE)
	@Default(intValues = SC_OK)
	private int statusCode;

	private Set<StubProperty> stubProperties;

	private Map<String, String> responseHeaders;

	@PostConstruct
	private void afterCreated() {
		ValueMap valueMap = resource.getValueMap();
		this.stubProperties = valueMap.keySet().stream()
				.filter(key -> !key.startsWith(StubConstants.JCR_NAMESPACE))
				.filter(key -> !key.startsWith(StubConstants.NAMESPACE))
				.filter(key -> !key.startsWith(StubConstants.RESPONSE_PREFIX))
				.map(key -> StubProperty.create(key, valueMap.get(key, String[].class))).flatMap(Collection::stream)
				.collect(Collectors.toSet());

		this.responseHeaders = valueMap.keySet().stream()
				.filter(key -> key.startsWith(StubConstants.RESPONSE_PREFIX))
				.collect(Collectors.toMap(key -> StringUtils.removeStart(key, StubConstants.RESPONSE_PREFIX), key -> valueMap.get(key, String.class)));
	}

	public InputStream getInputStream() {
		return resource.adaptTo(InputStream.class);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public Set<StubProperty> getProperties() {
		return stubProperties;
	}

	public HttpMethod getMethod() throws MissingSupportedMethodException {
		return HttpMethod.getMethod(method);
	}

	public Map<String, String> getResponseHeaders() {
		return this.responseHeaders;
	}
}
