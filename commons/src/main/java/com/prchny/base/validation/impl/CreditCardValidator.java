/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 29-Dec-2012
 *  @author amd
 */

package com.prchny.base.validation.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.prchny.base.annotations.CreditCard;
import com.prchny.base.validation.ValidationFactory;
import com.prchny.base.validation.Validator;

@Component
public class CreditCardValidator implements Validator {
  
  @PostConstruct
  @Override
  public void registerValidator() {
  
    ValidationFactory.addValidator(CreditCard.class, this);
  }
  
  @Override
  public boolean isValid(final Field f, final Object obj)
      throws IllegalArgumentException, IllegalAccessException {
  
    final Object thisObj = f.get(obj);
    return thisObj != null ? isCreditCardValid(thisObj
        .toString()) : true;
  }
  
  public static boolean isCreditCardValid(final String value) {
    
    final char[] chars = value.toCharArray();
    final List<Integer> digits = new ArrayList<Integer>();
    for (final char c : chars) {
      if (Character.isDigit(c)) {
        digits.add(c - '0');
      } else {
        return false;
      }
    }
    final int length = digits.size();
    int sum = 0;
    boolean even = false;
    for (int index = length - 1; index >= 0; index--) {
      int digit = digits.get(index);
      if (even) {
        digit *= 2;
      }
      if (digit > 9) {
        digit = (digit / 10) + (digit % 10);
      }
      sum += digit;
      even = !even;
    }
    return (sum % 10) == 0;
  }
  @Override
  public String getName() {
  
    return "CreditCardValidator";
  }
}
