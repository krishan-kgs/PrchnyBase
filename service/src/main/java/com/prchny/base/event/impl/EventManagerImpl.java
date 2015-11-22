
package com.prchny.base.event.impl;

import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import com.prchny.base.event.IEvent;
import com.prchny.base.event.IEventManager;

/**
 * Event manger is responsible for propagating triggered event asynchronously to
 * all the registered handlers
 * 
 * @author singla
 */
@Service("eventManager")
public class EventManagerImpl implements IEventManager, DisposableBean {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(EventManagerImpl.class);
  
  // TODO We need to optimize the number of threads for this executor
  private final ScheduledExecutorService scheduler = Executors
      .newScheduledThreadPool(40);
  
  protected Map<IEvent, Set<EventSubscription>> eventSubscriptionLists =
      new HashMap<IEvent, Set<EventSubscription>>();
  
  /*
   * (non-Javadoc)
   * 
   * @see
   * com.prchny.services.event.IEventManager#registerEventListener(com.prchny
   * .services.event.impl.EventManagerImpl.GenericEventListener,
   * com.prchny.services.event.IEvent)
   */
  @Override
  public synchronized void registerEventListener(
      final GenericEventListener receiver, final IEvent event) {
  
    registerEventListener(receiver, event, null);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see
   * com.prchny.services.event.IEventManager#registerEventListener(com.prchny
   * .services.event.impl.EventManagerImpl.GenericEventListener,
   * com.prchny.services.event.IEvent,
   * com.prchny.services.event.impl.EventManagerImpl.Condition)
   */
  @Override
  public synchronized void registerEventListener(
      final GenericEventListener receiver, final IEvent event,
      final Condition condition) {
  
    LOG.info("registering receiver:{} for event:{}", receiver, event);
    Set<EventSubscription> subcriptionList = eventSubscriptionLists.get(event);
    if (subcriptionList == null) {
      subcriptionList = new HashSet<EventSubscription>();
      eventSubscriptionLists.put(event, subcriptionList);
    }
    
    final EventSubscription subscription =
        new EventSubscription(receiver, event, condition);
    if (!subcriptionList.contains(subscription)) {
      subcriptionList.add(subscription);
    }
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see
   * com.prchny.services.event.IEventManager#unregisterEventListener(com.prchny
   * .services.event.impl.EventManagerImpl.GenericEventListener,
   * com.prchny.services.event.IEvent)
   */
  @Override
  public synchronized void unregisterEventListener(
      final GenericEventListener receiver, final IEvent event) {
  
    final EventSubscription subscription =
        new EventSubscription(receiver, event, null);
    final Set<EventSubscription> subcriptionList =
        eventSubscriptionLists.get(event);
    if (subcriptionList.contains(subscription)) {
      subcriptionList.remove(subscription);
    }
  }
  
  /**
   * Create an instance of Runnable for the specified event.
   * 
   * @param sender
   *          object that triggered the event
   * @param event
   *          the event
   * @return new Runnable, or null if there are no subscribing nodes.
   */
  private synchronized Runnable createEventRunnable(final Object sender,
      final IEvent event) {
  
    return new Runnable() {
      
      @Override
      public void run() {
      
        notifySubscribers(sender, event, false);
      }
    };
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see
   * com.prchny.services.event.IEventManager#triggerEvent(java.lang.Object,
   * com.prchny.services.event.IEvent)
   */
  @Override
  public EventResponse triggerEvent(final Object sender, final IEvent event) {
  
    return triggerEvent(sender, event, false);
  }
  
  @Override
  public EventResponse triggerEvent(final Object sender, final IEvent event,
      final boolean isSynchronous) {
  
    return notifySubscribers(sender, event, isSynchronous);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see
   * com.prchny.services.event.IEventManager#triggerFutureEvent(java.lang.
   * Object, com.prchny.services.event.IEvent, long,
   * java.util.concurrent.TimeUnit)
   */
  @Override
  public void triggerFutureEvent(final Object sender, final IEvent event,
      final long delay, final TimeUnit timeUnit) {
  
    final Runnable r = createEventRunnable(sender, event);
    scheduler.schedule(r, delay, timeUnit);
  }
  
  /**
   * Notify all the subscribers asynchronously based on the condition set by
   * them on register
   * 
   * @param sender
   * @param event
   */
  private EventResponse notifySubscribers(final Object sender,
      final IEvent event, final boolean isSynchronous) {
  
    final Set<EventSubscription> subscriptionList =
        eventSubscriptionLists.get(event);
    if ((subscriptionList == null) || subscriptionList.isEmpty()) {
      return event.getResponse();
    }
    
    final Iterator<EventSubscription> iterator = subscriptionList.iterator();
    while (iterator.hasNext()) {
      final EventSubscription eventSubscription = iterator.next();
      if (eventSubscription.getEvent().getClass() == event.getClass()) {
        if ((eventSubscription.getCondition() == null)
            || eventSubscription.getCondition().matches(sender, event)) {
          if (!isSynchronous) {
            invokeHandlerMethodAsynchronously(sender, event,
                eventSubscription.getReceiver());
          } else {
            invokeHandlerMethodSynchronously(sender, event,
                eventSubscription.getReceiver());
          }
        }
      }
    }
    return event.getResponse();
  }
  
  private void invokeHandlerMethodSynchronously(final Object sender,
      final IEvent event, final GenericEventListener receiver) {
  
    try {
      LOG.info("synchronously triggering event {} on receiver:{}", event,
          receiver);
      receiver.eventTriggered(sender, event);
    } catch (final Throwable e) {
      LOG.error(
          "error while synchronously  triggering event: {} on receiver:{}, errorMessage:{}",
          new Object[] {
              event.getName(), receiver, e.getMessage()
          }, e);
      e.printStackTrace();
    }
  }
  
  private void invokeHandlerMethodAsynchronously(final Object sender,
      final IEvent event, final GenericEventListener receiver) {
  
    scheduler.submit(new Runnable() {
      
      @Override
      public void run() {
      
        try {
          LOG.info("triggering event {} on receiver:{}", event, receiver);
          receiver.eventTriggered(sender, event);
        } catch (final Throwable e) {
          LOG.error(
              "error while triggering event: {} on receiver:{}, errorMessage:{}",
              new Object[] {
                  event.getName(), receiver, e.getMessage()
              }, e);
          e.printStackTrace();
        }
      }
    });
  }
  
  public interface GenericEventListener extends EventListener {
    
    void eventTriggered(Object sender, IEvent event);
  }
  
  private class EventSubscription {
    
    private final GenericEventListener receiver;
    
    private final IEvent event;
    
    private final Condition condition;
    
    public EventSubscription(final GenericEventListener receiver,
        final IEvent event, final Condition condition) {
    
      this.receiver = receiver;
      this.event = event;
      this.condition = condition;
    }
    
    public GenericEventListener getReceiver() {
    
      return receiver;
    }
    
    public IEvent getEvent() {
    
      return event;
    }
    
    public Condition getCondition() {
    
      return condition;
    }
    
    @Override
    public int hashCode() {
    
      final int prime = 31;
      int result = 1;
      result =
          (prime * result) + ((receiver == null) ? 0 : receiver.hashCode());
      return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
    
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final EventSubscription other = (EventSubscription) obj;
      if (receiver == null) {
        if (other.receiver != null) {
          return false;
        }
      } else if (!receiver.equals(other.receiver)) {
        return false;
      }
      return true;
    }
    
  }
  
  public interface Condition {
    
    boolean matches(Object sender, IEvent event);
  }
  
  @Override
  public ScheduledExecutorService getScheduler() {
  
    return scheduler;
  }
  
  @Override
  public void destroy() throws Exception {
  
    LOG.info("Shutting down EventManager's executor service");
    scheduler.shutdown();
  }
}
