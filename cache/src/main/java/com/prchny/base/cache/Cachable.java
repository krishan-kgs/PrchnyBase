package com.prchny.base.cache;

import java.io.Serializable;

// Marker Interface for Identifying entities that are cachable
public interface Cachable extends Serializable {
  
  String toMemString();
  
  Cachable loadFromMemString(String str);
}
