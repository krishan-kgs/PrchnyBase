
package com.prchny.base.notification.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prchny.base.audit.annotation.AuditableClass;
import com.prchny.base.audit.annotation.AuditableField;

@AuditableClass
public class EmailMessage implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1485697410;
  
  @AuditableField
  private String from;
  
  private String replyTo;
  
  @AuditableField
  private List<String> to;
  
  private List<String> cc;
  
  private List<String> bcc;
  
  @AuditableField
  private String templateName;
  
  private String mailHTML;
  
  @AuditableField
  private final Map<String, Object> templateParams =
      new HashMap<String, Object>();
  
  public EmailMessage() {
  
  }
  
  public EmailMessage(final String templateName) {
  
    this.templateName = templateName;
    to = new ArrayList<String>();
  }
  
  public EmailMessage(final List<String> to, final String templateName) {
  
    this.to = to;
    this.templateName = templateName;
  }
  
  public EmailMessage(final List<String> to, final String from,
      final String replyTo, final String templateName) {
  
    this.from = from;
    this.replyTo = replyTo;
    this.to = to;
    this.templateName = templateName;
  }
  
  public EmailMessage(final String to, final String from, final String replyTo,
      final String templateName) {
  
    this.from = from;
    this.replyTo = replyTo;
    this.to = new ArrayList<String>(1);
    this.to.add(to);
    this.templateName = templateName;
  }
  
  public EmailMessage(final String to, final String templateName) {
  
    this.to = new ArrayList<String>(1);
    this.to.add(to);
    this.templateName = templateName;
  }
  
  public void addRecepient(final String to) {
  
    this.to.add(to);
  }
  
  public void addRecepients(final Collection<String> tos) {
  
    to.addAll(tos);
  }
  
  public void addCC(final String cc) {
  
    if (this.cc == null) {
      this.cc = new ArrayList<String>(1);
    }
    this.cc.add(cc);
  }
  
  public void addCCs(final Collection<String> ccs) {
  
    if (cc == null) {
      cc = new ArrayList<String>(ccs.size());
    }
    cc.addAll(ccs);
  }
  
  public void addBCC(final String bcc) {
  
    if (this.bcc == null) {
      this.bcc = new ArrayList<String>(1);
    }
    this.bcc.add(bcc);
  }
  
  public void addBCCs(final Collection<String> bccs) {
  
    if (bcc == null) {
      bcc = new ArrayList<String>(bccs.size());
    }
    bcc.addAll(bccs);
  }
  
  public void addTemplateParam(final String name, final Object value) {
  
    templateParams.put(name, value);
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
  
  public List<String> getTo() {
  
    return to;
  }
  
  public List<String> getCc() {
  
    return cc;
  }
  
  public List<String> getBcc() {
  
    return bcc;
  }
  
  public String getTemplateName() {
  
    return templateName;
  }
  
  public Map<String, Object> getTemplateParams() {
  
    return templateParams;
  }
  
  public void setMailHTML(final String mailHTML) {
  
    this.mailHTML = mailHTML;
  }
  
  public String getMailHTML() {
  
    return mailHTML;
  }
  
}
