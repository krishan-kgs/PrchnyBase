/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 29-May-2012
 *  @author amd
 */

package com.prchny.base.ext;

public enum ShippingMethodType {
  STANDARD(
      "STD"),
  CASH_ON_DELIVERY(
      "COD"),
  ELECTRONIC(
      "EMAIL");
  
  private String code;
  
  private ShippingMethodType(final String code) {
  
    this.code = code;
  }
  
  public String code() {
  
    return code;
  }
}
