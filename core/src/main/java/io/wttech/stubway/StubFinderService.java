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

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component(service = StubFinderService.class)
public class StubFinderService {

	@Reference
	private MatchersRegistry matchersRegistry;

	public StubResponse getStubResponse(SlingHttpServletRequest request) {
		String resourcePath = request.getRequestPathInfo().getResourcePath();
		Resource stubEndpoint = request.getResourceResolver().getResource(resourcePath);
		try {
			RequestParameters stubParams = new RequestParameters(request);
			return createStubResponse(stubEndpoint, stubParams);
		} catch (FetchingParametersException fpe) {
			return StubResponse.internalError(fpe);
		}
	}

	private StubResponse createStubResponse(Resource stubEndpoint, RequestParameters stubParams) {
		return getStub(stubEndpoint, stubParams).map(StubResponse::foundStub).orElse(StubResponse.empty());
	}

	private Optional<Stub> getStub(Resource stubEndpoint, RequestParameters stubParams) {
		return Optional.ofNullable(stubEndpoint)
				.map(Resource::getChildren)
				.map(stubsResources -> toStub(stubsResources, stubParams));
	}

	private Stub toStub(Iterable<Resource> stubResources, RequestParameters stubParams) {
		return StreamSupport.stream(stubResources.spliterator(), false)
				.map(resource -> resource.adaptTo(Stub.class))
				.filter(Objects::nonNull)
				.filter(stub -> isMethodMatching(stub, stubParams))
				.filter(stub -> isStubMatching(stub, stubParams))
				.findFirst()
				.orElse(null);
	}

	private boolean isMethodMatching(Stub stub, RequestParameters stubParams) {
		try {
			return ObjectUtils.equals(stubParams.getMethod(), stub.getMethod());
		} catch (MissingSupportedMethodException ex) {
			return false;
		}
	}

	private boolean isStubMatching(Stub stub, RequestParameters stubParams) {
		return matchersRegistry.getMatcher(stubParams.getMethod()).matches(stub, stubParams);
	}
}
