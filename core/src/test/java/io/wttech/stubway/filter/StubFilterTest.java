package io.wttech.stubway.filter;

import io.wttech.stubway.StubServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class StubFilterTest {

    @InjectMocks
    private StubFilter stubFilter;

    @Mock
    private SlingHttpServletRequest request;

    @Mock
    private ServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private StubServlet stubServlet;

    @Mock Resource resource;

    @BeforeEach
    void setUp() {
        Mockito.when(request.getResource()).thenReturn(resource);;
    }

    @Test
    void testDoFilterIfResourceTypeIsStubway() throws IOException, ServletException {
        Mockito.when(resource.getResourceType()).thenReturn("stubway/stub");
        Mockito.doNothing().when(stubServlet).service(request, response);

        stubFilter.doFilter(request, response, filterChain);

        Mockito.verify(stubServlet, times(1)).service(request, response);
    }

    @Test
    void testDoFilterIfResourceTypeIsNotStubway() throws IOException, ServletException {
        Mockito.when(resource.getResourceType()).thenReturn("fake/resource");
        Mockito.doNothing().when(filterChain).doFilter(request, response);

        stubFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, times(1)).doFilter(request, response);
    }

}