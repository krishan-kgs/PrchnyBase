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

import com.prchny.base.annotations.Length;
import com.prchny.base.validation.ValidationFactory;
import com.prchny.base.validation.Validator;

@Component
public class LengthValidator implements Validator {
  
  @PostConstruct
  @Override
  public void registerValidator() {
  
    ValidationFactory.addValidator(Length.class, this);
  }
  
  @Override
  public boolean isValid(final Field f, final Object obj)
      throws IllegalArgumentException, IllegalAccessException {
  
    final Object thisObj = f.get(obj);
    return thisObj != null
        ? f.getAnnotation(Length.class).maxValue() >= thisObj.toString()
            .toCharArray().length : true;
  }
  
  @Override
  public String getName() {
  
    return "LengthValidator";
  }
}