package com.knotslicer.server.ports.interactor.exceptions;

public class EntityNotFoundException extends BusinessException {
    private static final long serialVersionUID = -222L;
    public EntityNotFoundException(String message) {
        super(message);
    }
    public EntityNotFoundException() {}
}
