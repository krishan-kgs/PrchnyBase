/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Mar 15, 2011
 *  @author rahul
 */

package com.prchny.base.notification.email;

import com.prchny.base.entity.EmailChannel;
import com.prchny.base.vo.EmailTemplateVO;

public interface IEmailSender {
  
  public void send(EmailMessage message, EmailTemplateVO template);
  
  public void initialize(EmailChannel channel);
  
}
