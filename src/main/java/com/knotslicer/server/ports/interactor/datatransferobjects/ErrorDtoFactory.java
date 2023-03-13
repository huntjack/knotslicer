package com.knotslicer.server.ports.interactor.datatransferobjects;

public interface ErrorDtoFactory {
    public ErrorDto createErrorDto(int errorCode, String errorMessage);
}
