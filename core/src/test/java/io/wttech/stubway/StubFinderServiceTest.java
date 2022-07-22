package io.wttech.stubway;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import io.wttech.stubway.response.StubResponse;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(AemContextExtension.class)
public class StubFinderServiceTest {
    public AemContext aemContext = new AemContext();
    public StubFinderService stubFinderService;
    public SlingHttpServletRequest request;
    public RequestPathInfo requestPathInfo;
    public ResourceResolver resourceResolver;

    @Before
    public void setUp() {
        stubFinderService = aemContext.registerService(new StubFinderService());
        request = mock(SlingHttpServletRequest.class);
        requestPathInfo = mock(RequestPathInfo.class);
        resourceResolver = mock(ResourceResolver.class);
    }
    @Test
    public void getStubResponseShouldPassWhenRequestParamsAreMatching() {
        //given
        //when
        when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        StubResponse result = stubFinderService.getStubResponse(request);
        //then
        System.out.println(result.getInputStream());
    }
    @Test
    public void getStubResponseWhenRequestParamsAreNotMatching() throws IOException {
        //given
        //when
        when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(request.getReader()).thenThrow(new IOException());
        StubResponse result = stubFinderService.getStubResponse(request);
        //then
        System.out.println(result.getStatusCode());
        System.out.println(result.getInputStream());
    }
}