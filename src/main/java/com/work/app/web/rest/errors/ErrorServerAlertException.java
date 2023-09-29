package com.work.app.web.rest.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ErrorServerAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final String params;

    private final String errorKey;

    public ErrorServerAlertException(String defaultMessage, String params, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, params, errorKey);
    }

    public ErrorServerAlertException(URI type, String defaultMessage, String params, String errorKey) {
        super(type, defaultMessage, Status.INTERNAL_SERVER_ERROR, null, null, null, getAlertParameters(params, errorKey));
        this.params = params;
        this.errorKey = errorKey;
    }

    /*public String getEntityName() {
        return entityName;
    }*/

    private static Map<String, Object> getAlertParameters(String params, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", "{\"mensajeservidor\":\"" + params + "\"}");
        return parameters;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
