package com.rohan.jms.messagestructure;

import javax.jms.JMSContext;
import javax.jms.JMSConsumer;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class RequestReplyDemo {

	public static void main(String[] args) throws NamingException, JMSException {
		
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/requestQueue");
		Queue replyQueue = (Queue) context.lookup("queue/replyQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			
			JMSProducer producer = jmsContext.createProducer();
			producer.setJMSReplyTo();
			TextMessage message = jmsContext.createTextMessage("Arise, Awake and Stop not till he goal is not reached");
			message.setJMSReplyTo(replyQueue);
			producer.send(queue, message);

			JMSConsumer consumer = jmsContext.createConsumer(queue);
			TextMessage messageReceived = (TextMessage) consumer.receive(String.class);
			System.out.println(messageReceived.getText());

			JMSProducer replyProducer = jmsContext.createProducer();
			replyProducer.send(messageReceived.getJMSReplyTo(), "You are awesome!");

			JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
			System.out.println(replyConsumer.receiveBody(String.class));

		}
		
	}

}
