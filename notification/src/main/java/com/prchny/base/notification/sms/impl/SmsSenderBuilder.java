/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 21, 2010
 *  @author rahul
 */

package com.prchny.base.notification.sms.impl;

import com.prchny.base.notification.sms.ISmsSender;

public class SmsSenderBuilder {
  
  public static ISmsSender buildSender(final String senderClassName) {
  
    try {
      return (ISmsSender) Class.forName(senderClassName).newInstance();
    } catch (final ClassNotFoundException e) {
      throw new RuntimeException(e);
    } catch (final InstantiationException e) {
      throw new RuntimeException(e);
    } catch (final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
