
package com.prchny.base.activemq;

import java.io.Serializable;
import java.util.List;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageProducerManager {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(MessageProducerManager.class);
  
  public void pushToQueue(final List<? extends Serializable> messages,
      final String queueName, final String networkPath, final String username,
      final String password) throws Exception {
  
    try {
      final Connection connection =
          ActiveMQConnectionUtils
              .getConnection(networkPath, username, password);
      connection.start();
      final Session session = ActiveMQConnectionUtils.getSession(connection);
      final Destination destination =
          ActiveMQConnectionUtils.createQueue(session, queueName);
      final MessageProducer producer =
          ActiveMQConnectionUtils.getProducer(session, destination);
      producer.setDeliveryMode(DeliveryMode.PERSISTENT);
      for (final Serializable message : messages) {
        final ObjectMessage obj = session.createObjectMessage(message);
        producer.send(obj);
      }
      
      session.close();
      connection.close();
    } catch (final Exception e) {
      LOG.error("exception in MessageProducerManager ---> pushToQueue() method"
          + e);
      throw e;
    }
  }
  
}
