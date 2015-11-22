/**
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.prchny.base.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import com.prchny.base.aspect.PrchnyServiceRequestContextHolder;
import com.prchny.base.utils.Constants;
import com.prchny.base.utils.StringUtils;

/**
 * @version 1.0, 31-Jul-2014
 * @author naveen
 */
public class LogbackRequestPatternConvertor extends ClassicConverter {
  
  @Override
  public String convert(final ILoggingEvent event) {
  
    String logString =
        event.getMDCPropertyMap().get(
            LoggingConstants.REQUEST_CONTEXT_LOG_STRING);
    if (StringUtils.isEmpty(logString)) {
      // This will fetch data for sync appenders
      
      logString = PrchnyServiceRequestContextHolder.getLogString();
      if (StringUtils.isEmpty(logString)) {
        if (event.getMDCPropertyMap().get(Constants.REQUEST_USER_TRACKING_KEY) != null) {
          return "[requestId="
              + event.getMDCPropertyMap()
                  .get(Constants.REQUEST_USER_TRACKING_KEY).toString() + "]";
        }
        return "";
      }
    }
    return logString;
  }
  
}
