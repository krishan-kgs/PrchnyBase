// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Cryptography.java

package com.prchny.base.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Cryptography {
  
  public static String KEY = "";
  
  public Cryptography() {
  
  }
  
  public static String encrypt(final String keyAes, final String data)
      throws Exception {
  
    final Key key = generateKey(keyAes);
    final Cipher c = Cipher.getInstance("AES");
    c.init(1, key);
    final byte encVal[] = c.doFinal(data.getBytes());
    final String encryptedValue = (new BASE64Encoder()).encode(encVal);
    return encryptedValue;
  }
  
  public static String decrypt(final String keyAes, final String data)
      throws Exception {
  
    final Key key = generateKey(keyAes);
    final Cipher c = Cipher.getInstance("AES");
    c.init(2, key);
    final byte decodedValue[] = (new BASE64Decoder()).decodeBuffer(data);
    final byte decValue[] = c.doFinal(decodedValue);
    final String decryptedValue = new String(decValue);
    return decryptedValue;
  }
  
  public static Key generateKey(final String keyAes) throws Exception {
  
    final byte[] keyAsBytes = keyAes.getBytes("UTF8");
    final Key key = new SecretKeySpec(keyAsBytes, "AES");
    return key;
  }
  
  private static final String ALGO = "AES";
  
  private static final byte keyValue[] = {
      35, 115, 36, 110, 33, 64, 97, 112, 94, 42, 100, 101, 95, 97, 36, 108
  };
  
}
