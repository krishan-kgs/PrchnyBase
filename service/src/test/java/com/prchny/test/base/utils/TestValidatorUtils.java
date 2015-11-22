/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Apr 20, 2011
 *  @author amit
 */

package com.prchny.test.base.utils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestValidatorUtils {
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  
  }
  
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  
  }
  
  @Before
  public void setUp() throws Exception {
  
  }
  
  @After
  public void tearDown() throws Exception {
  
  }
  
  @Test
  public void emailValidatorTest() {
  
    // // valid email addresses
    // assertEquals(true, ValidatorUtils.isEmailValid("amit@gmail.com"));
    // assertEquals(true,
    // ValidatorUtils.isEmailValid("?'a$m%i.t@facebook.com"));
    //
    // // invalid email addresses
    // assertEquals(false, ValidatorUtils.isEmailValid("?'a$m%i.t@gmail.com"));
    // assertEquals(false, ValidatorUtils.isEmailValid("<amit@gmail.com"));
    // assertEquals(false, ValidatorUtils.isEmailValid(",()[];:<>@gmail.com"));
    // assertEquals(false, ValidatorUtils.isEmailValid(".amit@gmail.com"));
    // assertEquals(false, ValidatorUtils.isEmailValid("amit.@gmail.com"));
    // assertEquals(false, ValidatorUtils.isEmailValid("am..it@gmail.com"));
    // assertEquals(false, ValidatorUtils.isEmailValid("am@it@gmail.com"));
    // assertEquals(false, ValidatorUtils.isEmailValid("amitgmail.com"));
    // assertEquals(false, ValidatorUtils.isEmailValid("amit@spam.la"));
  }
  
}
