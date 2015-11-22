/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 19, 2010
 *  @author singla
 */

package com.prchny.base.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.prchny.base.validation.impl.CreditCardValidator;

/**
 * @Deprecated
 * @see CreditCardValidator etc.
 */
public class ValidatorUtils {
  
  private static final String ATOM =
      "[^\\x00-\\x1F^\\(^\\)^\\<^\\>^\\@^\\,^\\;^\\:^\\\\^\\\"^\\.^\\[^\\]^\\s]";
  
  private static final String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)+";
  
  private static final Pattern EMAIL_PATTERN = java.util.regex.Pattern.compile(
      "^" + ATOM + "+(\\." + ATOM + "+)*@" + DOMAIN + ")$",
      java.util.regex.Pattern.CASE_INSENSITIVE);
  
  private static final Pattern MOBILE_PATTERN = Pattern
      .compile("^\\+?(91|0)?-?([7-9][0-9]{9})$");
  
  public static boolean isEmailPatternValid(String value) {
  
    if (value == null || value.length() == 0) {
      return false;
    }
    return EMAIL_PATTERN.matcher(value).matches();
  }
  
  public static String normaliseEmail(String value) {
  
    if (value == null || value.length() == 0) {
      return null;
    }
    if (value.endsWith("@gmail.com")) {
      return value.replace(".", "");
    }
    return value;
    
  }
  
  public static String getValidMobileOrNull(String value) {
  
    if (value == null) {
      return null;
    }
    Matcher m = MOBILE_PATTERN.matcher(value);
    if (m.matches()) {
      return m.group(2);
    } else {
      return null;
    }
  }
  
  public static boolean isPincodeValid(String value) {
  
    try {
      Integer.parseInt(value);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return value.toCharArray().length == 6;
  }
  
  public static boolean isCreditCardValid(String value) {
  
    char[] chars = value.toCharArray();
    List<Integer> digits = new ArrayList<Integer>();
    for (char c : chars) {
      if (Character.isDigit(c)) {
        digits.add(c - '0');
      } else {
        return false;
      }
    }
    int length = digits.size();
    int sum = 0;
    boolean even = false;
    for (int index = length - 1; index >= 0; index--) {
      int digit = digits.get(index);
      if (even) {
        digit *= 2;
      }
      if (digit > 9) {
        digit = digit / 10 + digit % 10;
      }
      sum += digit;
      even = !even;
    }
    return sum % 10 == 0;
  }
}
