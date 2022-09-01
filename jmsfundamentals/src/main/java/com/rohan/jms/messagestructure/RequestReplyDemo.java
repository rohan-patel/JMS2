package com.rohan.jms.messagestructure;

import javax.jms.JMSContext;
import javax.jms.JMSConsumer;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.Map;
import java.util.HashMap;


import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class RequestReplyDemo {

	public static void main(String[] args) throws NamingException, JMSException {
		
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/requestQueue");
		Queue replyQueue = (Queue) context.lookup("queue/replyQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			
			// We have 2 apps; App A, App B

			// App A; creating producer to send request
			JMSProducer producer = jmsContext.createProducer();
			// producer.setJMSReplyTo();
			TextMessage message = jmsContext.createTextMessage("Arise, Awake and Stop not till he goal is not reached");
			message.setJMSReplyTo(replyQueue);
			producer.send(queue, message);
			System.out.println(message.getJMSMessageID());

			// A map to store all the request messages and their messageID
			Map<String, TextMessage> requestMessages = new HashMap<>();
			requestMessages.put(message.getJMSMessageID(), message);

			// App B; creating Consumer to get the request
			JMSConsumer consumer = jmsContext.createConsumer(queue);
			TextMessage messageReceived = (TextMessage) consumer.receive();
			System.out.println(messageReceived.getText());

			// App B; creating producer for replying to the App A
			JMSProducer replyProducer = jmsContext.createProducer();
			TextMessage replyMessage = jmsContext.createTextMessage("You are awesome!");
			replyMessage.setJMSCorrelationID(messageReceived.getJMSMessageID());
			replyProducer.send(messageReceived.getJMSReplyTo(), replyMessage);

			// App A; creating Consumer to get the reply from App B
			JMSConsumer replyConsumer = jmsContext.createConsumer(replyQueue);
			// System.out.println(replyConsumer.receiveBody(String.class));
			TextMessage replyReceived = (TextMessage) replyConsumer.receive();
			System.out.println(replyReceived.getJMSCorrelationID());
			System.out.println(requestMessages.get(replyReceived.getJMSCorrelationID()).getText());


		}
		
	}

}
