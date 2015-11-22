/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Oct 19, 2011
 *  @author jitesh
 */

package com.prchny.base.utils;

import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BannedZipParser {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(BannedZipParser.class);
  
  public static boolean matches(String pattern, final String zip) {
  
    if (StringUtils.isEmpty(zip) || StringUtils.isEmpty(pattern)) {
      return false;
    }
    
    boolean match = false;
    if (!(pattern.indexOf("!") < 0)) {
      pattern = pattern.substring(pattern.indexOf("!") + 1);
      match = true;
    }
    
    final StringTokenizer st = new StringTokenizer(pattern, ",");
    while (st.hasMoreTokens()) {
      String rule = st.nextToken().trim();
      try {
        
        rule = rule.replace("*", "[0-9]*");
        final Pattern p = Pattern.compile(rule);
        if (p.matcher(zip).matches()) {
          if (match) {
            return false;
          } else {
            return true;
          }
          
        }
      } catch (final PatternSyntaxException e) {
        LOG.warn("Pattern not compilable " + rule, e);
        // Since this is not valid pattern we ignore it
        
      }
    }
    return match;
  }
}
