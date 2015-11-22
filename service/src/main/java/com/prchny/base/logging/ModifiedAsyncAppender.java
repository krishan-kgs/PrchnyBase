/*
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 17-Jul-2014
 *  @author ajinkya
 */

package com.prchny.base.logging;

import org.apache.log4j.AsyncAppender;
import org.apache.log4j.spi.LoggingEvent;

import com.prchny.base.aspect.PrchnyServiceRequestContextHolder;
import com.prchny.base.utils.StringUtils;

public class ModifiedAsyncAppender extends AsyncAppender {
  
  public ModifiedAsyncAppender() {
  
    super();
  }
  
  @Override
  public void append(final LoggingEvent event) {
  
    // Copying request context over thread boundary so that it can be logged
    // asyncly
    final String requestContextLogString =
        PrchnyServiceRequestContextHolder.getLogString();
    
    // This is done because Logging event's map is backed by a hashtable which
    // does not permit null keys or null values
    if (StringUtils.isNotEmpty(requestContextLogString)) {
      event.setProperty(LoggingConstants.REQUEST_CONTEXT_LOG_STRING,
          requestContextLogString);
    }
    
    super.append(event);
  }
  
}
