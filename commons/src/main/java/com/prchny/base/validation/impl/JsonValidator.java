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

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.prchny.base.annotations.Json;
import com.prchny.base.validation.ValidationFactory;
import com.prchny.base.validation.Validator;

@Component
public class JsonValidator implements Validator {
  
  @PostConstruct
  @Override
  public void registerValidator() {
  
    ValidationFactory.addValidator(Json.class, this);
  }
  
  @Override
  public boolean isValid(final Field f, final Object obj)
      throws IllegalArgumentException, IllegalAccessException {
  
    final Object thisObj = f.get(obj);
    boolean valid = false;
    try {
      if (thisObj != null) {
        new JsonParser().parse(thisObj.toString());
      }
      valid = true;
    } catch (final JsonParseException ex) {
    }
    return valid;
  }
  
  @Override
  public String getName() {
  
    return "JsonValidator";
  }
}
