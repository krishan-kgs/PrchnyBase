/*
 *  Copyright 2014 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 07-Jul-2014
 *  @author ajinkya
 */

package com.prchny.base.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.prchny.base.model.common.ServiceRequest;
import com.prchny.base.services.request.context.LocalContextProvider;
import com.prchny.base.services.request.context.RequestContextProcessor;
import com.prchny.base.services.request.context.RequestContextProcessorFactory;
import com.prchny.base.services.request.context.RequestContextSRO;
import com.prchny.base.utils.Constants;
import com.prchny.base.utils.StringUtils;

@Aspect
@Component("requestContextProcessingAspect")
public class RequestContextProcessingAspect implements Ordered {
  
  @Autowired
  private RequestContextProcessorFactory requestContextProcessorFactory;
  
  // Scoping designator
  @Pointcut("execution(* com.prchny..*.*(..))")
  public void methodPointcut() {
  
  }
  
  // Contextual designator
  @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
  public void isRequestMapping() {
  
  }
  
  // Contextual designator
  @Pointcut("@annotation(javax.ws.rs.Path)")
  public void isPath() {
  
  }
  
  @Around("methodPointcut() && (isRequestMapping() || isPath())")
  public Object forwardRequestContext(final ProceedingJoinPoint pjp)
      throws Throwable {
  
    ServiceRequest serviceRequest = null;
    for (final Object arg : pjp.getArgs()) {
      if (arg instanceof ServiceRequest) {
        serviceRequest = (ServiceRequest) arg;
        break;
      }
    }
    
    // If one of the params is service request, try and transfer the request
    // context.
    if (serviceRequest != null) {
      // Create new context object based on original and save it in thread local
      // If no thread local is found in request, then transport layer will
      // create one during the first external API call.
      
      // Get copy of context so that any change in service request does not
      // affect the context stored
      LocalContextProvider localContext = null;
      if (serviceRequest.getContextSRO() != null) {
        // Set request context sro
        localContext =
            new LocalContextProvider(serviceRequest.getContextSRO().getCopy());
        
        // Get post processor bean
        final RequestContextProcessor postProcessor =
            requestContextProcessorFactory.getProcessor();
        if (postProcessor != null) {
          // In case some post processing functionality is added in the future
          // where custom RequestContextProviders are used
          localContext.setMetadata(postProcessor.process(pjp.getArgs()));
        }
      } else if (StringUtils.isNotEmpty(serviceRequest.getUserTrackingId())) {
        // Set requestId from userTrackingId field for compatibility with older
        // clients
        final RequestContextSRO sro = new RequestContextSRO();
        sro.setRequestId(serviceRequest.getUserTrackingId());
        localContext = new LocalContextProvider(sro);
      }
      
      if ((localContext != null)
          && (localContext.getRequestContextSRO().getRequestId() != null)) {
        // This is to override any keys set by RequestIdentifierFilter in case
        // it is unable to distinguish between API hit and panel hit
        // May log some invalid values in case a LOG operation is called before
        // hitting this line,
        // since LOG operation copies MDC's context before passing to appender
        MDC.put(Constants.REQUEST_USER_TRACKING_KEY, localContext
            .getRequestContextSRO().getRequestId());
        
        // Set context in thread local.
        PrchnyServiceRequestContextHolder.setRequestContext(localContext);
      }
      
    }
    
    try {
      return pjp.proceed();
    } finally {
      PrchnyServiceRequestContextHolder.clearRequestContext();
    }
  }
  
  /**
   * This method will return a value such that this aspect has a lower
   * precedence than other aspects like trace-interceptor
   */
  @Override
  public int getOrder() {
  
    return Integer.MAX_VALUE - 1;
  }
  
}
