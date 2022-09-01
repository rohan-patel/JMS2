package com.rohan.jms.messagestructure;

import javax.jms.JMSContext;
import javax.jms.JMSConsumer;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.BytesMessage;
import javax.jms.StreamMessage;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.Map;
import java.util.HashMap;


import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessageTypesDemo {

	public static void main(String[] args) throws NamingException, InterruptedException, JMSException {
		
		InitialContext context = new InitialContext();
		Queue queue = (Queue) context.lookup("queue/myQueue");
		
		try(ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext()) {

            JMSProducer producer = jmsContext.createProducer();
			TextMessage message = jmsContext.createTextMessage("Arise, Awake and Stop not till he goal is not reached");

			BytesMessage bytesMessage = jmsContext.createBytesMessage();
			bytesMessage.writeUTF("Rohan");
			bytesMessage.writeLong(123l);
			
			StreamMessage streamMessage = jmsContext.createBytesMessage();
			streamMessage.writeBoolean(true);
			streamMessage.writeFloat(22.25f);

			MapMessage mapMessage = jmsContext.createMapMessage();
			mapMessage.setBoolean("IsCreditAvailable", true);

			producer.send(queue, mapMessage);

			// System.out.println("<------ Bytes Message ------>");
			// BytesMessage messageReceived = (BytesMessage) jmsContext.createConsumer(queue).receive(5000);
			// System.out.println(messageReceived.readUTF());
			// System.out.println(messageReceived.readLong());

			// System.out.println("<------ Streams Message ------>");
			// StreamMessage messageReceived = (StreamMessage) jmsContext.createConsumer(queue).receive(5000);
			// System.out.println(messageReceived.readBoolean());
			// System.out.println(messageReceived.readFloat());

			System.out.println("<------ Maps Message ------>");
			MapMessage messageReceived = (MapMessage) jmsContext.createConsumer(queue).receive(5000);
			System.out.println(messageReceived.getBoolean("IsCreditAvailable"));

			
		}
		
    }
}