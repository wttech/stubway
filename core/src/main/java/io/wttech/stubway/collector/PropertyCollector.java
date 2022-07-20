package io.wttech.stubway.collector;

import io.wttech.stubway.request.RequestParameters;
import io.wttech.stubway.stub.StubProperty;

import java.util.Set;

public interface PropertyCollector {
    Set<StubProperty> collectProperties(RequestParameters request);
}
