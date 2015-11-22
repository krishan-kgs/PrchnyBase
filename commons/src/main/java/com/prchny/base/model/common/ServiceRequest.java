
package com.prchny.base.model.common;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.nustaq.serialization.annotations.Version;

import com.dyuproject.protostuff.Tag;
import com.prchny.base.audit.annotation.AuditableClass;
import com.prchny.base.audit.annotation.AuditableField;
import com.prchny.base.services.request.context.RequestContextSRO;
import com.prchny.base.transport.service.ITransportService.Protocol;

@JsonIgnoreProperties(ignoreUnknown = true)
@AuditableClass
public abstract class ServiceRequest implements Serializable {
  
  private static final long serialVersionUID = -5519767113471859157L;
  
  @Tag(1)
  private Protocol responseProtocol;
  
  @Tag(2)
  private Protocol requestProtocol;
  
  @AuditableField
  @Tag(999)
  private String userTrackingId;
  
  @AuditableField
  @Tag(1000)
  @Version(1)
  private RequestContextSRO contextSRO;
  
  public Protocol getResponseProtocol() {
  
    return responseProtocol;
  }
  
  public void setResponseProtocol(final Protocol protocol) {
  
    responseProtocol = protocol;
  }
  
  public Protocol getRequestProtocol() {
  
    return requestProtocol;
  }
  
  public void setRequestProtocol(final Protocol requestProtocol) {
  
    this.requestProtocol = requestProtocol;
  }
  
  public String getUserTrackingId() {
  
    return userTrackingId;
  }
  
  public void setUserTrackingId(final String userTrackingId) {
  
    this.userTrackingId = userTrackingId;
  }
  
  public RequestContextSRO getContextSRO() {
  
    return contextSRO;
  }
  
  public void setContextSRO(final RequestContextSRO contextSRO) {
  
    this.contextSRO = contextSRO;
  }
  
  /**
   * @Logic This will return an API specific log code. By default a null value
   *        will be returned. This function needs to be overridden(optional) by
   *        the classes extending ServiceRequest class, from those classes you
   *        can return api-specific-unique-logging code and can append that in
   *        your server's logs to track all logs for that particular API hit.
   * @return {@link String}
   */
  public String getUniqueLoggingCode() {
  
    return null;
  }
  
}
