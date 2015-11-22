/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 21, 2010
 *  @author rahul
 */

package com.prchny.base.notification.sms.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prchny.base.entity.SmsChannel;
import com.prchny.base.notification.sms.ISmsSender;
import com.prchny.base.transport.http.HttpSender;
import com.prchny.base.transport.http.HttpTransportException;

public class GupShupSmsSender implements ISmsSender {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(GupShupSmsSender.class);
  
  private static final String API_CACHE_NAME = "SMSSender";
  
  private static final String SMS_SENDER_URL =
      "http://enterprise.smsgupshup.com/GatewayAPI/rest";
  
  private static final String METHOD_SEND_MESSAGE = "sendMessage";
  
  private String username;
  
  private String password;
  
  @Override
  public void initialize(final SmsChannel smsChannel) {
  
    username = smsChannel.getUsername();
    password = smsChannel.getPassword();
  }
  
  @Override
  public void send(final String mobile, final String message) {
  
    final HttpSender httpSender = new HttpSender(API_CACHE_NAME);
    final Map<String, String> paramsMap = getRequestParams(METHOD_SEND_MESSAGE);
    paramsMap.put("msg", message);
    paramsMap.put("send_to", mobile);
    try {
      httpSender.executeGet(SMS_SENDER_URL, paramsMap);
    } catch (final HttpTransportException e) {
      LOG.error("Unable to execute send", e);
    }
  }
  
  private Map<String, String> getRequestParams(final String method) {
  
    final HashMap<String, String> paramsMap = new HashMap<String, String>();
    paramsMap.put("userid", username);
    paramsMap.put("password", password);
    paramsMap.put("method", method);
    paramsMap.put("auth_scheme", "PLAIN");
    paramsMap.put("msg_type", "TEXT");
    paramsMap.put("v", "1.1");
    return paramsMap;
  }
}
