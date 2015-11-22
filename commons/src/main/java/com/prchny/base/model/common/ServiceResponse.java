
package com.prchny.base.model.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.dyuproject.protostuff.Tag;
import com.prchny.base.audit.annotation.AuditableClass;
import com.prchny.base.audit.annotation.AuditableField;
import com.prchny.base.transport.service.ITransportService.Protocol;
import com.prchny.base.validation.ValidationError;

@AuditableClass
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceResponse implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = -7608591897563945814L;
  
  @AuditableField
  @Tag(1)
  private boolean successful = true;
  
  @AuditableField
  @Tag(9768)
  private String code;
  
  @AuditableField
  @Tag(2)
  private String message;
  
  @AuditableField
  @Tag(3)
  private List<ValidationError> validationErrors =
      new ArrayList<ValidationError>(1);
  
  @Tag(4)
  protected Protocol protocol;
  
  public ServiceResponse() {
  
  }
  
  /**
   * @deprecated use <code> new ServiceResponse(code,message) </code> instead.
   */
  @Deprecated
  public ServiceResponse(final boolean successful, final String message) {
  
    this.successful = successful;
    this.message = message;
  }
  
  public ServiceResponse(final String code, final String message) {
  
    this.code = code;
    this.message = message;
  }
  
  public ServiceResponse(final String code) {
  
    this.code = code;
  }
  
  /**
   * @return the successful
   * @deprecated use {@link getCode()} instead.
   */
  @Deprecated
  public boolean isSuccessful() {
  
    return successful;
  }
  
  /**
   * @param successful
   *          the successful to set
   * @deprecated use {@link setCode()} instead.
   */
  @Deprecated
  public void setSuccessful(final boolean successful) {
  
    this.successful = successful;
  }
  
  public String getCode() {
  
    return code;
  }
  
  public void setCode(final String code) {
  
    this.code = code;
  }
  
  /**
   * @return the message
   */
  public String getMessage() {
  
    return message;
  }
  
  /**
   * @param message
   *          the message to set
   */
  public void setMessage(final String message) {
  
    this.message = message;
  }
  
  /**
   * @deprecated use {@link setCode()} instead.
   */
  @Deprecated
  public void setValidationErrors(final List<ValidationError> validationErrors) {
  
    this.validationErrors = validationErrors;
  }
  
  /**
   * @deprecated use {@link getCode()} instead.
   */
  @Deprecated
  public List<ValidationError> getValidationErrors() {
  
    return validationErrors;
  }
  
  /**
   * @deprecated use {@link setCode()} instead.
   */
  @Deprecated
  public void addValidationError(final ValidationError error) {
  
    if (validationErrors == null) {
      validationErrors = new ArrayList<ValidationError>();
    }
    validationErrors.add(error);
  }
  
  public Protocol getProtocol() {
  
    return protocol;
  }
  
  public void setProtocol(final Protocol protocol) {
  
    this.protocol = protocol;
  }
  
}
