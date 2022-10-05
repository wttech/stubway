package io.wttech.stubway.response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.wttech.stubway.stub.Stub;

class StubResponseTest {

    @Test
    void emptyShouldReturnStubResponseWithEmptyParams() throws IOException {
        StubResponse empty = StubResponse.empty();
        Assertions.assertEquals(404, empty.getStatusCode());
        String expected = "{\n"
                + "  \"message\": \"Stub not found\",\n"
                + "  \"statusCode\": 404\n"
                + "}";
        Assertions.assertEquals(expected, IOUtils.toString(empty.getInputStream(), Charset.defaultCharset()));
    }

    @Test
    void internalErrorShouldReturnStubResponseWithStatusCode500() {
        StubResponse internalError = StubResponse.internalError(new Exception());
        Assertions.assertEquals(500, internalError.getStatusCode());
    }

    @Test
    void errorShouldReturnStubResponseWithStatusCode400() {
        StubResponse error = StubResponse.error(new Exception());
        Assertions.assertEquals(400, error.getStatusCode());
    }

    @Test
    void foundStubShouldReturnStubResponseWithSetValues() throws IOException {
        Stub stub = mock(Stub.class);
        when(stub.getStatusCode()).thenReturn(200);
        when(stub.getInputStream()).thenReturn(IOUtils.toInputStream("fake input stream message", Charset.defaultCharset()));
        Map<String, String> mockedHeadersMap = new HashMap<>();
        mockedHeadersMap.put("Server", "Stubway/1.0.0");
        mockedHeadersMap.put("Date", "Sat, 18 May 2008 09:20:00 GMT");
        when(stub.getResponseHeaders()).thenReturn(mockedHeadersMap);

        StubResponse foundStub = StubResponse.foundStub(stub);
        Assertions.assertEquals(200, foundStub.getStatusCode());
        Assertions.assertEquals("fake input stream message",
                IOUtils.toString(foundStub.getInputStream(), Charset.defaultCharset()));
        Assertions.assertEquals(foundStub.getResponseHeaders(), mockedHeadersMap);
    }
}