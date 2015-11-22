
package com.prchny.base.ruleEngine.manager;

import java.util.List;
import java.util.Set;

import org.drools.KnowledgeBase;

import com.prchny.base.ruleEngine.IFact;
import com.prchny.base.ruleEngine.exception.RuleEngineException;

public interface IRuleEngineManager {
  
  KnowledgeBase initialiseKnowledgeBase(String ruleFile)
      throws RuleEngineException;
  
  KnowledgeBase initialiseKnowledgeBase(Set<String> ruleFiles)
      throws RuleEngineException;
  
  void fireRules(IFact fact) throws RuleEngineException;
  
  void fireRules(List<IFact> facts) throws RuleEngineException;
}
