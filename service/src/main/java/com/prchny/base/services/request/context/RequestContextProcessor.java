/*
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 18-Jul-2014
 *  @author ajinkya
 */

package com.prchny.base.services.request.context;

/**
 * This is used for post processing request context in the aspect.
 * 
 * @author ajinkya
 */
public interface RequestContextProcessor {
  
  public String process(Object[] args);
  
}
