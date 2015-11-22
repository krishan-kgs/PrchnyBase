
package com.prchny.base.ext;

import java.util.HashMap;
import java.util.Map;

public enum ShippingGroupType {
  PRODUCT_NONCOD(
      "STD"),
  PRODUCT_COD(
      "COD-PRODUCTS"),
  VOUCHER_COD(
      "COD-VOUCHERS"),
  VOUCHER_NONCOD(
      "EMAIL-MOBILE");
  
  private String code;
  
  private static Map<String, ShippingGroupType> cache =
      new HashMap<String, ShippingGroupType>();
  
  static {
    for (final ShippingGroupType type : ShippingGroupType.values()) {
      cache.put(type.code(), type);
    }
  }
  
  private ShippingGroupType(final String code) {
  
    this.code = code;
  }
  
  public String code() {
  
    return code;
  }
  
  public static ShippingGroupType getTypeByCode(final String code) {
  
    return cache.get(code);
  }
}
