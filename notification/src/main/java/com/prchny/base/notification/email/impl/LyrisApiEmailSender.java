/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Mar 15, 2011
 *  @author rahul
 */

package com.prchny.base.notification.email.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prchny.base.entity.EmailChannel;
import com.prchny.base.notification.email.EmailMessage;
import com.prchny.base.notification.email.IEmailSender;
import com.prchny.base.transport.http.HttpSender;
import com.prchny.base.transport.http.HttpTransportException;
import com.prchny.base.utils.StringUtils;
import com.prchny.base.vo.EmailTemplateVO;

public class LyrisApiEmailSender implements IEmailSender {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(LyrisApiEmailSender.class);
  
  private static final String LYRIS_URL =
      "https://elabs11.com/API/mailing_list.html";
  
  private String siteId;
  
  private String mlid;
  
  private String triggerId;
  
  private String tracking;
  
  private String lyrisURL;
  
  private EmailChannel channel;
  
  @Override
  public void initialize(final EmailChannel channel) {
  
    siteId = channel.getParameter("siteId");
    mlid = channel.getParameter("mlid");
    triggerId = channel.getParameter("triggerId");
    tracking = channel.getParameter("trackingState");
    lyrisURL = channel.getParameter("lyrisURL");
    this.channel = channel;
    if (StringUtils.isEmpty(lyrisURL)) {
      lyrisURL = LYRIS_URL;
    }
    if ((siteId == null) || (mlid == null) || (triggerId == null)
        || (tracking == null)) {
      throw new IllegalStateException(
          "Lyris Api Email Sender is not properly configured");
    }
  }
  
  @Override
  public void send(final EmailMessage message, final EmailTemplateVO template) {
  
    final StringBuilder recipients = new StringBuilder();
    String fromEmail = null;
    String fromName = null;
    
    if ((message.getTo() == null) || (message.getTo().size() == 0)) {
      message.addRecepients(template.getTo());
    }
    if ((message.getTo() == null) || (message.getTo().size() == 0)) {
      throw new IllegalArgumentException("No recipient specified.");
    } else {
      recipients.append(StringUtils.join(',', message.getTo()));
    }
    
    if (template.getCc() != null) {
      message.addCCs(template.getCc());
    }
    
    if ((message.getCc() != null) && (message.getCc().size() > 0)) {
      recipients.append(',').append(StringUtils.join(',', message.getCc()));
    }
    
    if (template.getBcc() != null) {
      message.addBCCs(template.getBcc());
    }
    
    if ((message.getBcc() != null) && (message.getBcc().size() > 0)) {
      recipients.append(',').append(StringUtils.join(',', message.getBcc()));
    }
    
    if (message.getFrom() == null) {
      fromName =
          template.getFrom().substring(0, template.getFrom().indexOf('<'));
      fromEmail =
          template.getFrom().substring(template.getFrom().indexOf('<') + 1,
              template.getFrom().indexOf('>'));
    } else {
      fromName = message.getFrom().substring(0, message.getFrom().indexOf('<'));
      fromEmail =
          message.getFrom().substring(message.getFrom().indexOf('<') + 1,
              message.getFrom().indexOf('>'));
    }
    
    final StringBuilder transactXML = new StringBuilder();
    transactXML
        .append("<DATASET><SITE_ID>")
        .append(siteId)
        .append("</SITE_ID><DATA type='extra' id='password'>prchny123</DATA>");
    transactXML.append("<MLID>").append(mlid).append("</MLID>");
    transactXML.append("<DATA type='extra' id='trigger_ID'>")
        .append(getTriggerIdByEmailName(message.getTemplateName()))
        .append("</DATA>");
    transactXML.append("<DATA type='extra' id='recipients'>")
        .append(recipients.toString()).append("</DATA>");
    transactXML
        .append("<DATA type='extra' id='subject'>")
        .append(
            StringEscapeUtils.escapeXml(template.getSubjectTemplate().evaluate(
                message.getTemplateParams()))).append("</DATA>");
    transactXML.append("<DATA type='extra' id='from_email'>").append(fromEmail)
        .append("</DATA>");
    transactXML.append("<DATA type='extra' id='from_name'>").append(fromName)
        .append("</DATA>");
    transactXML.append("<DATA type='extra' id='clickthru'>").append(tracking)
        .append("</DATA>");
    if (template.getPlainTextBodyTemplate() != null) {
      transactXML
          .append("<DATA type='extra' id='message_text'>")
          .append(
              StringEscapeUtils.escapeXml(template.getPlainTextBodyTemplate()
                  .evaluate(message.getTemplateParams())))
          .append(
              StringEscapeUtils.escapeXml("<div class=\"unsubscribe\"></div>"))
          .append("</DATA>");
    } else {
      // Hardcoding "India’s largest e-commerce marketplace", since if the
      // "message_text" tag is absent, lyris duplicates the rich text version in
      // the plain text version
      transactXML.append("<DATA type='extra' id='message_text'>")
          .append("India’s largest e-commerce marketplace").append("</DATA>");
    }
    transactXML
        .append("<DATA type='extra' id='message'>")
        .append(
            StringEscapeUtils.escapeXml(template.getBodyTemplate().evaluate(
                message.getTemplateParams())))
        .append(
            StringEscapeUtils.escapeXml("<div class=\"unsubscribe\"></div>"))
        .append("</DATA>");
    transactXML.append("</DATASET>");
    
    final HttpSender httpSender = new HttpSender("lyrisEmailSystem");
    try {
      LOG.info("Sending email of type {}, to recipients: {}",
          template.getName(), recipients.toString());
      final String response =
          httpSender.executePost(
              lyrisURL,
              getRequestParams("triggers", "fire-trigger",
                  transactXML.toString()));
      LOG.info("lyris response for {} email: {}", template.getName(), response);
      LOG.info("Lyris Trigger ID: "
          + getTriggerIdByEmailName(message.getTemplateName())
          + " for the email type: " + message.getTemplateName());
    } catch (final HttpTransportException e) {
      LOG.error("error while sending email through lyris api", e);
    }
    
  }
  
  private String getTriggerIdByEmailName(final String emailName) {
  
    final String channelId =
        channel.getParameter(emailName + "_LyrisTriggerId");
    return channelId == null ? triggerId : channelId;
  }
  
  private Map<String, String> getRequestParams(final String type,
      final String activity, final String input) {
  
    final HashMap<String, String> paramsMap = new HashMap<String, String>();
    paramsMap.put("type", type);
    paramsMap.put("activity", activity);
    paramsMap.put("input", input);
    return paramsMap;
  }
  
  public static void main(final String[] args) throws HttpTransportException {
  
    final StringBuilder transactXML = new StringBuilder();
    transactXML
        .append("<DATASET><SITE_ID>")
        .append("2010001149")
        .append("</SITE_ID><DATA type='extra' id='password'>prchny123</DATA>");
    transactXML.append("<MLID>").append(46979).append("</MLID>");
    transactXML.append("<DATA type='extra' id='trigger_ID'>").append(8141)
        .append("</DATA>");
    transactXML.append("<DATA type='extra' id='recipients'>")
        .append("sunny.agarwal@jasperindia.com").append("</DATA>");
    transactXML.append("<DATA type='extra' id='subject'>")
        .append("Trigger Email").append("</DATA>");
    transactXML.append("<DATA type='extra' id='from_email'>")
        .append(StringEscapeUtils.escapeXml("noreply@prchnys.co.in"))
        .append("</DATA>");
    transactXML.append("<DATA type='extra' id='from_name'>").append("Prchny")
        .append("</DATA>");
    // transactXML.append("<DATA type='extra' id='tracking'>").append("off").append("</DATA>");
    transactXML.append("<DATA type='extra' id='clickthru'>").append("off")
        .append("</DATA>");
    transactXML.append("<DATA type='extra' id='message_text'>")
        .append(StringEscapeUtils.escapeXml("text message")).append("</DATA>");
    transactXML.append("<DATA type='extra' id='message'>")
        .append(StringEscapeUtils.escapeXml("<html>HTML Email Message</html>"))
        .append("</DATA>");
    transactXML.append("</DATASET>");
    final HttpSender httpSender = new HttpSender("lyrisEmailSystem");
    final HashMap<String, String> paramsMap = new HashMap<String, String>();
    paramsMap.put("type", "triggers");
    paramsMap.put("activity", "fire-trigger");
    paramsMap.put("input", transactXML.toString());
    final String response = httpSender.executePost(LYRIS_URL, paramsMap);
    System.out.println(response);
  }
}
