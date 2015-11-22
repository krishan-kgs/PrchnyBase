/*
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 10-Jul-2014
 *  @author ajinkya
 */

package com.prchny.base.startup.config;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.prchny.base.utils.ApplicationPropertyUtils;
import com.prchny.base.utils.StringUtils;

@Component
public class AppEnvironmentContext {
  
  private String componentName;
  
  private String processIdent;
  
  private String appIdent;
  
  private String serverIP;
  
  private static AppEnvironmentContext instance;
  
  public AppEnvironmentContext() {
  
    super();
  }
  
  public AppEnvironmentContext(final String componentName) {
  
    super();
    this.componentName = componentName;
  }
  
  @PostConstruct
  public void init() throws UnknownHostException {
  
    if (StringUtils.isEmpty(componentName)) {
      throw new IllegalStateException(
          "Unable to startup application without identification. Please refer to http://techspace.prchny.in/display/EN/Upgrade+instructions for details on how to fix this error !");
    }
    
    setProcessIdent(System.getProperty("processIdent"));
    
    StringBuilder br = new StringBuilder().append(getComponentName());
    if (getProcessIdent() != null) {
      br = br.append(":").append(getProcessIdent());
    }
    setAppIdent(br.toString());
    
    setServerIP(ApplicationPropertyUtils.getLocalHostLANAddress()
        .getHostAddress());
    
    instance = this;
  }
  
  public static String getComponent() {
  
    if (instance == null) {
      throw new IllegalStateException(
          "AppEnvironmentContext has not be initialized !!");
    }
    return instance.getComponentName();
  }
  
  public static String getProcessIdentifier() {
  
    if (instance == null) {
      throw new IllegalStateException(
          "AppEnvironmentContext has not be initialized !!");
    }
    return instance.getProcessIdent();
  }
  
  /**
   * This is equal to component name + process identifier
   * 
   * @return
   */
  public static String getAppIdentifier() {
  
    if (instance == null) {
      throw new IllegalStateException(
          "AppEnvironmentContext has not be initialized !!");
    }
    return instance.getAppIdent();
  }
  
  public static String getServerIPAddr() {
  
    if (instance == null) {
      throw new IllegalStateException(
          "AppEnvironmentContext has not be initialized !!");
    }
    return instance.getServerIP();
  }
  
  private String getComponentName() {
  
    return componentName;
  }
  
  @SuppressWarnings("unused")
  private void setComponentName(final String componentName) {
  
    this.componentName = componentName;
  }
  
  private String getProcessIdent() {
  
    return processIdent;
  }
  
  private void setProcessIdent(final String processIdent) {
  
    this.processIdent = processIdent;
  }
  
  private String getAppIdent() {
  
    return appIdent;
  }
  
  private void setAppIdent(final String appIdent) {
  
    this.appIdent = appIdent;
  }
  
  private String getServerIP() {
  
    return serverIP;
  }
  
  private void setServerIP(final String serverIP) {
  
    this.serverIP = serverIP;
  }
  
  @Override
  public String toString() {
  
    return "AppEnvironmentContext [componentName=" + componentName
        + ", processIdent=" + processIdent + ", serverIP=" + serverIP + "]";
  }
  
}
