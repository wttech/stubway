package io.wttech.stubway;

import io.wttech.stubway.matcher.Matcher;
import io.wttech.stubway.matcher.MatchersRegistry;
import io.wttech.stubway.request.FetchingParametersException;
import io.wttech.stubway.request.HttpMethod;
import io.wttech.stubway.request.MissingSupportedMethodException;
import io.wttech.stubway.request.RequestParameters;
import io.wttech.stubway.response.StubResponse;
import io.wttech.stubway.stub.Stub;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class StubFinderServiceTest {

    private static final String expectedMessageForNotFoundStub = "{\n"
            + "  \"message\": \"Stub not found\",\n"
            + "  \"statusCode\": 404\n"
            + "}";

    @InjectMocks
    private StubFinderService stubFinderService;

    @Mock
    private MatchersRegistry matchersRegistry;

    @Mock
    private Matcher matcher;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private RequestPathInfo requestPathInfo;

    @Mock
    private ResourceResolver resourceResolver;

    @Mock
    private Resource stubsResource;

    @Mock
    private Stub stub;

    @BeforeEach
    void setUp() {
        String resourcePath = "/content/stubway/stubs/library/books";
        Mockito.when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
        Mockito.when(requestPathInfo.getResourcePath()).thenReturn(resourcePath);
        Mockito.when(request.getResourceResolver()).thenReturn(resourceResolver);
        Mockito.when(resourceResolver.getResource(resourcePath)).thenReturn(stubsResource);
    }

    @Test
    void getStubResponseShouldPassWhenStubNotFound() throws FetchingParametersException, IOException {
        try (MockedConstruction<RequestParameters> mocked = Mockito.mockConstruction(RequestParameters.class)) {
            RequestParameters requestParameters = new RequestParameters(request);
            StubResponse stubResponse = stubFinderService.getStubResponse(request);
            Assertions.assertEquals(404, stubResponse.getStatusCode());
            Assertions.assertEquals(expectedMessageForNotFoundStub,
                    IOUtils.toString(stubResponse.getInputStream(), Charset.defaultCharset()));
        }
    }

    @Test
    void getStubResponseShouldPassWhenRequestParamsMatch()
            throws IOException, MissingSupportedMethodException {

        prepareMocks();
        Mockito.when(stub.getStatusCode()).thenReturn(200);
        Mockito.when(stub.getInputStream()).thenReturn(IOUtils.toInputStream("Response message", Charset.defaultCharset()));
        Mockito.when(stub.getMethod()).thenReturn(HttpMethod.GET);
        Mockito.when(matchersRegistry.getMatcher(HttpMethod.GET)).thenReturn(matcher);

        try (MockedConstruction<RequestParameters> mocked = Mockito.mockConstruction(RequestParameters.class,
                (mock, context) -> {
                    Mockito.when(mock.getMethod()).thenReturn(HttpMethod.GET);
                    Mockito.when(matcher.matches(stub, mock)).thenReturn(true);
                })) {

            StubResponse stubResponse = stubFinderService.getStubResponse(request);
            Assertions.assertEquals(200, stubResponse.getStatusCode());
            Assertions.assertEquals("Response message", IOUtils.toString(stubResponse.getInputStream(), Charset.defaultCharset()));
        }
    }

    @Test
    void getStubResponseShouldPassWhenMethodMatchButParamsDontMatch() throws IOException, MissingSupportedMethodException {

        prepareMocks();
        Mockito.when(stub.getMethod()).thenReturn(HttpMethod.GET);
        Mockito.when(matchersRegistry.getMatcher(HttpMethod.GET)).thenReturn(matcher);

        try (MockedConstruction<RequestParameters> mocked = Mockito.mockConstruction(RequestParameters.class,
                (mock, context) -> {
                    Mockito.when(mock.getMethod()).thenReturn(HttpMethod.GET);
                    Mockito.when(matcher.matches(stub, mock)).thenReturn(false);
                })) {

            StubResponse stubResponse = stubFinderService.getStubResponse(request);
            Assertions.assertEquals(404, stubResponse.getStatusCode());
            Assertions.assertEquals(expectedMessageForNotFoundStub,
                    IOUtils.toString(stubResponse.getInputStream(), Charset.defaultCharset()));
        }
    }

    @Test
    void getStubResponseShouldPassWhenMethodDoesntMatch() throws IOException, MissingSupportedMethodException {
        prepareMocks();
        Mockito.when(stub.getMethod()).thenReturn(HttpMethod.PUT);

        try (MockedConstruction<RequestParameters> mocked = Mockito.mockConstruction(RequestParameters.class,
                (mock, context) -> {
                    Mockito.when(mock.getMethod()).thenReturn(HttpMethod.GET);
                })) {
            StubResponse stubResponse = stubFinderService.getStubResponse(request);
            Assertions.assertEquals(404, stubResponse.getStatusCode());
            Assertions.assertEquals(expectedMessageForNotFoundStub,
                    IOUtils.toString(stubResponse.getInputStream(), Charset.defaultCharset()));
        }
    }

    @Test
    void getStubResponseShouldPassWhenMethodIsMissingInStub() throws IOException, MissingSupportedMethodException {
        prepareMocks();
        Mockito.when(stub.getMethod()).thenThrow(MissingSupportedMethodException.class);

        try (MockedConstruction<RequestParameters> mocked = Mockito.mockConstruction(RequestParameters.class,
                (mock, context) -> {
                    Mockito.when(mock.getMethod()).thenReturn(HttpMethod.GET);
                })) {

            StubResponse stubResponse = stubFinderService.getStubResponse(request);
            Assertions.assertEquals(404, stubResponse.getStatusCode());
            Assertions.assertEquals(expectedMessageForNotFoundStub,
                    IOUtils.toString(stubResponse.getInputStream(), Charset.defaultCharset()));
        }

    }

    @Test
    void getStubResponseShouldPassWhenCannotGetParams() throws IOException, MissingSupportedMethodException {
        try (MockedConstruction<RequestParameters> mocked = Mockito.mockConstruction(RequestParameters.class,
                (mock, context) -> {
                    Mockito.when(mock.getMethod()).thenReturn(HttpMethod.GET);
                })) {
            // TODO: it should test the scenario when constructor for RequestParameters throws FetchingParametersException
            StubResponse stubResponse = stubFinderService.getStubResponse(request);
            // TODO: add assertions
        }
    }

    private void prepareMocks() throws MissingSupportedMethodException {
        List<Resource> resourceList = new ArrayList<>();
        Resource mockedResource = Mockito.mock(Resource.class);
        Mockito.when(mockedResource.adaptTo(Stub.class)).thenReturn(stub);
        resourceList.add(mockedResource);
        Mockito.when(stubsResource.getChildren()).thenReturn(resourceList);
    }
}