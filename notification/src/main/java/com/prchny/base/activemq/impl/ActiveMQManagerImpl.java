/*
 *  Copyright 2012 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *  
 *  @version     1.0, 19-Jun-2012
 *  @author sunny
 */

package com.prchny.base.activemq.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.prchny.base.activemq.IActiveMQManager;
import com.prchny.base.activemq.exception.ActiveMQException;

@Service("activeMQManager")
public class ActiveMQManagerImpl implements IActiveMQManager {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(ActiveMQManagerImpl.class);
  
  Map<Long, ActiveMQProducer> tokenToPublisherQueue =
      new HashMap<Long, ActiveMQProducer>();
  
  Map<Long, ActiveMQConsumer> tokenToSubscriberQueue =
      new HashMap<Long, ActiveMQConsumer>();
  
  private class ActiveMQProducer {
    
    private final Session session;
    
    private final MessageProducer messageProducer;
    
    private final Connection connection;
    
    public ActiveMQProducer(final Session session,
        final MessageProducer producer, final Connection connection) {
    
      this.session = session;
      messageProducer = producer;
      this.connection = connection;
    }
    
    public Session getSession() {
    
      return session;
    }
    
    public MessageProducer getMessageProducer() {
    
      return messageProducer;
    }
    
    public Connection getConnection() {
    
      return connection;
    }
  }
  
  private class ActiveMQConsumer {
    
    private final MessageConsumer consumer;
    
    private final Session session;
    
    private final Connection connection;
    
    public ActiveMQConsumer(final MessageConsumer consumer,
        final Session session, final Connection connection) {
    
      this.consumer = consumer;
      this.session = session;
      this.connection = connection;
    }
    
    public MessageConsumer getConsumer() {
    
      return consumer;
    }
    
    public Session getSession() {
    
      return session;
    }
    
    public Connection getConnection() {
    
      return connection;
    }
  }
  
  @Override
  public Long registerPublisher(final String queueName, final String url,
      final String userName, final String password) throws JMSException {
  
    return registerPublisher(queueName, url, userName, password, false,
        Session.AUTO_ACKNOWLEDGE, DeliveryMode.PERSISTENT);
  }
  
  /**
   * @param acknowledgeMode
   *          Session.AUTO_ACKNOWLEDGE, DUPS_OK_ACKNOWLEDGE, CLIENT_ACKNOWLEDGE
   * @param deliveryMode
   *          DeliveryMode.NON_PERSISTENT, DeliveryMode.PERSISTENT
   * @throws JMSException
   */
  @Override
  public Long registerPublisher(final String queueName, final String url,
      final String userName, final String password, final boolean transacted,
      final int acknowledgeMode, final int deliveryMode) throws JMSException {
  
    Connection connection = null;
    Session session = null;
    MessageProducer producer = null;
    try {
      connection = getConnection(userName, password, url);
      connection.start();
      session = connection.createSession(transacted, acknowledgeMode);
      final Destination destination = session.createQueue(queueName);
      producer = session.createProducer(destination);
      producer.setDeliveryMode(deliveryMode);
      final Long token = RandomUtils.nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
      final ActiveMQProducer activeMQProducer =
          new ActiveMQProducer(session, producer, connection);
      tokenToPublisherQueue.put(token, activeMQProducer);
      return token;
    } catch (final JMSException e) {
      close(producer, session, connection);
      throw e;
    } catch (final RuntimeException e) {
      close(producer, session, connection);
      throw e;
    }
  }
  
  @Override
  public void unregisterPublisher(final Long token) throws JMSException {
  
    final ActiveMQProducer activeMQProducer = tokenToPublisherQueue.get(token);
    if (activeMQProducer != null) {
      close(activeMQProducer.getMessageProducer(),
          activeMQProducer.getSession(), activeMQProducer.getConnection());
    }
    tokenToPublisherQueue.remove(token);
  }
  
  @Override
  public <T extends Serializable> void publish(final Long token,
      final List<T> messages) throws ActiveMQException, JMSException {
  
    final ActiveMQProducer activeMQProducer = tokenToPublisherQueue.get(token);
    if (activeMQProducer == null) {
      throw new ActiveMQException("Producer not registered");
    }
    
    for (final Serializable message : messages) {
      final ObjectMessage obj =
          activeMQProducer.getSession().createObjectMessage(message);
      activeMQProducer.getMessageProducer().send(obj);
    }
  }
  
  @Override
  public <T extends Serializable> void publish(final Long token, final T message)
      throws ActiveMQException, JMSException {
  
    final ActiveMQProducer activeMQProducer = tokenToPublisherQueue.get(token);
    if (activeMQProducer == null) {
      throw new ActiveMQException("Producer not registered");
    }
    
    final ObjectMessage obj =
        activeMQProducer.getSession().createObjectMessage(message);
    activeMQProducer.getMessageProducer().send(obj);
  }
  
  @Override
  public Long registerSubscriber(final String queueName, final String url,
      final String userName, final String password,
      final MessageListener listener) throws JMSException {
  
    return registerSubscriber(queueName, url, userName, password, listener,
        false, Session.AUTO_ACKNOWLEDGE);
  }
  
  /**
   * @param acknowledgeMode
   *          Session.AUTO_ACKNOWLEDGE, DUPS_OK_ACKNOWLEDGE, CLIENT_ACKNOWLEDGE
   * @throws JMSException
   */
  @Override
  public Long registerSubscriber(final String queueName, final String url,
      final String userName, final String password,
      final MessageListener listener, final boolean transacted,
      final int acknowledgeMode) throws JMSException {
  
    Connection connection = null;
    Session session = null;
    MessageConsumer consumer = null;
    try {
      connection = getConnection(userName, password, url);
      connection.start();
      session = connection.createSession(transacted, acknowledgeMode);
      final Destination destination = createQueue(session, queueName);
      
      consumer = session.createConsumer(destination);
      consumer.setMessageListener(listener);
      final Long token = RandomUtils.nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
      final ActiveMQConsumer activeMQConsumer =
          new ActiveMQConsumer(consumer, session, connection);
      tokenToSubscriberQueue.put(token, activeMQConsumer);
      return token;
    } catch (final JMSException e) {
      close(consumer, session, connection);
      throw e;
    } catch (final RuntimeException e) {
      close(consumer, session, connection);
      throw e;
    }
  }
  
  @Override
  public void unregisterSubscriber(final Long token) throws JMSException {
  
    final ActiveMQConsumer activeMQConsumer = tokenToSubscriberQueue.get(token);
    if (activeMQConsumer != null) {
      close(activeMQConsumer.getConsumer(), activeMQConsumer.getSession(),
          activeMQConsumer.getConnection());
    }
    tokenToSubscriberQueue.remove(token);
  }
  
  private Connection getConnection(final String userName,
      final String password, final String url) throws JMSException {
  
    final ActiveMQConnectionFactory connectionFactory =
        new ActiveMQConnectionFactory(userName, password, url);
    return connectionFactory.createConnection();
  }
  
  private Destination createQueue(final Session session, final String queueName)
      throws JMSException {
  
    return session.createQueue(queueName);
  }
  
  private void close(final MessageConsumer consumer, final Session session,
      final Connection connection) {
  
    if (consumer != null) {
      try {
        consumer.setMessageListener(null);
        consumer.close();
      } catch (final Exception e) {
        LOG.error("Error in closing consumer", e);
      }
    }
    close(session, connection);
  }
  
  private void close(final MessageProducer producer, final Session session,
      final Connection connection) {
  
    if (producer != null) {
      try {
        producer.close();
      } catch (final Exception e) {
        LOG.error("Error in closing producer", e);
      }
    }
    close(session, connection);
  }
  
  private void close(final Session session, final Connection connection) {
  
    if (session != null) {
      try {
        session.close();
      } catch (final Exception e) {
        LOG.error("Error in closing session", e);
      }
    }
    if (connection != null) {
      try {
        connection.close();
      } catch (final Exception e) {
        LOG.error("Error in closing connection", e);
      }
    }
  }
}
