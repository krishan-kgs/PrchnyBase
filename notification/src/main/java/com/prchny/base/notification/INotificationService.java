/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Apr 20, 2012
 *  @author amit
 */

package com.prchny.base.notification;

import java.util.List;
import java.util.concurrent.ExecutorService;

import com.prchny.base.entity.EmailChannel;
import com.prchny.base.entity.SmsChannel;
import com.prchny.base.notification.email.EmailMessage;
import com.prchny.base.notification.sms.SmsMessage;
import com.prchny.base.vo.EmailTemplateVO;
import com.prchny.base.vo.SmsTemplateVO;

public interface INotificationService {
  
  public void sendEmail(EmailMessage message, EmailTemplateVO template);
  
  public void sendSMS(SmsMessage message, SmsTemplateVO template,
      boolean isDNDActive);
  
  public void loadEmailChannelConfiguration();
  
  public void loadSmsChannelConfiguration();
  
  public void loadEmailDomainConfiguration();
  
  public List<EmailChannel> getAllEmailChannels();
  
  public List<SmsChannel> getAllSmsChannels();
  
  ExecutorService getExecutorService();
}
