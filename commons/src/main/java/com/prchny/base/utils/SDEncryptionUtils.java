/*
 *  Copyright 2013 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 17-Jul-2013
 *  @author aniket
 */

package com.prchny.base.utils;

public class SDEncryptionUtils {
  
  private static final String MD5_SALT_PASSWORD_ENCRYPTION =
      "prchnysaltforpassword123876heysaltie";
  
  public static String getMD5EncodedPassword(final String text) {
  
    return MD5ChecksumUtils.md5Encode(text, MD5_SALT_PASSWORD_ENCRYPTION);
  }
}
