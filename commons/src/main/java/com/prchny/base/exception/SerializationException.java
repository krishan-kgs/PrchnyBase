package com.prchny.base.exception;

public class SerializationException extends Exception {
  
  private static final long serialVersionUID = -432384641879416L;
  
  public enum SerializationErrorCode {
    JSON_SERIALIZATION_EXCEPTION(
        "501", "Unable to serialize json"),
    JSON_DESERIALIZATION_EXCEPTION(
        "502", "Unable to deserialize json"),
    PROTOSTUFF_SERIALIZATION_EXCEPTION(
        "503", "Unable to serialize protostuff"),
    PROTOSTUFF_DESERIALIZATION_EXCEPTION(
        "504", "Unable to deserialize protostuff"),
    INSTANTIATION_EXCEPTION(
        "505",
        "Unable to initialise request/response class. May be zero argument constructor is missing."),
    IO_EXCEPTION(
        "506", "IO exception while creating object of request/response class."),
    FST_SERIALIZATION_EXCEPTION(
        "507", "Unable to serialize through fst"),
    FST_DESERIALIZATION_EXCEPTION(
        "508", "Unable to deserialize through fst"),
    UNSUPPORTED_MEDIA_TYPE(
        "509", "Unable to find serialization service for given content type");
    
    private String code;
    
    private String description;
    
    private SerializationErrorCode(final String code, final String description) {
    
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
  
  private final SerializationErrorCode errorCode;
  
  public SerializationException(final SerializationErrorCode code,
      final Throwable cause) {
  
    super(code.description(), cause);
    errorCode = code;
  }
  
  public SerializationException(final SerializationErrorCode code,
      final String message) {
  
    errorCode = code;
  }
  
  public SerializationException(final SerializationErrorCode code) {
  
    errorCode = code;
  }
  
  public SerializationErrorCode getErrorCode() {
  
    return errorCode;
  }
  
}
