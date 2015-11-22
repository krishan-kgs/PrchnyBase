/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Apr 22, 2011
 *  @author singla
 */

package com.prchny.base.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {
  
  /**
   * @param <T>
   * @param c
   * @return
   */
  public static <T> List<T> asList(final Collection<T> c) {
  
    if (c instanceof List) {
      return (List<T>) c;
    } else {
      final List<T> list = new ArrayList<T>();
      list.addAll(c);
      return list;
    }
  }
  
  public static <T> boolean isEmpty(final Collection<T> c) {
  
    if (c == null) {
      return true;
    }
    if (c.size() == 0) {
      return true;
    }
    return false;
  }
  
  public static List<Integer> asIntegerList(final String str, final String regex) {
  
    final List<Integer> returnList = new ArrayList<Integer>();
    if (StringUtils.isEmpty(str) || StringUtils.isEmpty(regex)) {
      return null;
    }
    final List<String> intIds = StringUtils.split(str, regex);
    for (final String id : intIds) {
      returnList.add(Integer.valueOf(id));
    }
    return returnList;
  }
  
  public static List<Integer> asIntegerList(final String str) {
  
    if (StringUtils.isEmpty(str)) {
      return null;
    }
    return asIntegerList(str, ",");
  }
}
