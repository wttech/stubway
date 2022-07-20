package io.wttech.stubway.matcher;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.wttech.stubway.StubConstants;
import io.wttech.stubway.stub.StubProperty;
import io.wttech.stubway.request.RequestParameters;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

class RequestBodyMatcher extends AbstractMatcher {

	@Override
	protected Set<StubProperty> collectProperties(RequestParameters request) {

		Set<StubProperty> properties = request.getQueryParameters().stream()
				.map(p -> StubProperty.create(p.getName(), p.getValue().split(",")))
				.flatMap(Collection::stream).collect(Collectors.toSet());
		Optional.ofNullable(request.getRequestBody())
				.orElse(new JsonObject())
				.entrySet().stream().map(this::toStubProperty)
				.forEach(properties::add);
		return properties;
	}

	private StubProperty toStubProperty(Entry<String, JsonElement> entry) {
		String value = Optional.of(entry.getValue())
				.filter(JsonElement::isJsonPrimitive)
				.map(JsonElement::getAsString)
				.orElse(StringUtils.EMPTY);

		return StubProperty.create(String.format(StubConstants.BODY_PREFIX, entry.getKey()), value, false);
	}

}
