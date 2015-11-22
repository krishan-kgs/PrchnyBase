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

//http://luna.a2wi.co.in:7501/failsafe/HttpLink?aid=506027&pin=snap@1&mnumber=9711778168&message=Testmsg123
public class Air2WebSmsSender implements ISmsSender {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(Air2WebSmsSender.class);
  
  private static final String API_CACHE_NAME = "SMSSender";
  
  private static final String SMS_SENDER_URL =
      "http://luna.a2wi.co.in:7501/failsafe/HttpLink";
  
  private String accountId;
  
  private String password;
  
  @Override
  public void initialize(final SmsChannel smsChannel) {
  
    accountId = smsChannel.getUsername();
    password = smsChannel.getPassword();
  }
  
  @Override
  public void send(final String mobile, final String message) {
  
    final HttpSender httpSender = new HttpSender(API_CACHE_NAME);
    final Map<String, String> paramsMap = new HashMap<String, String>();
    paramsMap.put("aid", accountId);
    paramsMap.put("pin", password);
    paramsMap.put("message", message);
    paramsMap.put("mnumber", mobile);
    try {
      final String response = httpSender.executeGet(SMS_SENDER_URL, paramsMap);
      LOG.info("Message: {}. Air2Web Api response: {}", message, response);
    } catch (final HttpTransportException e) {
      LOG.error("Unable to execute send", e);
    }
  }
  
}
