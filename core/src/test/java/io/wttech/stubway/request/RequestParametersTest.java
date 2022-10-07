package io.wttech.stubway.request;

import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class RequestParametersTest {

    @Mock
    SlingHttpServletRequest request;

    @Mock
    BufferedReader bufferedReader;

    RequestParameters requestParameters;

    @BeforeEach
    void setUp() throws IOException {
        Mockito.when(request.getMethod()).thenReturn(String.valueOf(HttpMethod.POST));
    }

    @Test
    void testGetMethod() throws FetchingParametersException, IOException {
        Mockito.when(bufferedReader.lines()).thenReturn(Stream.empty());
        Mockito.when(request.getReader()).thenReturn(bufferedReader);

        requestParameters = new RequestParameters(request);

        Assertions.assertEquals(HttpMethod.POST, requestParameters.getMethod());
    }

    @Test
    void testGetQueryParameters() throws FetchingParametersException, IOException {
        Mockito.when(bufferedReader.lines()).thenReturn(Stream.empty());
        Mockito.when(request.getReader()).thenReturn(bufferedReader);

        List<RequestParameter> requestParameterList = new ArrayList<>();
        RequestParameter requestParameter1 = Mockito.mock(RequestParameter.class);
        Mockito.when(requestParameter1.getName()).thenReturn("type");
        Mockito.when(requestParameter1.getString()).thenReturn("poetry");
        requestParameterList.add(requestParameter1);
        RequestParameter requestParameter2 = Mockito.mock(RequestParameter.class);
        Mockito.when(requestParameter2.getName()).thenReturn("ebook");
        Mockito.when(requestParameter2.getString()).thenReturn("true");
        requestParameterList.add(requestParameter2);
        Mockito.when(request.getRequestParameterList()).thenReturn(requestParameterList);

        requestParameters = new RequestParameters(request);

        List<QueryParameter> queryParameters = requestParameters.getQueryParameters();
        Assertions.assertNotNull(queryParameters);
        Assertions.assertEquals(2, queryParameters.size());
        Assertions.assertEquals("type", queryParameters.get(0).getName());
        Assertions.assertEquals("poetry", queryParameters.get(0).getValue());
        Assertions.assertEquals("ebook", queryParameters.get(1).getName());
        Assertions.assertEquals("true", queryParameters.get(1).getValue());
    }

    @Test
    void testGetRequestBody() throws FetchingParametersException, IOException {
        Mockito.when(bufferedReader.lines()).thenReturn(Arrays.asList("{\"type\":\"poetry\"}").stream());
        Mockito.when(request.getReader()).thenReturn(bufferedReader);
        Mockito.when(request.getReader()).thenReturn(bufferedReader);

        requestParameters = new RequestParameters(request);

        JsonObject requestBody = requestParameters.getRequestBody();
        Assertions.assertNotNull(requestBody);
        Assertions.assertEquals("{\"type\":\"poetry\"}", requestBody.toString());
    }

    @Test
    void testConstructionThrowsFetchingParametersException() throws IOException {
        Mockito.when(request.getReader()).thenThrow(new IOException());

        FetchingParametersException thrown = Assertions.assertThrows(FetchingParametersException.class,
                () -> new RequestParameters(request),
                "Expected RequestParameters constructor to throw FetchingParametersException");

        Assertions.assertEquals("Fetching request parameters failed", thrown.getMessage());
    }
}
