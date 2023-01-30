package com.knotslicer.server.ports.interactor.datatransferobjects;

public class ErrorDtoImpl implements ErrorDto {
    private int errorCode;
    private String errorMessage;
    public ErrorDtoImpl(){}
    public ErrorDtoImpl(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    public int getErrorCode() {return errorCode;}
    public void setErrorCode(int errorCode) {this.errorCode = errorCode;}
    public String getErrorMessage() {return errorMessage;}
    public void setErrorMessage(String errorMessage) {this.errorMessage = errorMessage;}

}
