/**
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.prchny.base.logging;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;

import com.prchny.base.aspect.PrchnyServiceRequestContextHolder;
import com.prchny.base.utils.StringUtils;

/**
 * 
 * @version 1.0, 31-Jul-2014
 * @author naveen
 */
public class LogbackModifiedAsyncAppender extends AsyncAppender {
  
  public LogbackModifiedAsyncAppender() {
  
    super();
  }
  
  @Override
  protected void preprocess(final ILoggingEvent event) {
  
    // Copying request context over thread boundary so that it can be logged
    // asyncly
    final String requestContextLogString =
        PrchnyServiceRequestContextHolder.getLogString();
    
    if (StringUtils.isNotEmpty(requestContextLogString)) {
      event.getMDCPropertyMap().put(
          LoggingConstants.REQUEST_CONTEXT_LOG_STRING, requestContextLogString);
    }
    super.preprocess(event);
  }
}
