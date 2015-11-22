
package com.prchny.base.event;

import java.io.Serializable;

import com.prchny.base.event.impl.EventResponse;

/**
 * interface for all type of events
 */
public interface IEvent extends Serializable {
  
  void addResponse(String k, Object v);
  
  EventResponse getResponse();
  
  Object getPayload();
  
  String getName();
  
  Object getExtraPayLoad();
}
