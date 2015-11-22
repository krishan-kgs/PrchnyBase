/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Oct 7, 2010
 *  @author singla
 */

package com.prchny.base.vo;

import com.prchny.base.velocity.Template;

public class SmsTemplateVO {
  
  private String name;
  
  private Template bodyTemplate;
  
  private int smsChannelId;
  
  private boolean dndScrubbingOn;
  
  public String getName() {
  
    return name;
  }
  
  public void setName(final String name) {
  
    this.name = name;
  }
  
  public Template getBodyTemplate() {
  
    return bodyTemplate;
  }
  
  public void setBodyTemplate(final Template bodyTemplate) {
  
    this.bodyTemplate = bodyTemplate;
  }
  
  public void setSmsChannelId(final int smsChannelId) {
  
    this.smsChannelId = smsChannelId;
  }
  
  public int getSmsChannelId() {
  
    return smsChannelId;
  }
  
  public boolean isDndScrubbingOn() {
  
    return dndScrubbingOn;
  }
  
  public void setDndScrubbingOn(final boolean dndScrubbingOn) {
  
    this.dndScrubbingOn = dndScrubbingOn;
  }
  
}
