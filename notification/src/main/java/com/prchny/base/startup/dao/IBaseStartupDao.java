/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Apr 27, 2012
 *  @author amit
 */

package com.prchny.base.startup.dao;

import java.util.List;

import com.prchny.base.entity.EmailChannel;
import com.prchny.base.entity.EmailDomain;
import com.prchny.base.entity.SmsChannel;

public interface IBaseStartupDao {
  
  public List<EmailChannel> getEmailChannels();
  
  public List<SmsChannel> getSmsChannels();
  
  public List<EmailDomain> getEmailDomains();
  
}
