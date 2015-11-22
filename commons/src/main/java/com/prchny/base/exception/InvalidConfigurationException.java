package com.prchny.base.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidConfigurationException extends Exception {
  
  /**
   * 
   */
  private static final long serialVersionUID = 8612615691424268963L;
  
  private static final Logger LOG = LoggerFactory
      .getLogger(InvalidConfigurationException.class);
  
  private String message = "";
  
  public InvalidConfigurationException() {
  
  }
  
  public InvalidConfigurationException(final String message) {
  
    this.message = message;
  }
  
  @Override
  public void printStackTrace() {
  
    LOG.error(message);
    super.printStackTrace();
  }
  
}
