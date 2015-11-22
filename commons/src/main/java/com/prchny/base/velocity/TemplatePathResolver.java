/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, May 29, 2013
 *  @author ghanshyam
 */

package com.prchny.base.velocity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplatePathResolver {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(TemplatePathResolver.class);
  
  private static final TemplatePathResolver templatePathResolver =
      new TemplatePathResolver();
  
  /**
   * Added to get static path for current deployment
   */
  private static int noOfCdn = 4;
  
  private TemplatePathResolver() {
  
  }
  
  private static String getStaticPath(final String path) {
  
    if ("prod".equals(System.getProperty("env"))) {
      final StringBuilder sb = new StringBuilder();
      if (path.startsWith("imgs/a/")) {
        sb.append("http://n" + ((Math.abs(path.hashCode()) % noOfCdn) + 1)
            + ".sdlcdn.com/");
      } else {
        sb.append("http://i" + ((Math.abs(path.hashCode()) % noOfCdn) + 1)
            + ".sdlcdn.com/");
      }
      sb.append(path);
      return sb.toString();
    } else if ("staging".equals(System.getProperty("env"))) {
      final StringBuilder sb = new StringBuilder();
      if (path.startsWith("imgs/a/")) {
        sb.append("http://stg" + ((Math.abs(path.hashCode()) % noOfCdn) + 1)
            + ".sdlcdn.com/");
      } else {
        sb.append("http://static.prchny.com:8081/");
      }
      sb.append(path);
      return sb.toString();
    } else {
      if (System.getProperty("static.resource.url") == null) {
        throw new NullPointerException("missing property static.resource.url");
      }
      return System.getProperty("static.resource.url") + "/" + path;
      
    }
  }
  
  public String resources(final String initialPath) {
  
    return getStaticPath(initialPath);
  }
  
  public static TemplatePathResolver getTemplatePathResolver() {
  
    return templatePathResolver;
  }
}
