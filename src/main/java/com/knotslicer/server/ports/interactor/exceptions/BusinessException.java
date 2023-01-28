package com.knotslicer.server.ports.interactor.exceptions;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -111L;
    public BusinessException(String message) {
        super(message);
    }
    public BusinessException() {}
}