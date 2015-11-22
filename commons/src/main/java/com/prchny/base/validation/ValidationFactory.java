/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 29-Dec-2012
 *  @author amd
 */

package com.prchny.base.validation;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@SuppressWarnings("rawtypes")
@Component
public class ValidationFactory {
  
  private static Map<Class, Validator> validatorMap =
      new HashMap<Class, Validator>();
  
  public static Validator getValidator(final Annotation a) {
  
    return validatorMap.get(a.annotationType());
  }
  
  public static void addValidator(final Class clazz, final Validator validator) {
  
    validatorMap.put(clazz, validator);
  }
}
