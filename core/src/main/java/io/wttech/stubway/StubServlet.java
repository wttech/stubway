package io.wttech.stubway;

import java.io.IOException;

import javax.servlet.Servlet;

import io.wttech.stubway.response.StubResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.http.entity.ContentType;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = { Servlet.class, StubServlet.class }, property = {
		Constants.SERVICE_DESCRIPTION + "=Stubway Servlet", "sling.servlet.methods=" + HttpConstants.METHOD_POST,
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.methods=" + HttpConstants.METHOD_PUT,
		"sling.servlet.methods=" + HttpConstants.METHOD_DELETE,
		"sling.servlet.resourceTypes=" + StubConstants.STUB_RESOURCE_TYPE })

public class StubServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -5079391671456169468L;

	@Reference
	private transient StubFinderService stubFinderService;

	@Override
	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		handleRequest(request, response);
	}

	@Override
	public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		handleRequest(request, response);
	}

	@Override
	public void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		handleRequest(request, response);
	}

	@Override
	public void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		handleRequest(request, response);
	}

	private void handleRequest(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		response.setContentType(ContentType.APPLICATION_JSON.toString());
		response.setCharacterEncoding(CharEncoding.UTF_8);

		StubResponse stubResponse = stubFinderService.getStubResponse(request);
		response.setStatus(stubResponse.getStatusCode());
		IOUtils.copy(stubResponse.getInputStream(), response.getOutputStream());
	}

}
