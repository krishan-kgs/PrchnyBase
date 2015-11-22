
package com.prchny.base.model.common;

public enum CatalogType {
  DEAL(
      "deal"),
  PRODUCT(
      "product"),
  GETAWAY(
      "getaway");
  
  private final String type;
  
  private CatalogType(final String type) {
  
    this.type = type;
  }
  
  public String type() {
  
    return type;
  }
  
}
