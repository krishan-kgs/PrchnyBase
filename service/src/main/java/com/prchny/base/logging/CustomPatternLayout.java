/*
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 17-Jul-2014
 *  @author ajinkya
 */

package com.prchny.base.logging;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternParser;

/**
 * This class has originally been written to log requestContext details to logs.<br>
 * Reference : http://fw-geekycoder.blogspot.fr/2010/07/creating-log4j-custom-
 * patternlayout.html
 * 
 * @author ajinkya
 */
public class CustomPatternLayout extends PatternLayout {
  
  public CustomPatternLayout() {
  
    super();
  }
  
  public CustomPatternLayout(final String pattern) {
  
    super(pattern);
  }
  
  @Override
  protected PatternParser createPatternParser(final String pattern) {
  
    return new CustomPatternParser(pattern);
  }
  
}
