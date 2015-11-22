package com.prchny.base.services.request.context;

import java.io.Serializable;

import com.prchny.base.utils.StringUtils;

public class LocalContextProvider implements Serializable {
  
  private static final long serialVersionUID = -2751500036230533957L;
  
  private RequestContextSRO requestContextSRO;
  
  private String metadata;
  
  public LocalContextProvider(final RequestContextSRO requestContextSRO) {
  
    super();
    this.requestContextSRO = requestContextSRO;
  }
  
  public RequestContextSRO getRequestContextSRO() {
  
    return requestContextSRO;
  }
  
  public void setRequestContextSRO(final RequestContextSRO requestContextSRO) {
  
    this.requestContextSRO = requestContextSRO;
  }
  
  public String getMetadata() {
  
    return metadata;
  }
  
  public void setMetadata(final String metadata) {
  
    this.metadata = metadata;
  }
  
  @Override
  public String toString() {
  
    if (StringUtils.isEmpty(getMetadata())) {
      return getRequestContextSRO().toString();
    }
    return requestContextSRO + "|metadata=" + metadata + "]";
  }
  
}
