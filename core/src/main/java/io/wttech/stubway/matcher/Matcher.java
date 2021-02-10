package io.wttech.stubway.matcher;

import io.wttech.stubway.stub.Stub;
import io.wttech.stubway.request.RequestParameters;

public interface Matcher {

	boolean matches(Stub stub, RequestParameters requestParameters);

}
