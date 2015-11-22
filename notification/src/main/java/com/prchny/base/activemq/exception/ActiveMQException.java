/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 19-Jun-2012
 *  @author sunny
 */

package com.prchny.base.activemq.exception;

public class ActiveMQException extends Exception {
  
  private static final long serialVersionUID = 1312324342344L;
  
  public ActiveMQException(final String message) {
  
    super(message);
  }
}
