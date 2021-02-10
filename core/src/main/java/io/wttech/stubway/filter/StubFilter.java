package io.wttech.stubway.filter;

import io.wttech.stubway.StubServlet;
import io.wttech.stubway.StubConstants;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = Filter.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Allows bypassing CSRF protection for stubs.",
		Constants.SERVICE_RANKING + ":Integer=2147483647",
		EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
		EngineConstants.SLING_FILTER_RESOURCETYPES + "=" + StubConstants.STUB_RESOURCE_TYPE })
public final class StubFilter implements Filter {

	@Reference
	private StubServlet stubServlet;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
		String resourceType = slingRequest.getResource().getResourceType();
		if (resourceType.equals(StubConstants.STUB_RESOURCE_TYPE)) {
			stubServlet.service(request, response);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) {
		// nothing to do
	}

	@Override
	public void destroy() {
		// nothing to do
	}
}
