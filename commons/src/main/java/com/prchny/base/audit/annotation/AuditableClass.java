
package com.prchny.base.audit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * 
 * Identifies if a class can be audited. Typically, such a class would contain data that needs to be audited.
 * NOTE: DO NOT Change the Class Name! It is being referred to as a String in AuditingAspect
 * 
 * @author ashish.saxena
 *
 */
public @interface AuditableClass {
  
}
