package com.prchny.base.services.request.context;

import org.slf4j.MDC;

import com.prchny.base.startup.config.AppEnvironmentContext;
import com.prchny.base.utils.Constants;
import com.prchny.base.utils.StringUtils;

public class RequestContextSROFactory {
  
  public static RequestContextSRO createRequestContextSRO(
      final LocalContextProvider contextProvider) {
  
    final RequestContextSRO sro = new RequestContextSRO();
    if ((contextProvider != null)
        && (contextProvider.getRequestContextSRO() != null)) {
      sro.setRequestId(contextProvider.getRequestContextSRO().getRequestId());
    } else {
      sro.setRequestId(getRequestId());
    }
    sro.setAppIdent(getAppIdent());
    sro.setAppIP(getAppIP());
    return sro;
  }
  
  private static String getRequestId() {
  
    final String userTrackingId = MDC.get(Constants.REQUEST_USER_TRACKING_KEY);
    if (StringUtils.isNotEmpty(userTrackingId)) {
      return userTrackingId;
    }
    return StringUtils.generateUserTrackingId();
  }
  
  private static String getAppIdent() {
  
    return AppEnvironmentContext.getAppIdentifier();
  }
  
  private static String getAppIP() {
  
    return AppEnvironmentContext.getServerIPAddr();
  }
  
}
