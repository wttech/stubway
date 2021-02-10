package io.wttech.stubway.response;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Optional;

import io.wttech.stubway.stub.Stub;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.apache.commons.httpclient.HttpStatus.*;

public class StubResponse {

	private InputStream inputStream;
	private int statusCode;

	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static StubResponse foundStub(Stub foundStub) {
		return new StubResponse(foundStub);
	}

	public static StubResponse empty() {
		return new StubResponse();
	}

	public static StubResponse error(Exception e) {
		return new StubResponse(e, SC_BAD_REQUEST);
	}

	public static StubResponse internalError(Exception e) {
		return new StubResponse(e, SC_INTERNAL_SERVER_ERROR);
	}

	private StubResponse(Stub foundStub) {
		this.inputStream = Optional.ofNullable(foundStub.getInputStream())
				.orElse(IOUtils.toInputStream("", Charset.defaultCharset()));
		this.statusCode = foundStub.getStatusCode();
	}

	private StubResponse() {
		ResponseBody responseBody = new ResponseBody("Stub not found", SC_NOT_FOUND);
		String message = gson.toJson(responseBody);
		this.inputStream = IOUtils.toInputStream(message, Charset.defaultCharset());
		this.statusCode = SC_NOT_FOUND;
	}

	private StubResponse(Exception exception, int status) {
		ResponseBody responseBody = new ResponseBody(exception.getMessage(), SC_BAD_REQUEST);
		String message = gson.toJson(responseBody);
		this.inputStream = IOUtils.toInputStream(message, Charset.defaultCharset());
		this.statusCode = status;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
