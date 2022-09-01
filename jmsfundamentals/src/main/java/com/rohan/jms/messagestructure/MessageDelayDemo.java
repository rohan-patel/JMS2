package com.rohan.jms.messagestructure;

import javax.jms.JMSContext;
import javax.jms.JMSConsumer;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Message;
import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.Map;
import java.util.HashMap;


import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageDelayDemo {

	public static void main(String[] args) throws NamingException, InterruptedException {
		
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {

            JMSProducer producer = jmsContext.createProducer();
			producer.setDeliveryDelay(3000);
			producer.send(queue, "Arise, Awake and Stop not till he goal is not reached");

			Message messageReceived = jmsContext.createConsumer(queue).receive(5000);
			System.out.println(messageReceived);

			
		}
		
    }
}