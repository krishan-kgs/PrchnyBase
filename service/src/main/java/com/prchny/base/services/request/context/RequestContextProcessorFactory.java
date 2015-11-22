/*
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 18-Jul-2014
 *  @author ajinkya
 */

package com.prchny.base.services.request.context;

import org.springframework.stereotype.Component;

@Component("requestContextProcessorFactory")
public class RequestContextProcessorFactory {
  
  private final RequestContextProcessor contextProcessor;
  
  public RequestContextProcessorFactory() {
  
    super();
    contextProcessor = null;
  }
  
  public RequestContextProcessorFactory(
      final RequestContextProcessor contextProcessor) {
  
    super();
    this.contextProcessor = contextProcessor;
  }
  
  public RequestContextProcessor getProcessor() {
  
    return contextProcessor;
  }
  
}
