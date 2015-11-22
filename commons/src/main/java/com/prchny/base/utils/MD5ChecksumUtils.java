/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 17, 2010
 *  @author singla
 */

package com.prchny.base.utils;

import java.io.InputStream;
import java.security.MessageDigest;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class MD5ChecksumUtils {
  
  private static final String HEXES = "0123456789ABCDEF";
  
  private static byte[] createChecksum(final InputStream fis) throws Exception {
  
    final byte[] buffer = new byte[1024];
    final MessageDigest complete = MessageDigest.getInstance("MD5");
    int numRead;
    do {
      numRead = fis.read(buffer);
      if (numRead > 0) {
        complete.update(buffer, 0, numRead);
      }
    } while (numRead != -1);
    fis.close();
    return complete.digest();
  }
  
  private static String getHex(final byte[] raw) {
  
    final StringBuilder hex = new StringBuilder(2 * raw.length);
    for (final byte b : raw) {
      hex.append(HEXES.charAt((b & 0xF0) >> 4))
          .append(HEXES.charAt((b & 0x0F)));
    }
    return hex.toString();
  }
  
  public static String getMD5Checksum(final InputStream fis) throws Exception {
  
    return getHex(createChecksum(fis));
  }
  
  public static String md5Encode(final String text, final String salt) {
  
    return new Md5PasswordEncoder().encodePassword(text, salt);
  }
  
  public static boolean isCodeValid(final String code, final String text,
      final String salt) {
  
    return new Md5PasswordEncoder().isPasswordValid(code, text, salt);
  }
  
}
