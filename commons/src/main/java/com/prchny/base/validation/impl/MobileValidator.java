/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 29-Dec-2012
 *  @author amd
 */

package com.prchny.base.validation.impl;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.prchny.base.annotations.Mobile;
import com.prchny.base.validation.ValidationFactory;
import com.prchny.base.validation.Validator;

@Component
public class MobileValidator implements Validator {
  
  private static final Pattern MOBILE_PATTERN = Pattern
      .compile("^\\+?(91|0)?-?([7-9][0-9]{9})$");
  
  @PostConstruct
  @Override
  public void registerValidator() {
  
    ValidationFactory.addValidator(Mobile.class, this);
  }
  
  @Override
  public boolean isValid(final Field f, final Object obj)
      throws IllegalArgumentException, IllegalAccessException {
  
    final Object thisObj = f.get(obj);
    return thisObj != null ? getValidMobileOrNull(thisObj
        .toString()) != null : true;
  }
  
  public static String getValidMobileOrNull(final String value) {
  
    if (value == null) {
      return null;
    }
    final Matcher m = MOBILE_PATTERN.matcher(value);
    if (m.matches()) {
      return m.group(2);
    } else {
      return null;
    }
  }
  
  @Override
  public String getName() {
  
    return "MobileValidator";
  }
  
}
