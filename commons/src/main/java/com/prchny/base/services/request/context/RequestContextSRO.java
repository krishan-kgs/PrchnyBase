package com.prchny.base.services.request.context;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.dyuproject.protostuff.Tag;
import com.prchny.base.audit.annotation.AuditableClass;
import com.prchny.base.audit.annotation.AuditableField;

/**
 * Default implementation of {@link RequestContextProvider} <br>
 * This may be overridden by changing the code in aspect, having a creation
 * factory bean initialized in appContext
 * 
 * @author ajinkya
 */
@AuditableClass
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestContextSRO implements Serializable {
  
  private static final long serialVersionUID = -4788606633051950734L;
  
  @AuditableField
  @Tag(1)
  private String requestId;
  
  @AuditableField
  @Tag(2)
  private String appIdent;
  
  @AuditableField
  @Tag(3)
  private String appIP;
  
  @AuditableField
  @Tag(4)
  private String apiVariantId; // For A/B testing
  
  public RequestContextSRO() {
  
    super();
  }
  
  public String getRequestId() {
  
    return requestId;
  }
  
  public void setRequestId(final String requestId) {
  
    this.requestId = requestId;
  }
  
  public String getAppIdent() {
  
    return appIdent;
  }
  
  public void setAppIdent(final String appIdent) {
  
    this.appIdent = appIdent;
  }
  
  public String getAppIP() {
  
    return appIP;
  }
  
  public void setAppIP(final String appIP) {
  
    this.appIP = appIP;
  }
  
  public String getApiVariantId() {
  
    return apiVariantId;
  }
  
  public void setApiVariantId(final String apiVariantId) {
  
    this.apiVariantId = apiVariantId;
  }
  
  @JsonIgnore
  public RequestContextSRO getCopy() {
  
    final RequestContextSRO context = new RequestContextSRO();
    context.setRequestId(getRequestId());
    context.setAppIP(getAppIP());
    context.setAppIdent(getAppIdent());
    context.setApiVariantId(getApiVariantId());
    return context;
  }
  
  @Override
  public String toString() {
  
    return "[requestId=" + requestId + "|appIdent=" + appIdent + "|appIP="
        + appIP + "|apiVariantId=" + apiVariantId + "]";
  }
  
}
