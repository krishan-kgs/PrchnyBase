package com.prchny.base.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidFormatException extends Exception {
  
  /**
   * 
   */
  private static final long serialVersionUID = -6431598473101753904L;
  
  private static final Logger LOG = LoggerFactory
      .getLogger(InvalidFormatException.class);
  
  private final String fileName;
  
  public InvalidFormatException(final String fileName) {
  
    this.fileName = fileName;
  }
  
  public InvalidFormatException() {
  
    fileName = "";
  }
  
  @Override
  public void printStackTrace() {
  
    LOG.error("fileName : {}", fileName);
    super.printStackTrace();
  }
  
}
