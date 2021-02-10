package io.wttech.stubway.request;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RequestParameters {

	private Gson gson = new Gson();

	private List<QueryParameter> queryParameters;
	private JsonObject requestBody;
	private HttpMethod method;

	public RequestParameters(SlingHttpServletRequest request) throws FetchingParametersException {
		method = HttpMethod.valueOf(request.getMethod());
		queryParameters = request.getRequestParameterList().stream()
				.map(p -> QueryParameter.create(p.getName(), p.getString())).collect(Collectors.toList());
		requestBody = getRequestBody(request);
	}

	private JsonObject getRequestBody(SlingHttpServletRequest request) throws FetchingParametersException {
		try (BufferedReader reader = request.getReader()) {
			String body = reader.lines().collect(Collectors.joining());
			return gson.fromJson(body, JsonObject.class);
		} catch (IOException e) {
			throw new FetchingParametersException(e);
		}
	}

	public List<QueryParameter> getQueryParameters() {
		return queryParameters;
	}

	public JsonObject getRequestBody() {
		return requestBody;
	}

	public HttpMethod getMethod() {
		return method;
	}

}
