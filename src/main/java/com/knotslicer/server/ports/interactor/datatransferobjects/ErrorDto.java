package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ErrorDtoImpl.class)
public interface ErrorDto {
    public int getErrorCode();
    public void setErrorCode(int errorCode);
    public String getErrorMessage();
    public void setErrorMessage(String errorMessage);

}
