/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 29-Dec-2012
 *  @author amd
 */

package com.prchny.base.validation;

import java.lang.reflect.Field;

public interface Validator {
  
  public boolean isValid(Field f, Object obj) throws IllegalArgumentException,
      IllegalAccessException;
  
  public String getName();
  
  public void registerValidator();
}
