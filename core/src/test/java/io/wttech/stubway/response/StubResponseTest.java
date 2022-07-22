package io.wttech.stubway.response;

import io.wttech.stubway.stub.Stub;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class StubResponseTest {

    @Test
    public void emptyShouldReturnStubResponseWithEmptyParams() {
        StubResponse empty = StubResponse.empty();
        Assertions.assertEquals((Integer) null, empty.getStatusCode());
        Assertions.assertNull(empty.getInputStream());
    }

    @Test
    public void internalErrorShouldReturnStubResponseWithStatusCode500() {
        StubResponse internalError = StubResponse.internalError(new Exception());
        Assertions.assertEquals(500, internalError.getStatusCode());
    }

    @Test
    public void errorShouldReturnStubResponseWithStatusCode400() {
        StubResponse error = StubResponse.error(new Exception());
        Assertions.assertEquals(400, error.getStatusCode());
    }

    @Test
    public void foundStubShouldReturnStubResponseWithSetValues() {
        Stub stub = new Stub();
        StubResponse foundStub = StubResponse.foundStub(stub);
        Assertions.assertEquals(200, foundStub.getStatusCode());
        Assertions.assertNull(foundStub.getInputStream());
    }
}