/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Jan 4, 2012
 *  @author amit
 */

package com.prchny.base.event.impl;

import java.util.HashMap;
import java.util.Map;

public class EventResponse {
  
  private final Map<String, Object> response = new HashMap<String, Object>();
  
  public Map<String, Object> getResponse() {
  
    return response;
  }
}
