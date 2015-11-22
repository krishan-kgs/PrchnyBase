
package com.prchny.base.cron.validator;

/**
 * @author naveen
 */
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CronExpressionValidator implements
  ConstraintValidator<CronExpression, String> {
  
  @Override
  public void initialize(final CronExpression cronExpression) {
  
    // To change body of implemented methods use File | Settings | File
    // Templates.
  }
  
  @Override
  public boolean isValid(final String s,
      final ConstraintValidatorContext constraintValidatorContext) {
  
    return org.quartz.CronExpression.isValidExpression(s);
  }
}
