
package com.prchny.base.ruleEngine.exception;

public class RuleEngineException extends Exception {
  
  private static final long serialVersionUID = -3678905704260312098L;
  
  public RuleEngineException(final String message) {
  
    super(message);
  }
  
  public RuleEngineException(final String message, final Throwable cause) {
  
    super(message, cause);
  }
}
