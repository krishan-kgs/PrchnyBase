/*
 *  Copyright 2010 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, Sep 20, 2010
 *  @author rahul
 */

package com.prchny.base.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.prchny.base.annotations.Cache;
import com.prchny.base.entity.EmailDomain;

@Cache(name = "emailDomainCache")
public class EmailDomainsCache {
  
  private final List<EmailDomain> emailDomains = new ArrayList<EmailDomain>();
  
  private final Map<String, EmailDomain> domainToEmailDomain =
      new HashMap<String, EmailDomain>();
  
  private final Map<String, Pattern> domainToEmailPattern =
      new HashMap<String, Pattern>();
  
  public void addEmailDomain(final EmailDomain emailDomain) {
  
    emailDomains.add(emailDomain);
    domainToEmailDomain.put(emailDomain.getDomain(), emailDomain);
    if (emailDomain.getAddressPattern() != null) {
      domainToEmailPattern.put(emailDomain.getDomain(),
          Pattern.compile(emailDomain.getAddressPattern()));
    }
  }
  
  public boolean isDomainBlocked(final String emailDomain) {
  
    final EmailDomain domain = domainToEmailDomain.get(emailDomain);
    return domain != null ? domain.isBlocked() : false;
  }
  
  public Pattern getAddressPatternForDomain(final String emailDomain) {
  
    return domainToEmailPattern.get(emailDomain);
  }
}
