/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 29-Dec-2012
 *  @author amd
 */

package com.prchny.base.validation.impl;

import java.lang.reflect.Field;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.prchny.base.annotations.Signed;
import com.prchny.base.annotations.Signed.Sign;
import com.prchny.base.validation.ValidationFactory;
import com.prchny.base.validation.Validator;

@Component
public class SignedValidator implements Validator {
  
  @PostConstruct
  @Override
  public void registerValidator() {
  
    ValidationFactory.addValidator(Signed.class, this);
  }
  
  @Override
  public boolean isValid(final Field f, final Object obj)
      throws IllegalArgumentException, IllegalAccessException {
  
    final Object thisObj = f.get(obj);
    boolean valid = false;
    if (thisObj == null) {
      return true;
    }
    if (f.getAnnotation(Signed.class).value() == Sign.POSITIVE) {
      valid = Integer.parseInt(thisObj.toString()) >= 0 ? true : false;
    } else if (f.getAnnotation(Signed.class).value() == Sign.NEGATIVE) {
      valid = Integer.parseInt(thisObj.toString()) <= 0 ? true : false;
    } else {
      valid = false;
    }
    return valid;
  }
  
  @Override
  public String getName() {
  
    return "SignedValidator";
  }
}
