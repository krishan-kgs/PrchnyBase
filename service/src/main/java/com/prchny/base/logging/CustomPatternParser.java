/*
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 17-Jul-2014
 *  @author ajinkya
 */

package com.prchny.base.logging;

import org.apache.log4j.helpers.PatternParser;

/**
 * This class defines the placeholders for custom log strings.<br>
 * More may be added to the switch case if needed
 * 
 * @author ajinkya
 */
public class CustomPatternParser extends PatternParser {
  
  private static final char REQUEST_CONTEXT = 'R';
  
  public CustomPatternParser(final String pattern) {
  
    super(pattern);
  }
  
  @Override
  protected void finalizeConverter(final char c) {
  
    switch (c) {
      case REQUEST_CONTEXT:
        currentLiteral.setLength(0);
        addConverter(new RequestContextPatternConverter());
        break;
      default:
        super.finalizeConverter(c);
        break;
    }
  }
  
}
