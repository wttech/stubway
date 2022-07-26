package io.wttech.stubway.collector;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.wttech.stubway.StubConstants;
import io.wttech.stubway.stub.StubProperty;
import io.wttech.stubway.request.RequestParameters;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class RequestBodyCollector implements PropertiesCollector {

	private static RequestBodyCollector instance;

	private RequestBodyCollector() {}

	public static PropertiesCollector createCollector() {
		if (instance == null) {
			instance = new RequestBodyCollector();
		}

		return instance;
	}

	@Override
	public Set<StubProperty> collectProperties(RequestParameters request) {
		JsonObject json = request.getRequestBody();
		return Optional.ofNullable(json)
				.orElse(new JsonObject()).entrySet().stream().map(this::toStubProperty)
				.collect(Collectors.toSet());

	}

	private StubProperty toStubProperty(Entry<String, JsonElement> entry) {
		String value = Optional.of(entry.getValue())
				.filter(JsonElement::isJsonPrimitive)
				.map(JsonElement::getAsString)
				.orElse(StringUtils.EMPTY);

		return StubProperty.create(String.format(StubConstants.BODY_PREFIX, entry.getKey()), value, false);
	}

}
