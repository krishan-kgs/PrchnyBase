/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Oct 7, 2010
 *  @author singla
 */

package com.prchny.base.vo;

import java.util.List;

import com.prchny.base.velocity.Template;

public class EmailTemplateVO {
  
  private String name;
  
  private Template bodyTemplate;
  
  private Template plainTextBodyTemplate;
  
  private Template subjectTemplate;
  
  private List<String> to;
  
  private List<String> cc;
  
  private List<String> bcc;
  
  private String from;
  
  private String replyTo;
  
  private int emailChannelId;
  
  public String getName() {
  
    return name;
  }
  
  public void setName(final String name) {
  
    this.name = name;
  }
  
  public Template getBodyTemplate() {
  
    return bodyTemplate;
  }
  
  public void setBodyTemplate(final Template bodyTemplate) {
  
    this.bodyTemplate = bodyTemplate;
  }
  
  public Template getPlainTextBodyTemplate() {
  
    return plainTextBodyTemplate;
  }
  
  public void setPlainTextBodyTemplate(final Template plainTextBodyTemplate) {
  
    this.plainTextBodyTemplate = plainTextBodyTemplate;
  }
  
  public Template getSubjectTemplate() {
  
    return subjectTemplate;
  }
  
  public void setSubjectTemplate(final Template subjectTemplate) {
  
    this.subjectTemplate = subjectTemplate;
  }
  
  public List<String> getTo() {
  
    return to;
  }
  
  public void setTo(final List<String> to) {
  
    this.to = to;
  }
  
  public List<String> getCc() {
  
    return cc;
  }
  
  public void setCc(final List<String> cc) {
  
    this.cc = cc;
  }
  
  public List<String> getBcc() {
  
    return bcc;
  }
  
  public void setBcc(final List<String> bcc) {
  
    this.bcc = bcc;
  }
  
  public String getFrom() {
  
    return from;
  }
  
  public void setFrom(final String from) {
  
    this.from = from;
  }
  
  public String getReplyTo() {
  
    return replyTo;
  }
  
  public void setReplyTo(final String replyTo) {
  
    this.replyTo = replyTo;
  }
  
  public void setEmailChannelId(final int emailChannelId) {
  
    this.emailChannelId = emailChannelId;
  }
  
  public int getEmailChannelId() {
  
    return emailChannelId;
  }
}
