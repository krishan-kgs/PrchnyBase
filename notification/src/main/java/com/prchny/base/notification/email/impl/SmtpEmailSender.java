/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 20, 2010
 *  @author singla
 */

package com.prchny.base.notification.email.impl;

import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.prchny.base.entity.EmailChannel;
import com.prchny.base.notification.email.EmailMessage;
import com.prchny.base.notification.email.IEmailSender;
import com.prchny.base.utils.StringUtils;
import com.prchny.base.vo.EmailTemplateVO;

public class SmtpEmailSender implements IEmailSender {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(SmtpEmailSender.class);
  
  private JavaMailSender mailSender;
  
  @Override
  public void initialize(final EmailChannel channel) {
  
    final JavaMailSenderImpl sender = new JavaMailSenderImpl();
    sender.setHost(channel.getParameter("host"));
    sender.setPort(Integer.parseInt(channel.getParameter("port")));
    sender.setUsername(channel.getParameter("username"));
    sender.setPassword(channel.getParameter("password"));
    final Properties javaMailProperties = new Properties();
    javaMailProperties.put("mail.smtps.auth", true);
    javaMailProperties.put("mail.smtps.starttls.enable", true);
    javaMailProperties.put("mail.smtps.debug", true);
    sender.setJavaMailProperties(javaMailProperties);
    mailSender = sender;
  }
  
  @Override
  public void send(final EmailMessage message, final EmailTemplateVO template) {
  
    mailSender.send(getHtmlMailMessage(message, template));
    LOG.info("Sent email of type {}", template.getName());
  }
  
  public MimeMessagePreparator getHtmlMailMessage(
      final EmailMessage emailMessage, final EmailTemplateVO template) {
  
    final MimeMessagePreparator preparator = new MimeMessagePreparator() {
      
      @Override
      public void prepare(final MimeMessage mimeMessage) throws Exception {
      
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        if ((emailMessage.getTo() == null)
            || (emailMessage.getTo().size() == 0)) {
          emailMessage.addRecepients(template.getTo());
          if ((emailMessage.getTo() == null)
              || (emailMessage.getTo().size() == 0)) {
            throw new IllegalArgumentException("No recipient specified.");
          }
          
          String from = emailMessage.getFrom();
          
          if (from == null) {
            from = template.getFrom();
          }
          
          if (template.getCc() != null) {
            emailMessage.addCCs(template.getCc());
          }
          
          if (template.getBcc() != null) {
            emailMessage.addBCCs(template.getBcc());
          }
          
          String replyTo = emailMessage.getReplyTo();
          
          if (replyTo == null) {
            replyTo = template.getReplyTo();
          }
          
          final StringBuilder recipients = new StringBuilder();
          recipients.append("TO: ").append(
              StringUtils.join(',', emailMessage.getTo()));
          for (final String toAddress : emailMessage.getTo()) {
            message.addTo(toAddress);
          }
          
          message.setFrom(from);
          
          if (emailMessage.getCc() != null) {
            recipients.append(", CC: ").append(
                StringUtils.join(',', emailMessage.getCc()));
            for (final String ccAddress : emailMessage.getCc()) {
              message.addCc(ccAddress);
            }
          }
          
          if (emailMessage.getBcc() != null) {
            recipients.append(", BCC: ").append(
                StringUtils.join(',', emailMessage.getBcc()));
            for (final String bccAddress : emailMessage.getBcc()) {
              message.addBcc(bccAddress);
            }
          }
          LOG.info("Recipients for email of type {}, are : {}",
              template.getName(), recipients.toString());
          if (replyTo != null) {
            message.setReplyTo(replyTo);
          }
          
          message.setSubject(template.getSubjectTemplate().evaluate(
              emailMessage.getTemplateParams()));
          message.setText(
              template.getBodyTemplate().evaluate(
                  emailMessage.getTemplateParams()), true);
        }
      }
    };
    return preparator;
  }
}
