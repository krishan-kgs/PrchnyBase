package com.prchny.base.exception;

public class TransportException extends PrchnyWSException {
  
  private static final long serialVersionUID = -4815032384641879416L;
  
  public enum TransportErrorCode {
    ILLEGAL_ARGUMENT_EXCEPTION(
        "111", "Illegal Argument Exception In Reflection"),
    ILLEGAL_ACCESS_EXCEPTION(
        "112", "Illegal Access Exception In Reflection"),
    HTTP_TRANSPORT_EXCEPTION(
        "113", "Exception while making request to remote server."),
    BASE_URL_MISSING_EXCEPTION(
        "114", "Base URL not defined");
    
    private String code;
    
    private String description;
    
    private TransportErrorCode(final String code, final String description) {
    
      this.code = code;
      this.description = description;
    }
    
    public String code() {
    
      return code;
    }
    
    public String description() {
    
      return description;
    }
  }
  
  private String code;
  
  public TransportException(final String code, final String message,
      final Throwable cause) {
  
    super(message, cause);
    this.code = code;
  }
  
  public TransportException(final String code, final String message,
      final Integer wsErrorCode, final Throwable cause) {
  
    super(message, cause);
    super.setWsErrorCode(wsErrorCode);
    this.code = code;
  }
  
  public TransportException(final TransportErrorCode errorCode,
      final Throwable cause) {
  
    this(errorCode.code(), errorCode.description(), cause);
  }
  
  public TransportException(final Integer wsErrorCode, final String message,
      final Throwable cause) {
  
    super(wsErrorCode, message, cause);
  }
  
  public TransportException(final TransportErrorCode errorCode,
      final Integer wsErrorCode, final Throwable cause) {
  
    this(errorCode.code(), errorCode.description(), cause);
    super.setWsErrorCode(wsErrorCode);
  }
  
  public TransportException(final TransportErrorCode errorCode) {
  
    super();
    code = errorCode.code();
  }
  
  public String getCode() {
  
    return code;
  }
  
}
