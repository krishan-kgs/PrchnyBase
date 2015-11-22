
package com.prchny.base.event;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.prchny.base.event.impl.EventManagerImpl.Condition;
import com.prchny.base.event.impl.EventManagerImpl.GenericEventListener;
import com.prchny.base.event.impl.EventResponse;

/**
 * @author singla
 */
public interface IEventManager {
  
  /**
   * Registers an event listener, and binds it to a specific type of event.
   * Define your own types of events by creating classes imlement the Event
   * interface.
   * 
   * @param receiver
   *          The callback object that will be called once the event is
   *          triggered. This may typically be an anonymous implementation of
   *          the class.
   * @param event
   *          An instance of the type of Event that is subscribed to. This does
   *          not need to be the same Event instance that is used to trigger the
   *          event, it may be another instance of the same Event type (the
   *          class inheriting the Event class).
   */
  void registerEventListener(GenericEventListener receiver, IEvent event);
  
  /**
   * Registers an event listener, and binds it to a specific type of event and
   * provides a condition.
   * 
   * @param receiver
   *          The callback object that will be called once the event is
   *          triggered. This may typically be an anonymous implementation of
   *          the class.
   * @param event
   *          An instance of the type of Event that is subscribed to. This does
   *          not need to be the same Event instance that is used to trigger the
   *          event, it may be another instance of the same Event type (the
   *          class inheriting the Event class).
   * @param condition
   *          A condition that must be true for the EventListener to be called.
   */
  void registerEventListener(GenericEventListener receiver, IEvent event,
      Condition condition);
  
  /**
   * Unregisters an event listener bound to an event
   * 
   * @param receiver
   *          The EventListener registered for a particular event.
   * @param event
   */
  void unregisterEventListener(GenericEventListener receiver, IEvent event);
  
  /**
   * Triggers an event.
   * 
   * @param sender
   *          The object instance triggering the event.
   * @param event
   *          An instance of the type of Event that is triggered.
   */
  EventResponse triggerEvent(Object sender, IEvent event);
  
  /**
   * Triggers event synchronously.
   * 
   * @param sender
   * @param event
   * @param isSynchoronous
   */
  EventResponse triggerEvent(Object sender, IEvent event, boolean isSynchronous);
  
  /**
   * Triggers an event in the future.
   * 
   * @param sender
   *          The object instance triggering the event.
   * @param event
   *          An instance of the type of Event that is triggered.
   */
  void triggerFutureEvent(Object sender, IEvent event, long delay,
      TimeUnit timeUnit);
  
  ScheduledExecutorService getScheduler();
  
}
