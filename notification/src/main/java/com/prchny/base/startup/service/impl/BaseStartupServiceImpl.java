/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Apr 20, 2012
 *  @author amit
 */

package com.prchny.base.startup.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prchny.base.cache.CacheManager;
import com.prchny.base.cache.EmailChannelCache;
import com.prchny.base.cache.EmailDomainsCache;
import com.prchny.base.cache.SmsChannelCache;
import com.prchny.base.entity.EmailChannel;
import com.prchny.base.entity.EmailDomain;
import com.prchny.base.entity.SmsChannel;
import com.prchny.base.startup.dao.IBaseStartupDao;
import com.prchny.base.startup.service.IBaseStartupService;

@Service("baseStartupService")
@Transactional
public class BaseStartupServiceImpl implements IBaseStartupService {
  
  @Autowired
  IBaseStartupDao startupDao;
  
  @Override
  public void loadEmailChannels() throws InstantiationException,
      IllegalAccessException, ClassNotFoundException {
  
    final List<EmailChannel> channels = startupDao.getEmailChannels();
    final EmailChannelCache emailChannelCache = new EmailChannelCache();
    for (final EmailChannel channel : channels) {
      emailChannelCache.addChannel(channel);
    }
    CacheManager.getInstance().setCache(emailChannelCache);
  }
  
  @Override
  public void loadSmsChannels() {
  
    final List<SmsChannel> channels = startupDao.getSmsChannels();
    final SmsChannelCache smsChannelCache = new SmsChannelCache();
    for (final SmsChannel channel : channels) {
      smsChannelCache.addChannel(channel);
    }
    CacheManager.getInstance().setCache(smsChannelCache);
  }
  
  @Override
  public void loadEmailDomains() {
  
    final List<EmailDomain> emailDomains = startupDao.getEmailDomains();
    final EmailDomainsCache cache = new EmailDomainsCache();
    for (final EmailDomain emailDomain : emailDomains) {
      cache.addEmailDomain(emailDomain);
    }
    CacheManager.getInstance().setCache(cache);
  }
}
