package com.knotslicer.server.ports.interactor.exceptions;

import java.util.List;

public class ValidationException extends BusinessException {

    private static final long serialVersionUID = -333;
    private final List<String> violations;
    public ValidationException(List<String> violations) {
        this.violations = violations;
    }
    public List<String> getMessages() {
        return violations;
    }
}