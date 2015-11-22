/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Aug 20, 2010
 *  @author singla
 */

package com.prchny.base.expression;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.prchny.base.utils.DateUtils;

public class Expression {
  
  private final org.springframework.expression.Expression expression;
  
  private Expression(final org.springframework.expression.Expression expression) {
  
    this.expression = expression;
    
  }
  
  public static Expression compile(final String expressionText) {
  
    final ExpressionParser parser = new SpelExpressionParser();
    return new Expression(parser.parseExpression(expressionText,
        ParserContext.TEMPLATE_EXPRESSION));
  }
  
  public <T> T evaluate(final Map<String, Object> contextParams,
      final Class<T> retType) {
  
    final EvaluationContext context = new StandardEvaluationContext();
    for (final Map.Entry<String, Object> eContextParam : contextParams
        .entrySet()) {
      context.setVariable(eContextParam.getKey(), eContextParam.getValue());
    }
    return expression.getValue(context, retType);
  }
  
  public String evaluate(final Map<String, Object> contextParams) {
  
    return evaluate(contextParams, String.class);
  }
  
  public static void main(final String[] args) {
  
    final Expression expression =
        Expression
            .compile("#{(#expiryDate > #currentTime) ? (#deal.type == 'product' ? (#currentTime.Date == 14 ? 'success': 'Promocode will be active only on August 14, 2011') : 'Promo code applicable only on products worth more than Rs 800'): 'Invalid promo code.'}");
    final Map<String, Object> contextParams = new HashMap<String, Object>();
    // contextParams.put("value", 100);
    // contextParams.put("quantity", 15);
    final Deal deal = new Deal();
    deal.type = "product";
    contextParams.put("deal", deal);
    contextParams.put("currentTime", new Date());
    contextParams.put("expiryDate",
        DateUtils.addToDate(new Date(), Calendar.DATE, 5));
    final String evaluate = expression.evaluate(contextParams, String.class);
    System.out.println(evaluate);
  }
  
  public static class Deal {
    
    public String type = "deal";
  }
}
