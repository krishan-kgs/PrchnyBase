
package com.prchny.base.cache.scheduler.impl;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@Service("reloadCacheScheduler")
public class ReloadCacheSchedulerImpl extends ThreadPoolTaskScheduler {
  
  private static final long serialVersionUID = 3024537078879822309L;
  
}
