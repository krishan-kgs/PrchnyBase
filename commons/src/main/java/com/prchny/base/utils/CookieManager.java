/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Jun 16, 2014
 *  @author jitesh
 */

package com.prchny.base.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieManager {
  
  public static final String USER_TRACKING_COOKIE = "u";
  
  public static void setUserTrackingId(final HttpServletRequest request,
      final HttpServletResponse response) {
  
    if (CookieManager.getCookie(request, USER_TRACKING_COOKIE) == null) {
      CookieManager.setCookie(response, USER_TRACKING_COOKIE,
          StringUtils.generateUserTrackingId());
    }
  }
  
  public static void setCookie(final HttpServletResponse response,
      final String name, final String value) {
  
    setCookie(response, name, value, Integer.MAX_VALUE);
  }
  
  public static void setCookie(final HttpServletResponse response,
      final String name, final String value, final int expiry) {
  
    final Cookie ckUser = new Cookie(name, value);
    ckUser.setMaxAge(expiry);
    ckUser.setPath("/");
    ckUser.setDomain(".prchny.com");
    response.addCookie(ckUser);
    response
        .setHeader(
            "P3P",
            "policyref=\"http://www.prchny.com/w3c/p3p.xml\", "
                + "CP=\"NOI CURa ADMa DEVa TAIa OUR BUS IND UNI COM NAV INT\";HttpOnly");
  }
  
  public static Cookie getCookie(final HttpServletRequest request,
      final String name) {
  
    final Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (final Cookie cookie : cookies) {
        if (name.equals(cookie.getName())) {
          return cookie;
        }
      }
    }
    return null;
  }
  
  public static void deleteCookie(final HttpServletRequest request,
      final HttpServletResponse response, final String name) {
  
    final Cookie cookie = getCookie(request, name);
    if (cookie != null) {
      cookie.setMaxAge(0);
      cookie.setPath("/");
      cookie.setDomain(".prchny.com");
      response.addCookie(cookie);
    }
  }
}
