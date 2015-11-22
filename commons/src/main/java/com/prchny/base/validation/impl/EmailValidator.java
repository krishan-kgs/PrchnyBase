/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 29-Dec-2012
 *  @author amd
 */

package com.prchny.base.validation.impl;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.prchny.base.annotations.Email;
import com.prchny.base.validation.ValidationFactory;
import com.prchny.base.validation.Validator;

@Component
public class EmailValidator implements Validator {
  
  private static final String ATOM =
      "[^\\x00-\\x1F^\\(^\\)^\\<^\\>^\\@^\\,^\\;^\\:^\\\\^\\\"^\\.^\\[^\\]^\\s]";
  
  private static final String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)+";
  
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^" + ATOM
      + "+(\\." + ATOM + "+)*@" + DOMAIN + ")$",
      java.util.regex.Pattern.CASE_INSENSITIVE);
  
  @PostConstruct
  @Override
  public void registerValidator() {
  
    ValidationFactory.addValidator(Email.class, this);
  }
  
  @Override
  public boolean isValid(final Field f, final Object obj)
      throws IllegalArgumentException, IllegalAccessException {
  
    final Object thisObj = f.get(obj);
    return thisObj != null ? isEmailPatternValid(thisObj.toString()) : true;
  }
  
  public boolean isEmailPatternValid(final String value) {
  
    if (StringUtils.isNotEmpty(value)) {
      return EMAIL_PATTERN.matcher(value).matches();
    }
    return false;
  }
  
  @Override
  public String getName() {
  
    return "EmailValidator";
  }
  
}
