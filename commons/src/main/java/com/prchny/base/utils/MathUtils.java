/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Dec 1, 2011
 *  @author amit
 */

package com.prchny.base.utils;

import java.text.DecimalFormat;

public class MathUtils {
  
  public static Float roundOffTo2Decimal(final Float number) {
  
    return Float.parseFloat(new DecimalFormat("0.00").format(number));
  }
  
  public static Double roundOffTo2Decimal(final Double number) {
  
    return Double.parseDouble(new DecimalFormat("0.00").format(number));
  }
}
