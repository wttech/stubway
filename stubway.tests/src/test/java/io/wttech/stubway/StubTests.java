package io.wttech.stubway;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class StubTests {

	private static String URL;

	private static String STUB_USER;

	private static String STUB_PASSWORD;

	private static final String STUB_DEFAULT_URL = "http://localhost:4503";

	private static final String STUB_DEFAULT_URSER = "admin";

	private static final String STUB_DEFAULT_PASSWORD = "admin";

	private Response sendGetRequest(String path) {
		Response response = given().auth().basic(STUB_USER, STUB_PASSWORD).when().get(URL + path);
		return response;
	}

	private Response sendPostRequest(String path, String body) {
		Response response = given().auth().basic(STUB_USER, STUB_PASSWORD).contentType(ContentType.JSON).body(body)
				.post(URL + path);
		return response;
	}

	private void compareJsonResponse(String fileName, Response response) throws IOException {
		JsonParser parser = new JsonParser();
		JsonElement actual = parser.parse(response.body().asPrettyString());
		JsonElement expected = parser.parse(getJsonFile(fileName));
		Assert.assertTrue(actual.isJsonObject());
		Assert.assertEquals(expected, actual);
	}

	private String getJsonFile(String fileName) throws IOException {
		String json = IOUtils.toString(this.getClass().getResourceAsStream(fileName), "UTF-8");
		return json;
	}

	@BeforeClass
	public static void init() {
		String stubUrl = System.getProperty("stubUrl");
		String stubUser = System.getProperty("stubUser");
		String stubPassword = System.getProperty("stubPassword");
		URL = StringUtils.isNotEmpty(stubUrl) ? stubUrl : STUB_DEFAULT_URL;
		STUB_USER = StringUtils.isNotEmpty(stubUser) ? stubUser : STUB_DEFAULT_URSER;
		STUB_PASSWORD = StringUtils.isNotEmpty(stubPassword) ? stubPassword : STUB_DEFAULT_PASSWORD;
		System.out.println("***** Stub tests will be run on instance: " + URL);
	}

	@Test
	public void getFantasyBooksTest() throws IOException {
		Response response = sendGetRequest("/content/stubway/stubs/library/books?type=fantasy");
		response.then().statusCode(200);
		compareJsonResponse("fantasy_get.json", response);
	}

	@Test
	public void getPoetryBooksTest() throws IOException {
		Response response = sendGetRequest("/content/stubway/stubs/library/books?type=poetry");
		response.then().statusCode(200);
		compareJsonResponse("poetry_get.json", response);
	}

	@Test
	public void getPoetryEbooksTest() throws IOException {
		Response response = sendGetRequest("/content/stubway/stubs/library/books?type=poetry&ebook=true");
		response.then().statusCode(200);
		compareJsonResponse("poetry_ebook_get.json", response);
	}

	@Test
	public void getPoetryAndFantasyTestOneQueryParameterTest() throws IOException {
		Response response = sendGetRequest("/content/stubway/stubs/library/books?type=poetry,fantasy");
		response.then().statusCode(200);
		compareJsonResponse("poetry_fantasy_get.json", response);
	}

	@Test
	public void getPoetryAndFantasyMultipleParametersTest() throws IOException {
		Response response = sendGetRequest("/content/stubway/stubs/library/books?type=poetry&type=fantasy");
		response.then().statusCode(200);
		compareJsonResponse("poetry_fantasy_get.json", response);
	}

	@Test
	public void getHistorialDefinedByRegexTest() throws IOException {
		Response response = sendGetRequest("/content/stubway/stubs/library/books?type=historical");
		response.then().statusCode(200);
		compareJsonResponse("historical_get.json", response);
	}

	@Test
	public void getSecretBooksTest() throws IOException {
		Response response = sendGetRequest("/content/stubway/stubs/library/books?type=secret");
		response.then().statusCode(401);
		compareJsonResponse("secret_get.json", response);
	}

	@Test
	public void getAllBooksTest() throws IOException {
		Response response = this.sendGetRequest("/content/stubway/stubs/library/books?type=.*");
		response.then().statusCode(200);
		compareJsonResponse("all_get.json", response);
	}

	@Test
	public void postFantasyBookTest() throws IOException {
		Response response = sendPostRequest("/content/stubway/stubs/library/books?type=fantasy", "");
		response.then().statusCode(200);
		compareJsonResponse("fantasy_post.json", response);
	}

	@Test
	public void postPoetryBookTest() throws IOException {
		String body = "{" + "type: poetry" + "}";
		Response response = sendPostRequest("/content/stubway/stubs/library/books", body);
		response.then().statusCode(200);
		compareJsonResponse("poetry_post.json", response);
	}

	@Test
	public void postFantasyPoetryBookTest() throws IOException {
		String body = "{" + "type: poetry" + "}";
		Response response = sendPostRequest("/content/stubway/stubs/library/books?type=fantasy", body);
		response.then().statusCode(200);
		compareJsonResponse("poetry_fantasy_get.json", response);
	}

	@Test
	public void postSecretBookTest() throws IOException {
		Response response = sendPostRequest("/content/stubway/stubs/library/books?type=secret", "");
		response.then().statusCode(401);
		compareJsonResponse("secret_post.json", response);
	}

	@Test
	public void postAllBookTest() throws IOException {
		String body = "{" + "type: .*" + "}";
		Response response = sendPostRequest("/content/stubway/stubs/library/books", body);
		response.then().statusCode(200);
		compareJsonResponse("all_post.json", response);
	}

	@Test
	public void shouldReturn404ForNotFoundStub() throws IOException {
		Response response = sendGetRequest("/content/stubway/stubs/library/books?type=notexisting");
		response.then().statusCode(404);
	}

	@Test
	public void shouldReturn404ForExistingQueryParameterNotCaseSensitive() throws IOException {
		Response response = sendGetRequest("/content/stubway/stubs/library/books?Type=fantasy");
		response.then().statusCode(404);
	}

	@Test
	public void shouldReturn404ForExistingQueryParameterValueNotCaseSensitive() throws IOException {
		Response response = sendGetRequest("/content/stubway/stubs/library/books?type=Fantasy");
		response.then().statusCode(404);
	}
}
