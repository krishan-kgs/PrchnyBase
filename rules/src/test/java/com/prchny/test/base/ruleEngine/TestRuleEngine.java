/*
 *  Copyright 2011 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Apr 20, 2011
 *  @author amit
 */

package com.prchny.test.base.ruleEngine;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.prchny.base.ruleEngine.exception.RuleEngineException;
import com.prchny.base.ruleEngine.manager.IRuleEngineManager;

@ContextConfiguration(locations = {
  "/applicationContext-test.xml"
})
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class TestRuleEngine {
  
  @Autowired
  private IRuleEngineManager ruleEngineManager;
  
  @Before
  public void setUp() throws Exception {
  
    ruleEngineManager.initialiseKnowledgeBase("testRules.drl");
  }
  
  @After
  public void tearDown() throws Exception {
  
  }
  
  @Test
  public void isFraud() throws RuleEngineException {
  
    final SDCashFraud sdf = new SDCashFraud();
    sdf.setSdCashValue(600);
    ruleEngineManager.fireRules(sdf);
    assertEquals(true, sdf.isFraud());
    assertEquals("Fraud", sdf.getMessage());
  }
  
  @Test
  public void isNotFraud() throws RuleEngineException {
  
    final SDCashFraud sdf = new SDCashFraud();
    sdf.setSdCashValue(499);
    ruleEngineManager.fireRules(sdf);
    assertEquals(false, sdf.isFraud());
  }
}
