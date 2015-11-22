
package com.prchny.base.audit.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.prchny.base.audit.AuditingManager;

/**
 * Aspect to enable auditing of services. The intention is to capture - the
 * request and response - both!
 * 
 * @author ashish.saxena
 * 
 */
@Configurable
@Aspect
public class AuditingAspect {
  
  @Autowired
  private AuditingManager auditingAgent;
  
  @Around("execution(@com.prchny.base.audit.annotation.AuditableMethod * com.prchny..*.*(..))")
  /**
   * 
   * Audits the request and response of the services which are @AuditingEnabled 
   * @param joinPoint
   * @return
   * @throws Throwable
   */
  public Object audit(final ProceedingJoinPoint joinPoint) throws Throwable {
  
    return performAuditing(joinPoint);
    
  }
  
  public Object performAuditing(final ProceedingJoinPoint joinPoint)
      throws Throwable {
  
    final Object[] reqArguments = joinPoint.getArgs();
    
    if ((reqArguments != null) && (reqArguments.length > 0)) {
      for (final Object reqArgument : reqArguments) {
        auditingAgent.audit(reqArgument);
      }
    }
    
    final Object response = joinPoint.proceed();
    auditingAgent.audit(response);
    
    return response;
  }
  
}
