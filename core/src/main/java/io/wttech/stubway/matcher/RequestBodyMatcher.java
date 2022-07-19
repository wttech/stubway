package io.wttech.stubway.matcher;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.wttech.stubway.stub.StubProperty;
import io.wttech.stubway.request.RequestParameters;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

class RequestBodyMatcher extends AbstractMatcher {

	@Override
	protected Set<StubProperty> collectProperties(RequestParameters request) {
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

		return StubProperty.create(entry.getKey(), value, false);
	}

}
