
package com.prchny.base.utils.productbulk;

import java.util.Random;

public class ProductBulkUtilColumn {
  
  private final String name;
  
  private final String headerValue;
  
  private final boolean containsIntValue;
  
  private String[] possibleStringItems;
  
  private int[] possibleIntItems;
  
  public ProductBulkUtilColumn(final String n, final String v, final boolean civ) {
  
    name = n;
    headerValue = v;
    
    containsIntValue = civ;
  }
  
  public ProductBulkUtilColumn(final String n, final String v,
      final String... pd) {
  
    name = n;
    headerValue = v;
    possibleStringItems = pd;
    
    containsIntValue = false;
  }
  
  public ProductBulkUtilColumn(final String n, final String v, final int... pd) {
  
    name = n;
    headerValue = v;
    possibleIntItems = pd;
    
    containsIntValue = true;
  }
  
  public String getName() {
  
    return name;
  }
  
  public String getHeaderValue() {
  
    return headerValue;
  }
  
  public boolean containsIntValue() {
  
    return containsIntValue;
  }
  
  public String getNextStringItemFromCount(final int count) {
  
    if (name.equals("COL_SKU") || name.equals("VENDOR_SKU")) {
      return "testbulkklm" + count;
    }
    if (name.equals("COL_VENDOR_CODE") || name.equals("VENDOR_CODE")) {
      final int rem = (count - 1) % possibleStringItems.length;
      return possibleStringItems[rem];
    }
    
    final Random random = new Random();
    return possibleStringItems[random.nextInt(possibleStringItems.length)];
  }
  
  public int getNextIntItemFromCount(final int count) {
  
    if (name.equals("COL_OFFER")) {
      return count;
    }
    
    final Random random = new Random();
    return possibleIntItems[random.nextInt(possibleIntItems.length)];
  }
}
