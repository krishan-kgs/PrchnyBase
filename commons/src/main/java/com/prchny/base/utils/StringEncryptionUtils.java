/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 16-Dec-2011
 *  @author SHUBHANSHU
 */

package com.prchny.base.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class StringEncryptionUtils {
  
  public static final String DESEDE_ENCRYPTION_SCHEME =
      "DESede/CBC/PKCS5Padding";
  
  public static final String AES_ENCRYPTION_SCHEME = "AES/CBC/PKCS5Padding";
  
  private KeySpec keySpec;
  
  private SecretKeyFactory keyFactory;
  
  private SecretKey key;
  
  private Cipher cipher;
  
  private AlgorithmParameterSpec paramSpec;
  
  public StringEncryptionUtils(final String encryptionKey)
      throws StringEncryptionUtils.EncryptionException {
  
    final String encryptionScheme = "DESede/CBC/PKCS5Padding";
    if (encryptionKey == null) {
      throw new IllegalArgumentException("encryption key was null");
    }
    if (encryptionKey.trim().length() < 16) {
      throw new IllegalArgumentException(
          "encryption key was less than 16 characters");
    }
    
    try {
      final byte[] keyAsBytes = encryptionKey.getBytes("UTF8");
      
      if (encryptionScheme.equals("DESede/CBC/PKCS5Padding")) {
        final byte[] iv = {
            64, 64, 64, 64, 64, 64, 64, 64
        };
        
        paramSpec = new IvParameterSpec(iv);
        
        final byte[] rawkey = encryptionKey.getBytes();
        keySpec = new DESedeKeySpec(rawkey);
        keyFactory = SecretKeyFactory.getInstance("DESede");
        key = keyFactory.generateSecret(keySpec);
      } else if (encryptionScheme.equals("AES/CBC/PKCS5Padding")) {
        final byte[] iv = {
            64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64
        };
        paramSpec = new IvParameterSpec(iv);
        
        key = new SecretKeySpec(keyAsBytes, "AES");
      } else {
        throw new IllegalArgumentException("Encryption scheme not supported: "
            + encryptionScheme);
      }
      
      cipher = Cipher.getInstance(encryptionScheme);
    } catch (final InvalidKeyException e) {
      throw new EncryptionException(e);
    } catch (final InvalidKeySpecException e) {
      throw new EncryptionException(e);
    } catch (final UnsupportedEncodingException e) {
      throw new EncryptionException(e);
    } catch (final NoSuchAlgorithmException e) {
      throw new EncryptionException(e);
    } catch (final NoSuchPaddingException e) {
      throw new EncryptionException(e);
    }
  }
  
  public String encrypt(final String unencryptedString)
      throws StringEncryptionUtils.EncryptionException {
  
    if ((unencryptedString == null) || (unencryptedString.trim().isEmpty())) {
      throw new IllegalArgumentException("unencrypted string was null or empty");
    }
    
    try {
      cipher.init(1, key, paramSpec);
      
      final byte[] cleartext = unencryptedString.getBytes("UTF8");
      final byte[] ciphertext = cipher.doFinal(cleartext);
      
      final BASE64Encoder base64encoder = new BASE64Encoder();
      return base64encoder.encode(ciphertext);
    } catch (final Exception e) {
    }
    final Throwable e = null;
    throw new EncryptionException(e);
  }
  
  public String decrypt(final String encryptedString)
      throws StringEncryptionUtils.EncryptionException {
  
    if ((encryptedString == null) || (encryptedString.trim().length() <= 0)) {
      throw new IllegalArgumentException("encrypted string was null or empty");
    }
    
    try {
      cipher.init(2, key, paramSpec);
      
      final BASE64Decoder base64decoder = new BASE64Decoder();
      final byte[] cleartext = base64decoder.decodeBuffer(encryptedString);
      final byte[] ciphertext = cipher.doFinal(cleartext);
      
      return bytes2String(ciphertext);
    } catch (final Exception e) {
    }
    final Throwable e = null;
    throw new EncryptionException(e);
  }
  
  public String decrypt_cbc(final String encryptedString)
      throws StringEncryptionUtils.EncryptionException {
  
    if ((encryptedString == null) || (encryptedString.trim().length() <= 0)) {
      throw new IllegalArgumentException("encrypted string was null or empty");
    }
    
    try {
      cipher.init(2, key, paramSpec);
      final BASE64Decoder base64decoder = new BASE64Decoder();
      final byte[] cleartext = base64decoder.decodeBuffer(encryptedString);
      final byte[] ciphertext = cipher.doFinal(cleartext);
      
      return bytes2String(ciphertext);
    } catch (final Exception e) {
    }
    final Throwable e = null;
    throw new EncryptionException(e);
  }
  
  private static String bytes2String(final byte[] bytes) {
  
    final StringBuffer stringBuffer = new StringBuffer();
    for (final byte b : bytes) {
      stringBuffer.append((char) b);
    }
    return stringBuffer.toString();
  }
  
  public static String hexEncrypt(final String unencryptedString) {
  
    return new String(Hex.encodeHex(unencryptedString.getBytes()));
  }
  
  public static String hexDecrypt(final String cipher)
      throws EncryptionException {
  
    try {
      return new String(Hex.decodeHex(cipher.toCharArray()));
    } catch (final DecoderException e) {
      throw new EncryptionException(e);
    }
  }
  
  @SuppressWarnings("serial")
  public static class EncryptionException extends Exception {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public EncryptionException(final Throwable throwException) {
    
      super();
    }
  }
}
