/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Sep 18, 2010
 *  @author singla
 */

package com.prchny.base.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptionUtils {
  
  public static final String ENCRYPTION_SCHEME = "DESede";
  
  public static final String ENCRYPTION_KEY = "SNAPDEAL ENCRYPTION KEYX";
  
  private static final String UNICODE_FORMAT = "UTF8";
  
  private static KeySpec keySpec;
  
  private static SecretKeyFactory keyFactory;
  
  private static final char[] HEX = {
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
      'e', 'f'
  };
  
  static {
    try {
      final byte[] keyAsBytes = ENCRYPTION_KEY.getBytes(UNICODE_FORMAT);
      keySpec = new DESedeKeySpec(keyAsBytes);
      keyFactory = SecretKeyFactory.getInstance(ENCRYPTION_SCHEME);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
  
  public static String encrypt(final String unencryptedString) {
  
    if ((unencryptedString == null) || (unencryptedString.trim().length() == 0)) {
      return unencryptedString;
    }
    
    try {
      final SecretKey key = keyFactory.generateSecret(keySpec);
      final Cipher cipher = Cipher.getInstance(ENCRYPTION_SCHEME);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      final byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
      final byte[] ciphertext = cipher.doFinal(cleartext);
      final BASE64Encoder base64encoder = new BASE64Encoder();
      return base64encoder.encode(ciphertext);
    } catch (final Exception e) {
      // Unable to encrypt
      return unencryptedString;
    }
  }
  
  public static String encrypt(final int unencryptedInt) {
  
    return encrypt(String.valueOf(unencryptedInt));
  }
  
  public static String decrypt(final String encryptedString) {
  
    if ((encryptedString == null) || (encryptedString.trim().length() <= 0)) {
      return encryptedString;
    }
    
    try {
      final SecretKey key = keyFactory.generateSecret(keySpec);
      final Cipher cipher = Cipher.getInstance(ENCRYPTION_SCHEME);
      cipher.init(Cipher.DECRYPT_MODE, key);
      final BASE64Decoder base64decoder = new BASE64Decoder();
      final byte[] cleartext = base64decoder.decodeBuffer(encryptedString);
      final byte[] ciphertext = cipher.doFinal(cleartext);
      
      return new String(ciphertext, UNICODE_FORMAT);
    } catch (final Exception e) {
      // Unable to decrypt
      return encryptedString;
    }
  }
  
  public static String md5Encode(final String input, final String salt) {
  
    return md5Encode(mergePasswordAndSalt(input, salt));
  }
  
  private static String mergePasswordAndSalt(String input, final String salt) {
  
    if (input == null) {
      input = "";
    }
    
    if (StringUtils.isEmpty(salt)) {
      return input;
    } else {
      return input + "{" + salt + "}";
    }
  }
  
  public static String md5Encode(final String input) {
  
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance("MD5");
    } catch (final NoSuchAlgorithmException e1) {
      throw new IllegalArgumentException("No such algorithm MD5");
    }
    
    byte[] digest;
    
    try {
      digest = messageDigest.digest(input.getBytes("UTF-8"));
    } catch (final UnsupportedEncodingException e) {
      throw new IllegalStateException("UTF-8 not supported!");
    }
    return hexEncode(digest);
  }
  
  public static String hexEncode(final byte[] bytes) {
  
    final int nBytes = bytes.length;
    final char[] result = new char[2 * nBytes];
    
    int j = 0;
    for (int i = 0; i < nBytes; i++) {
      // Char for top 4 bits
      result[j++] = HEX[(0xF0 & bytes[i]) >>> 4];
      // Bottom 4
      result[j++] = HEX[(0x0F & bytes[i])];
    }
    
    return new String(result);
  }
  
  public static String base64UrlDecode(final String input) {
  
    final Base64 decoder = new Base64(true);
    final byte[] decodedBytes = decoder.decode(input);
    return new String(decodedBytes);
  }
  
  public static void main(final String[] args) {
  
    System.out.println(md5Encode("15356343949d8bf39f43ea56aafcc"));
  }
  
  public static String getSecureSuborderCode(final String suborderCode,
      final String mobile) {
  
    return MD5ChecksumUtils.md5Encode(suborderCode, mobile);
  }
  
}
