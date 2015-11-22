
package com.prchny.base.activemq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQConnectionUtils {
  
  private static final Logger LOG = LoggerFactory
      .getLogger(ActiveMQConnectionUtils.class);
  
  public static Connection getConnection(final String networkPath,
      final String username, final String password) throws Exception {
  
    try {
      // Create a ConnectionFactory
      final ActiveMQConnectionFactory connectionFactory =
          new ActiveMQConnectionFactory(username, password, networkPath);
      
      // Create a Connection
      return connectionFactory.createConnection();
    } catch (final Exception e) {
      LOG.error("error in creating connection in consumer" + e);
      throw e;
    }
  }
  
  public static Session getSession(final String networkPath,
      final String username, final String password) throws JMSException {
  
    final ActiveMQConnectionFactory connectionFactory =
        new ActiveMQConnectionFactory(username, password, networkPath);
    final Connection connection = connectionFactory.createConnection();
    connection.start();
    return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
  }
  
  public static Session getSession(final Connection connection)
      throws JMSException {
  
    return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
  }
  
  public static Destination createQueue(final Session session,
      final String queueName) throws JMSException {
  
    return session.createQueue(queueName);
  }
  
  public static MessageProducer getProducer(final Session session,
      final Destination queue) throws JMSException {
  
    return session.createProducer(queue);
  }
  
  public static MessageConsumer getConsumer(final Session session,
      final Destination queue) throws JMSException {
  
    return session.createConsumer(queue);
  }
}
