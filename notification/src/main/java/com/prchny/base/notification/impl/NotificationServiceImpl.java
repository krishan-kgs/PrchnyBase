/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Apr 20, 2012
 *  @author amit
 */

package com.prchny.base.notification.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prchny.base.cache.CacheManager;
import com.prchny.base.cache.EmailChannelCache;
import com.prchny.base.cache.SmsChannelCache;
import com.prchny.base.entity.EmailChannel;
import com.prchny.base.entity.SmsChannel;
import com.prchny.base.notification.INotificationService;
import com.prchny.base.notification.email.EmailMessage;
import com.prchny.base.notification.email.IEmailSender;
import com.prchny.base.notification.sms.ISmsSender;
import com.prchny.base.notification.sms.SmsMessage;
import com.prchny.base.startup.service.IBaseStartupService;
import com.prchny.base.vo.EmailTemplateVO;
import com.prchny.base.vo.SmsTemplateVO;

@Service("notificationService")
@Transactional
public class NotificationServiceImpl implements INotificationService,
  DisposableBean {
  
  @Autowired
  IBaseStartupService baseStartupService;
  
  private static final Logger LOG = LoggerFactory
      .getLogger(NotificationServiceImpl.class);
  
  private final ExecutorService executorService = new ThreadPoolExecutor(20,
      100, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
  
  @Override
  public void sendEmail(final EmailMessage message,
      final EmailTemplateVO template) {
  
    LOG.info("Adding message type - {} to email queue.", template.getName());
    executorService.execute(new MessageSender(message, template));
  }
  
  @Override
  public void sendSMS(final SmsMessage message, final SmsTemplateVO template,
      final boolean isDNDActive) {
  
    LOG.info("Scheduling Message to-{} type-{}", message.getMobile(),
        template.getName());
    executorService.execute(new SmsSender(message, template, isDNDActive));
  }
  
  private final class MessageSender implements Runnable {
    
    private final EmailMessage message;
    
    private final EmailTemplateVO template;
    
    public MessageSender(final EmailMessage message,
        final EmailTemplateVO template) {
    
      this.message = message;
      this.template = template;
    }
    
    @Override
    public void run() {
    
      try {
        final IEmailSender mailSender =
            CacheManager.getInstance().getCache(EmailChannelCache.class)
                .getEmailSenderByChannelId(template.getEmailChannelId());
        mailSender.send(message, template);
        LOG.info("Successfully sent email message type - {}",
            message.getTemplateName());
      } catch (final Throwable e) {
        LOG.info("Failed to send email message type - {}",
            message.getTemplateName());
        LOG.error("exception sending email message", e);
      }
    }
  }
  
  private final class SmsSender implements Runnable {
    
    private final SmsMessage message;
    
    private final SmsTemplateVO template;
    
    private final boolean isDNDActive;
    
    public SmsSender(final SmsMessage message, final SmsTemplateVO template,
        final boolean isDNDActive) {
    
      this.message = message;
      this.template = template;
      this.isDNDActive = isDNDActive;
    }
    
    @Override
    public void run() {
    
      try {
        LOG.info("Sending Message to-{} type-{}", message.getMobile(),
            message.getTemplateName());
        if (template.isDndScrubbingOn() && isDNDActive) {
          LOG.info("DND Check : Blocking Message to-{} type-{}",
              message.getMobile(), message.getTemplateName());
        } else {
          final ISmsSender smsSender =
              CacheManager.getInstance().getCache(SmsChannelCache.class)
                  .getSmsSenderByChannelId(template.getSmsChannelId());
          final String templateMessage =
              template.getBodyTemplate().evaluate(message.getTemplateParams());
          smsSender.send(message.getMobile(), templateMessage);
          LOG.info("Successfully Sent Message to-{} type-{}",
              message.getMobile(), message.getTemplateName());
        }
      } catch (final Throwable e) {
        LOG.error("exception while sending sms", e);
      }
    }
  }
  
  @Override
  public void loadEmailChannelConfiguration() {
  
    try {
      baseStartupService.loadEmailChannels();
    } catch (final InstantiationException e) {
      LOG.error("InstantiationException loading Email Channel", e);
    } catch (final IllegalAccessException e) {
      LOG.error("IllegalAccessException loading Email Channel", e);
    } catch (final ClassNotFoundException e) {
      LOG.error("ClassNotFoundException loading Email Channel", e);
    }
  }
  
  @Override
  public void loadSmsChannelConfiguration() {
  
    baseStartupService.loadSmsChannels();
  }
  
  @Override
  public void loadEmailDomainConfiguration() {
  
    baseStartupService.loadEmailDomains();
  }
  
  @Override
  public List<EmailChannel> getAllEmailChannels() {
  
    return CacheManager.getInstance().getCache(EmailChannelCache.class)
        .getEmailChannels();
  }
  
  @Override
  public ExecutorService getExecutorService() {
  
    return executorService;
  }
  
  @Override
  public void destroy() throws Exception {
  
    LOG.info("Shutting down notification service's executor service..");
    executorService.shutdown();
  }
  
  @Override
  public List<SmsChannel> getAllSmsChannels() {
  
    return CacheManager.getInstance().getCache(SmsChannelCache.class)
        .getSmsChannels();
  }
  
}
