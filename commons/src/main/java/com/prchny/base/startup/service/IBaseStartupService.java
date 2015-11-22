/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Apr 20, 2012
 *  @author amit
 */

package com.prchny.base.startup.service;

public interface IBaseStartupService {
  
  public void loadEmailChannels() throws InstantiationException,
      IllegalAccessException, ClassNotFoundException;
  
  public void loadSmsChannels();
  
  public void loadEmailDomains();
  
}
