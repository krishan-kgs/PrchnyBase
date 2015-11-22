/*
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 17-Jul-2014
 *  @author ajinkya
 */

package com.prchny.base.logging;

import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

import com.prchny.base.aspect.PrchnyServiceRequestContextHolder;
import com.prchny.base.utils.Constants;
import com.prchny.base.utils.StringUtils;

/**
 *
 use log4j configuration instead
 */
public class RequestContextPatternConverter extends PatternConverter {
  
  @Override
  protected String convert(final LoggingEvent event) {
  
    // This will fetch data for async appenders
    String logString =
        event.getProperty(LoggingConstants.REQUEST_CONTEXT_LOG_STRING);
    if (StringUtils.isEmpty(logString)) {
      // This will fetch data for sync appenders
      logString = PrchnyServiceRequestContextHolder.getLogString();
      if (StringUtils.isEmpty(logString)) {
        if (event.getMDC(Constants.REQUEST_USER_TRACKING_KEY) != null) {
          return "[requestId="
              + event.getMDC(Constants.REQUEST_USER_TRACKING_KEY).toString()
              + "]";
        }
      }
    }
    return logString;
  }
  
}
