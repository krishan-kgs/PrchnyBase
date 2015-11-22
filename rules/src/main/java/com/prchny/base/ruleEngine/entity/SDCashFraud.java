/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 22-Oct-2012
 *  @author sunny
 */

package com.prchny.base.ruleEngine.entity;

import com.prchny.base.ruleEngine.IFact;

public class SDCashFraud implements IFact {
  
  private int sdCashValue;
  
  private boolean isFraud;
  
  public int getSdCashValue() {
  
    return sdCashValue;
  }
  
  public void setSdCashValue(final int sdCashValue) {
  
    this.sdCashValue = sdCashValue;
  }
  
  public boolean isFraud() {
  
    return isFraud;
  }
  
  public void setFraud(final boolean isFraud) {
  
    this.isFraud = isFraud;
  }
}
