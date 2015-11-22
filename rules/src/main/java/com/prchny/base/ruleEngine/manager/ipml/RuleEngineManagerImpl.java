
package com.prchny.base.ruleEngine.manager.ipml;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.DecisionTableFactory;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prchny.base.ruleEngine.IFact;
import com.prchny.base.ruleEngine.entity.SDCashFraud;
import com.prchny.base.ruleEngine.exception.RuleEngineException;
import com.prchny.base.ruleEngine.manager.IRuleEngineManager;
import com.prchny.base.utils.StringUtils;

@Service("ruleEngine")
public class RuleEngineManagerImpl implements IRuleEngineManager {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(RuleEngineManagerImpl.class);
  
  private KnowledgeBase kbase;
  
  @Override
  public void fireRules(final IFact fact) throws RuleEngineException {
  
    if (kbase == null) {
      throw new RuleEngineException("KnowledgeBase not initialized");
    }
    final StatelessKnowledgeSession ksession =
        kbase.newStatelessKnowledgeSession();
    ksession.execute(fact);
  }
  
  @Override
  public void fireRules(final List<IFact> facts) throws RuleEngineException {
  
    if (kbase == null) {
      throw new RuleEngineException("KnowledgeBase not initialized");
    }
    final StatelessKnowledgeSession ksession =
        kbase.newStatelessKnowledgeSession();
    ksession.execute(facts);
  }
  
  @Override
  public KnowledgeBase initialiseKnowledgeBase(final Set<String> ruleFiles)
      throws RuleEngineException {
  
    if ((ruleFiles == null) || ruleFiles.isEmpty()) {
      throw new RuleEngineException("No rule files passed.");
    }
    final KnowledgeBuilder kbuilder =
        KnowledgeBuilderFactory.newKnowledgeBuilder();
    for (final String file : ruleFiles) {
      final int index = file.lastIndexOf(".");
      final String ext = file.substring(index + 1);
      if ("drl".equals(ext)) {
        // Adds from DRL file
        kbuilder.add(ResourceFactory.newClassPathResource(file),
            ResourceType.DRL);
      } else if ("xls".equals(ext)) {
        // Adds from XLS file
        final Resource resource = ResourceFactory.newClassPathResource(file);
        final DecisionTableConfiguration dtconf =
            KnowledgeBuilderFactory.newDecisionTableConfiguration();
        dtconf.setInputType(DecisionTableInputType.XLS);
        kbuilder.add(resource, ResourceType.DTABLE, dtconf);
        try {
          DecisionTableFactory.loadFromInputStream(resource.getInputStream(),
              dtconf);
        } catch (final Exception e) {
          LOG.error("Error loading decision table from file: " + file, e);
          throw new RuleEngineException(
              "Error loading decision table from file: " + file, e);
        }
      } else {
        throw new RuleEngineException("File extension not supported: " + ext);
      }
      
      final KnowledgeBuilderErrors errors = kbuilder.getErrors();
      if (errors.size() > 0) {
        for (final KnowledgeBuilderError error : errors) {
          System.out.println(error);
          LOG.error("Could not parse ruleFile : " + file + ", error: "
              + error.toString());
        }
        throw new RuleEngineException("Could not parse ruleFile : " + file);
      }
    }
    kbase = KnowledgeBaseFactory.newKnowledgeBase();
    kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
    return kbase;
  }
  
  @Override
  public KnowledgeBase initialiseKnowledgeBase(final String ruleFile)
      throws RuleEngineException {
  
    if (StringUtils.isEmpty(ruleFile)) {
      throw new RuleEngineException("Invalid rule file name: " + ruleFile);
    }
    final Set<String> ruleFiles = new HashSet<String>();
    ruleFiles.add(ruleFile);
    return initialiseKnowledgeBase(ruleFiles);
  }
  
  public static void main(final String[] args) throws RuleEngineException {
  
    final RuleEngineManagerImpl ruleEngineManager = new RuleEngineManagerImpl();
    ruleEngineManager.initialiseKnowledgeBase("testRules.drl");
    final SDCashFraud sdf = new SDCashFraud();
    sdf.setSdCashValue(600);
    ruleEngineManager.fireRules(sdf);
    System.out.println(sdf.isFraud());
    
    final SDCashFraud sdf1 = new SDCashFraud();
    sdf1.setSdCashValue(1000);
    ruleEngineManager.fireRules(sdf1);
    System.out.println(sdf1.isFraud());
  }
}
