package com.rohan.jms.basics;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class JMSContextDemo {

	public static void main(String[] args) throws NamingException {
		
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {
			
			jmsContext.createProducer().send(queue, "Arise, Awake and Stop not till he goal is not reached");
			String messageReceived = jmsContext.createConsumer(queue).receiveBody(String.class);
			
			System.out.println(messageReceived);
			
		}
		
	}

}
