/**
 * 
 */

package com.prchny.base.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author fanendra
 *
 */
public class RandomKeyGenerator {
  
  private static SecureRandom SECURE_RANDOM;
  
  private static MessageDigest MESSAGE_DIGEST;
  
  /**
   * SecureRandom algorithm name.
   */
  private static final String SR_ALGO = "SHA1PRNG";
  
  /**
   * MessageDigest algorithm name.
   */
  private static final String MD_ALGO = "SHA-1";
  
  static {
    try {
      SECURE_RANDOM = SecureRandom.getInstance(SR_ALGO);
      MESSAGE_DIGEST = MessageDigest.getInstance(MD_ALGO);
    } catch (final NoSuchAlgorithmException e) {
    } catch (final Exception e) {
    }
  }
  
  /**
   * Responsible for generating the random key.
   * 
   * @return generated random key.
   */
  public static String randomKey() {
  
    try {
      final String randomNum = String.valueOf(SECURE_RANDOM.nextLong());
      final byte[] result = MESSAGE_DIGEST.digest(randomNum.getBytes());
      return hexEncode(result);
    } catch (final Exception e) {
      return randomKey();
    }
  }
  
  /**
   * The byte[] returned by MessageDigest does not have a nice textual
   * representation, so some form of encoding is usually performed. This method
   * converts a byte array into a String of hex characters.
   */
  private static String hexEncode(final byte[] aInput) {
  
    final StringBuilder result = new StringBuilder();
    final char[] digits =
        {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
            'd', 'e', 'f'
        };
    for (final byte b : aInput) {
      result.append(digits[(b & 0xf0) >> 4]);
      result.append(digits[b & 0x0f]);
    }
    return result.toString();
  }
}
