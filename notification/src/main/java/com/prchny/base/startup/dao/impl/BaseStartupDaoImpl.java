/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Apr 27, 2012
 *  @author amit
 */

package com.prchny.base.startup.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.prchny.base.entity.EmailChannel;
import com.prchny.base.entity.EmailDomain;
import com.prchny.base.entity.SmsChannel;
import com.prchny.base.startup.dao.IBaseStartupDao;

@Repository("baseStartupDao")
@SuppressWarnings("unchecked")
public class BaseStartupDaoImpl implements IBaseStartupDao {
  
  @Autowired
  @Qualifier("base")
  private SessionFactory baseSessionFactory;
  
  @Override
  public List<EmailChannel> getEmailChannels() {
  
    final Query query =
        baseSessionFactory.getCurrentSession().createQuery(
            "from EmailChannel where enabled = 1");
    return query.list();
  }
  
  @Override
  public List<SmsChannel> getSmsChannels() {
  
    final Query query =
        baseSessionFactory.getCurrentSession().createQuery("from SmsChannel");
    return query.list();
  }
  
  @Override
  public List<EmailDomain> getEmailDomains() {
  
    return baseSessionFactory.getCurrentSession()
        .createQuery("from EmailDomain").list();
  }
  
}
