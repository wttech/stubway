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
		for (Stub stub : getAllStubs(stubsResource)) {
			try {
				if (isStubMatching(stub, params) && isMethodMatching(stub, params)) {
					return StubResponse.foundStub(stub);
				}
			} catch (MissingSupportedMethodException exception) {
				return StubResponse.error(exception);
			}
		}
		return StubResponse.empty();
	}

	private List<Stub> getAllStubs(Resource stubsResource) {
		return Optional.ofNullable(stubsResource)
				.map(Resource::getChildren)
				.map(this::toStubs)
				.orElse(Collections.emptyList());
	}

	private List<Stub> toStubs(Iterable<Resource> resources) {
		return StreamSupport.stream(resources.spliterator(), false)
				.map(resource -> resource.adaptTo(Stub.class))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	private boolean isMethodMatching(Stub stub, RequestParameters params) throws MissingSupportedMethodException {
		return ObjectUtils.equals(params.getMethod(), stub.getMethod());
	}

	private boolean isStubMatching(Stub stub, RequestParameters params) {
		return matchersRegistry.getMatcher(params.getMethod()).matches(stub, params);
	}

}
