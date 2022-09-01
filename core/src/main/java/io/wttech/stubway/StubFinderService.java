package io.wttech.stubway;

import io.wttech.stubway.matcher.MatchersRegistry;
import io.wttech.stubway.request.FetchingParametersException;
import io.wttech.stubway.request.MissingSupportedMethodException;
import io.wttech.stubway.request.RequestParameters;
import io.wttech.stubway.response.StubResponse;
import io.wttech.stubway.stub.Stub;
import org.apache.commons.lang.ObjectUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component(service = StubFinderService.class)
public class StubFinderService {

	@Reference
	private MatchersRegistry matchersRegistry;

	public StubResponse getStubResponse(SlingHttpServletRequest request) {
		String resourcePath = request.getRequestPathInfo().getResourcePath();
		Resource stubsResource = request.getResourceResolver().getResource(resourcePath);
		try {
			RequestParameters params = new RequestParameters(request);
			return createStubResponse(stubsResource, params);
		} catch (FetchingParametersException fpe) {
			return StubResponse.internalError(fpe);
		}
	}

	private StubResponse createStubResponse(Resource stubsResource, RequestParameters params) {
		return getStub(stubsResource, params).map(StubResponse::foundStub).orElse(StubResponse.empty());
	}

	private Optional<Stub> getStub(Resource stubsResource, RequestParameters params) {
		return Optional.ofNullable(stubsResource)
				.map(Resource::getChildren)
				.map(resources -> toStub(resources, params));
	}

	private Stub toStub(Iterable<Resource> resources, RequestParameters params) {
		return StreamSupport.stream(resources.spliterator(), false)
				.map(resource -> resource.adaptTo(Stub.class))
				.filter(Objects::nonNull)
				.filter(stub -> isMethodMatching(stub, params))
				.filter(stub -> isStubMatching(stub, params))
				.findFirst()
				.orElse(null);
	}

	private boolean isMethodMatching(Stub stub, RequestParameters params) {
		try {
			return ObjectUtils.equals(params.getMethod(), stub.getMethod());
		} catch (MissingSupportedMethodException ex) {
			return false;
		}
	}

	private boolean isStubMatching(Stub stub, RequestParameters params) {
		return matchersRegistry.getMatcher(params.getMethod()).matches(stub, params);
	}
}
