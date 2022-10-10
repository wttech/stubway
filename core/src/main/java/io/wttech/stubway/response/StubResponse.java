package io.wttech.stubway.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.wttech.stubway.stub.Stub;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.httpclient.HttpStatus.*;

public class StubResponse {

	private InputStream body;
	private int statusCode;
	private Map<String, String> headers;

	private static Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.create();

	public static StubResponse foundStub(Stub foundStub) {
		return new StubResponse(foundStub);
	}

	public static StubResponse empty() {
		return new StubResponse(SC_NOT_FOUND, "Stub not found");
	}

	public static StubResponse error(Exception e) {
		return new StubResponse(SC_BAD_REQUEST, e.getMessage());
	}

	public static StubResponse internalError(Exception e) {
		return new StubResponse(SC_INTERNAL_SERVER_ERROR, e.getMessage());
	}

	private StubResponse(Stub foundStub) {
		this.statusCode = foundStub.getStatusCode();
		this.headers = foundStub.getResponseHeaders();
		this.body = Optional.ofNullable(foundStub.getInputStream())
				.orElse(IOUtils.toInputStream("", Charset.defaultCharset()));
	}

	private StubResponse(int status, String errorMessage) {
		this.statusCode = status;
		ErrorResponseBody errorBody = new ErrorResponseBody(status, errorMessage);
		this.body = IOUtils.toInputStream(gson.toJson(errorBody), Charset.defaultCharset());
	}

	public InputStream getBody() {
		return body;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public Optional<Map<String, String>> getHeaders() {
		return Optional.ofNullable(headers);
	}

}
