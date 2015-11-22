/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 19-Jun-2012
 *  @author sunny
 */

package com.prchny.base.activemq;

import java.io.Serializable;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MessageListener;

import com.prchny.base.activemq.exception.ActiveMQException;

public interface IActiveMQManager {
  
  Long registerPublisher(String queueName, String url, String userName,
      String password) throws JMSException;
  
  Long registerPublisher(String queueName, String url, String userName,
      String password, boolean transacted, int acknowledgeMode, int deliveryMode)
      throws JMSException;
  
  void unregisterPublisher(Long token) throws JMSException;
  
  <T extends Serializable> void publish(Long token, List<T> messages)
      throws ActiveMQException, JMSException;
  
  <T extends Serializable> void publish(Long token, T message)
      throws ActiveMQException, JMSException;
  
  Long registerSubscriber(String queueName, String url, String userName,
      String password, MessageListener listener) throws JMSException;
  
  Long registerSubscriber(String queueName, String url, String userName,
      String password, MessageListener listener, boolean transacted,
      int acknowledgeMode) throws JMSException;
  
  void unregisterSubscriber(Long token) throws JMSException;
  
}
