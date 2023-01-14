package com.guludoc.learning.u3app.uaa.exception;

import com.guludoc.learning.u3app.uaa.config.Constants;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class UserNotEnabledProblem extends AbstractThrowableProblem {
    private static final URI TYPE = URI.create(Constants.PROBLEM_BASE_URI + "/notenabled");

    public UserNotEnabledProblem(String problem) {
        super(TYPE, "NotEnabled", Status.CONFLICT, problem);
    }
}

