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

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prchny.base.entity.SmsChannel;
import com.prchny.base.notification.sms.ISmsSender;
import com.prchny.base.transport.http.HttpSender;
import com.prchny.base.transport.http.HttpTransportException;
import com.prchny.base.utils.StringUtils;

public class ValueFirstSmsSender implements ISmsSender {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(ValueFirstSmsSender.class);
  
  private String username;
  
  private String password;
  
  private String mask;
  
  @Override
  public void initialize(final SmsChannel smsChannel) {
  
    username = smsChannel.getUsername();
    password = smsChannel.getPassword();
    mask =
        StringUtils.isNotEmpty(smsChannel.getMask()) ? smsChannel.getMask()
            : "SNAPDEAL";
  }
  
  @Override
  public void send(final String mobile, String message) {
  
    final StringBuilder builder = new StringBuilder();
    
    // Header
    builder
        .append(
            "<?xml version='1.0' encoding='ISO-8859-1\'?><!DOCTYPE MESSAGE SYSTEM 'http://127.0.0.1/psms/dtd/messagev12.dtd' ><MESSAGE VER='1.2'><USER USERNAME='")
        .append(username).append("' PASSWORD='").append(password)
        .append("' />");
    
    message = StringEscapeUtils.escapeXml(message);
    builder.append("<SMS UDH=\"0\" CODING=\"1\" TEXT=\"").append(message)
        .append("\" PROPERTY=\"0\" ID=\"1\">");
    builder.append("<ADDRESS FROM='").append(mask).append("' TO=\"91")
        .append(mobile).append("\" SEQ='1' TAG='' />");
    builder.append("</SMS></MESSAGE>");
    final HttpSender sender = new HttpSender("valueFirst");
    final Map<String, String> params = new HashMap<String, String>();
    params.put("data", builder.toString());
    params.put("action", "send");
    try {
      final String response =
          sender
              .executePost(
                  "http://api.myvaluefirst.com/psms/servlet/psms.Eservice2",
                  params);
      LOG.info("Message: {}. Value first api response: {}", message, response);
    } catch (final HttpTransportException e) {
      LOG.error("unable to send sms :{}, to mobile:{}", message, mobile);
      throw new RuntimeException(e);
    }
  }
}
