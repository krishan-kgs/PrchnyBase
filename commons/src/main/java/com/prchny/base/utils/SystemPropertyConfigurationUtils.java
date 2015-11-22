/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 25-Oct-2012
 *  @author shishir
 */

package com.prchny.base.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prchny.base.exception.InvalidConfigurationException;
import com.prchny.base.exception.InvalidFormatException;
import com.prchny.base.utils.XMLParser.Element;

public class SystemPropertyConfigurationUtils {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(SystemPropertyConfigurationUtils.class);
  
  private final Map<String, Object> systemPropertyMap =
      new ConcurrentHashMap<String, Object>();
  
  @SuppressWarnings("unused")
  public static Map<String, Object> getMaps(final String fileNameToParse)
      throws InvalidFormatException, InvalidConfigurationException,
      IllegalArgumentException {
  
    final SystemPropertyConfigurationUtils spcu =
        new SystemPropertyConfigurationUtils();
    final File file = new File(fileNameToParse);
    XMLParser parser = null;
    try {
      parser = new XMLParser(file);
    } catch (final IllegalArgumentException e) {
      LOG.error("Couldn't find the file : {}.", file);
      throw e;
    }
    if (parser != null) {
      final Element root = parser.parse();
      for (final Element property : root.list("property")) {
        final String name = property.attribute("name");
        final String value = property.attribute("value");
        
        if (StringUtils.isEmpty(name)
            || spcu.systemPropertyMap.containsKey(name)) {
          throw new InvalidFormatException(fileNameToParse);
        } else if (value != null) {
          spcu.putScalar(name.toLowerCase(), value);
        } else {
          if (property.get("values") != null) {
            if (StringUtils.isEmpty(property.get("values").attribute("key")))// list
            {
              spcu.putList(name.toLowerCase(), property);
            } else { // map
              spcu.putMap(name.toLowerCase(), property);
            }
          } else {
            LOG.error("Error in config file. Please check.");
          }
        }
      }
      return new ConcurrentHashMap<String, Object>(spcu.systemPropertyMap);
    }
    return new ConcurrentHashMap<String, Object>();
  }
  
  private void putMap(final String propertyName, final Element property)
      throws InvalidConfigurationException {
  
    final Map<String, String> tempMap = new ConcurrentHashMap<String, String>();
    if (StringUtils.isEmpty(property.attribute("caseInsensitiveKey"))
        || "false".equalsIgnoreCase(property.attribute("caseInsensitiveKey"))) {
      for (final Element mapElement : property.list("values")) {
        tempMap.put(mapElement.attribute("key"), mapElement.attribute("value"));
      }
    } else if ("true"
        .equalsIgnoreCase(property.attribute("caseInsensitiveKey"))) {
      for (final Element mapElement : property.list("values")) {
        tempMap.put(mapElement.attribute("key").toLowerCase(),
            mapElement.attribute("value"));
      }
    } else {
      throw new InvalidConfigurationException(
          "case insensitive attribute can either be true or false or not present at all(default false)");
    }
    systemPropertyMap.put(propertyName, tempMap);
  }
  
  private void putList(final String propertyName, final Element property) {
  
    final List<String> tempList = new ArrayList<String>();
    for (final Element listElement : property.list("values")) {
      tempList.add(listElement.attribute("value"));
    }
    systemPropertyMap.put(propertyName, tempList);
  }
  
  private void putScalar(final String propertyName, final String value) {
  
    systemPropertyMap.put(propertyName, value);
  }
  
  public static void main(final String[] args) {
  
    try {
      final Map<String, Object> testMap =
          getMaps("/opt/configuration/sysconf.xml");
      System.out.println(testMap.get("testProp"));
    } catch (final IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final InvalidFormatException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final InvalidConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
