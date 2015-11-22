
package com.prchny.base.cron.validator;

/**
 * @author naveen 
 */
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = CronExpressionValidator.class)
@Target({
    METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface CronExpression {
  
  String message() default "Cron expression is not valid! Example : 0 0 0 1 * ?";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
}
