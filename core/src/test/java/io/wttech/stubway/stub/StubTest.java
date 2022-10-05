package io.wttech.stubway.stub;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import io.wttech.stubway.request.HttpMethod;
import io.wttech.stubway.request.MissingSupportedMethodException;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

@ExtendWith({ AemContextExtension.class })
class StubTest {
    private final String destPath = "/content/stubway/stubs";
    private final AemContext ctx = new AemContext(ResourceResolverType.JCR_OAK);

    @BeforeEach
    public void setUp() {
        ctx.addModelsForClasses(Stub.class);
        ctx.load().json("/io/wttech/stubway/stub/Stub.json", destPath);
    }

    @Test
    void testModelFieldsWhenStubIsCorrect() throws IOException, MissingSupportedMethodException {
        Resource resource = ctx.resourceResolver().getResource(destPath + "/stub_correct");
        Stub stub = resource.adaptTo(Stub.class);
        Assertions.assertEquals(HttpMethod.GET, stub.getMethod());
        Assertions.assertEquals(200, stub.getStatusCode());

        Set<StubProperty> properties = stub.getProperties();
        Assertions.assertEquals(2, properties.size());
        List<StubProperty> listOfProperties = new ArrayList<StubProperty>(properties);
        StubProperty firstProperty = listOfProperties.get(0);
        Assertions.assertEquals("type", firstProperty.getName());
        Assertions.assertEquals("\\Qpoetry\\E", firstProperty.getValue());
        StubProperty secondProperty = listOfProperties.get(1);
        Assertions.assertEquals("ebok", secondProperty.getName());
        Assertions.assertEquals("\\Qtrue\\E", secondProperty.getValue());

        Map<String, String> responseHeaders = stub.getResponseHeaders();
        Assertions.assertEquals(2, responseHeaders.size());
        Map<String, String> mockedHeadersMap = new HashMap<>();
        mockedHeadersMap.put("Server", "Stubway/1.0.0");
        mockedHeadersMap.put("Date", "Sat, 18 May 2008 09:20:00 GMT");
        Assertions.assertEquals(mockedHeadersMap, responseHeaders);

        Assertions.assertEquals("{\"nbrOfBooks\" : \"1\", \"author\" : \"Author 1\"}",
                IOUtils.toString(stub.getInputStream(), Charset.defaultCharset()));

    }

    @Test
    void testModelFieldsWhenMethodIsMissing() {
        Resource resource = ctx.resourceResolver().getResource(destPath + "/stub_missing_method");
        Stub stub = resource.adaptTo(Stub.class);

        MissingSupportedMethodException thrown = Assertions.assertThrows(MissingSupportedMethodException.class,
                () -> stub.getMethod(),
                "Expected getMethod() to throw MissingSupportedMethodException");

        Assertions.assertEquals("Missing 'stub.method' property. Check properties of your stub", thrown.getMessage());
    }

    @Test
    void testModelFieldsWhenMethodIsNotSupported() {
        Resource resource = ctx.resourceResolver().getResource(destPath + "/stub_not_supported_method");
        Stub stub = resource.adaptTo(Stub.class);

        MissingSupportedMethodException thrown = Assertions.assertThrows(MissingSupportedMethodException.class,
                () -> stub.getMethod(),
                "Expected getMethod() to throw MissingSupportedMethodException");

        Assertions.assertEquals("Method GET1 is not supported", thrown.getMessage());
    }
}