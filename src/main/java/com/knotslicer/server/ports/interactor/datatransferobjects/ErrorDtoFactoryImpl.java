package com.knotslicer.server.ports.interactor.datatransferobjects;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ErrorDtoFactoryImpl implements ErrorDtoFactory {
    @Override
    public ErrorDto createErrorDto(int errorCode, String errorMessage) {
        return new ErrorDtoImpl(errorCode, errorMessage);
    }
}
