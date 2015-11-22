package com.prchny.base.exception;

public class IllegalDataUploadedException extends Exception {
  
  private static final long serialVersionUID = -1765123816225325060L;
  
  private String errorMessage;
  
  public IllegalDataUploadedException() {
  
  }
  
  public IllegalDataUploadedException(final String message) {
  
    errorMessage = message;
  }
  
  public String getErrorMessage() {
  
    return errorMessage;
  }
  
  public void setErrorMessage(final String errorMessage) {
  
    this.errorMessage = errorMessage;
  }
  
}
