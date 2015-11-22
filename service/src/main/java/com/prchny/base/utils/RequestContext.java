/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Nov 30, 2010
 *  @author singla
 */

package com.prchny.base.utils;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Transient;

public class RequestContext {
  
  private static ThreadLocal<RequestContext> ctx =
      new ThreadLocal<RequestContext>();
  
  private String userTrackingId;
  
  private Map<String, String> sourceTrackingParams =
      new HashMap<String, String>();
  
  private Map<String, String> visitTrackingParams =
      new HashMap<String, String>();
  
  private String affiliateTrackingCode;
  
  private String requestFrom;
  
  private String countryCode;
  
  private String browser;
  
  private int userId;
  
  private String loginToken;
  
  private String userAgent;
  
  private String mobileViewSet;
  
  private Integer width;
  
  private String requestUrl;
  
  public static final String BROWSER_IE_9 = "IE9";
  
  public static final String BROWSER_SAFARI = "Safari";
  
  public static final String BROWSER_UC = "UCBrowser";
  
  public static final String BROWSER_CHROME = "Chrome";
  
  public static final String BROWSER_MERCURY = "Mercury";
  
  public static RequestContext current() {
  
    RequestContext requestCtx = ctx.get();
    if (requestCtx == null) {
      requestCtx = new RequestContext();
      ctx.set(requestCtx);
    }
    return requestCtx;
  }
  
  public static void destroy() {
  
    ctx.remove();
  }
  
  public String getUserTrackingId() {
  
    return userTrackingId;
  }
  
  public void setUserTrackingId(final String userTrackingId) {
  
    this.userTrackingId = userTrackingId;
  }
  
  public Map<String, String> getSourceTrackingParams() {
  
    return sourceTrackingParams;
  }
  
  public void setSourceTrackingParams(
      final Map<String, String> sourceTrackingParams) {
  
    this.sourceTrackingParams = sourceTrackingParams;
  }
  
  public Map<String, String> getVisitTrackingParams() {
  
    return visitTrackingParams;
  }
  
  public void setVisitTrackingParams(
      final Map<String, String> visitTrackingParams) {
  
    this.visitTrackingParams = visitTrackingParams;
  }
  
  @Transient
  public String getSourceParams() {
  
    final StringBuilder builder = new StringBuilder();
    for (final String key : sourceTrackingParams.keySet()) {
      builder.append(key).append('=').append(sourceTrackingParams.get(key))
          .append('|');
    }
    return builder.toString();
  }
  
  @Transient
  public String getVisiterParams() {
  
    final StringBuilder builder = new StringBuilder();
    for (final String key : visitTrackingParams.keySet()) {
      builder.append(key).append('=').append(visitTrackingParams.get(key))
          .append('|');
    }
    return builder.toString();
  }
  
  public void setAffiliateTrackingCode(final String affiliateTrackingCode) {
  
    this.affiliateTrackingCode = affiliateTrackingCode;
  }
  
  public String getAffiliateTrackingCode() {
  
    return affiliateTrackingCode;
  }
  
  public void setRequestFrom(final String requestFrom) {
  
    this.requestFrom = requestFrom;
  }
  
  public String getRequestFrom() {
  
    return requestFrom;
  }
  
  public void setCountryCode(final String countryCode) {
  
    this.countryCode = countryCode;
  }
  
  public String getCountryCode() {
  
    return countryCode;
  }
  
  public String getBrowser() {
  
    return browser;
  }
  
  public void setBrowser(final String browser) {
  
    this.browser = browser;
  }
  
  public int getUserId() {
  
    return userId;
  }
  
  public void setUserId(final int userId) {
  
    this.userId = userId;
  }
  
  public String getLoginToken() {
  
    return loginToken;
  }
  
  public void setLoginToken(final String loginToken) {
  
    this.loginToken = loginToken;
  }
  
  public String getUserAgent() {
  
    return userAgent;
  }
  
  public void setUserAgent(final String userAgent) {
  
    this.userAgent = userAgent;
  }
  
  public String getMobileViewSet() {
  
    return mobileViewSet;
  }
  
  public void setMobileViewSet(final String mobileViewSet) {
  
    this.mobileViewSet = mobileViewSet;
  }
  
  public Integer getWidth() {
  
    return width;
  }
  
  public void setWidth(final Integer width) {
  
    this.width = width;
  }
  
  public String getRequestUrl() {
  
    return requestUrl;
  }
  
  public void setRequestUrl(final String requestUrl) {
  
    this.requestUrl = requestUrl;
  }
}
