
package com.prchny.base.event.impl;

import com.prchny.base.event.IEvent;

/**
 * @author singla
 * @param <T>
 */
public class GenericEvent<T> implements IEvent {
  
  private static final long serialVersionUID = -110008327666274539L;
  
  private T payload;
  
  private String name;
  
  private EventResponse response = new EventResponse();
  
  private Object extraPayload;
  
  public GenericEvent(final T payload, final String name) {
  
    this.payload = payload;
    this.name = name;
  }
  
  public GenericEvent(final T payload, final String name,
      final Object extrPayLoad) {
  
    this.payload = payload;
    this.name = name;
    this.extraPayload = extrPayLoad;
  }
  
  public GenericEvent(final String name) {
  
    this(null, name);
  }
  
  @Override
  public T getPayload() {
  
    return payload;
  }
  
  @Override
  public Object getExtraPayLoad() {
  
    return this.extraPayload;
  }
  
  @Override
  public void addResponse(final String k, final Object v) {
  
    response.getResponse().put(k, v);
  }
  
  @Override
  public EventResponse getResponse() {
  
    return response;
  }
  
  @Override
  public String getName() {
  
    return name;
  }
  
  @Override
  public int hashCode() {
  
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((name == null) ? 0 : name.hashCode());
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
    final IEvent other = (IEvent) obj;
    if (name == null) {
      if (other.getName() != null) {
        return false;
      }
    } else if (!name.equals(other.getName())) {
      return false;
    }
    return true;
  }
}
