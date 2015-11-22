/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Jul 6, 2011
 *  @author rahul
 */

package com.prchny.base.utils;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

public class HibernateUtils {
  
  @SuppressWarnings("unchecked")
  public static <T> T initializeAndUnproxy(T entity) {
  
    if (entity == null) {
      throw new NullPointerException("Entity passed for initialization is null");
    }
    
    Hibernate.initialize(entity);
    if (entity instanceof HibernateProxy) {
      entity =
          (T) ((HibernateProxy) entity).getHibernateLazyInitializer()
              .getImplementation();
    }
    return entity;
  }
  
}
