package com.prchny.base.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.prchny.base.annotations.Cache;
import com.prchny.base.exception.InvalidConfigurationException;

@Cache(name = "systemFileProperties")
@SuppressWarnings("unchecked")
public class SystemFilePropertiesCache {
  
  private Map<String, Object> systemPropertyMap =
      new ConcurrentHashMap<String, Object>();
  
  public SystemFilePropertiesCache() {
  
  }
  
  public SystemFilePropertiesCache(final Map<String, Object> maps) {
  
    systemPropertyMap = maps;
  }
  
  public String getScalar(String property) throws InvalidConfigurationException {
  
    property = property.toLowerCase();
    if (systemPropertyMap.containsKey(property)) {
      if (!(systemPropertyMap.get(property) instanceof Collection<?>)) {
        return (String) systemPropertyMap.get(property);
      } else {
        throw new InvalidConfigurationException(
            "The property passed is a collection");
      }
    }
    return null;
  }
  
  public String getMapValue(String property, final String key)
      throws InvalidConfigurationException {
  
    property = property.toLowerCase();
    if (systemPropertyMap.containsKey(property)) {
      if (systemPropertyMap.get(property) instanceof Map<?, ?>) {
        final Map<String, String> tempMap =
            (Map<String, String>) systemPropertyMap.get(property);
        return tempMap.get(key);
      } else {
        throw new InvalidConfigurationException(
            "The property passed is not a Map");
      }
    }
    return null;
  }
  
  public List<String> getList(String property)
      throws InvalidConfigurationException {
  
    property = property.toLowerCase();
    if (systemPropertyMap.containsKey(property)
        && (systemPropertyMap.get(property) != null)) {
      if (systemPropertyMap.get(property) instanceof List<?>) {
        final List<String> list =
            (List<String>) systemPropertyMap.get(property);
        return new ArrayList<String>(list);
      } else {
        throw new InvalidConfigurationException(
            "The property Called is not a List");
      }
    }
    return null;
  }
  
  public Map<String, String> getMap(String property)
      throws InvalidConfigurationException {
  
    property = property.toLowerCase();
    if (systemPropertyMap.containsKey(property)) {
      if (systemPropertyMap.get(property) instanceof Map<?, ?>) {
        final Map<String, String> tempMap =
            (Map<String, String>) systemPropertyMap.get(property);
        return new ConcurrentHashMap<String, String>(tempMap);
      } else {
        throw new InvalidConfigurationException(
            "The property Called is not a Map");
      }
    }
    return null;
  }
}
