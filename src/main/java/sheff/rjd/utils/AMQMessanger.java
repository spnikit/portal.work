package sheff.rjd.utils;
import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


public class AMQMessanger {
    // URL of the JMS server. DEFAULT_BROKER_URL will just mean
    // that JMS server is on localhost
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String urlredhat = "10.48.0.7:8080";
    // Name of the queue we will be sending messages to
    private static String subject = "TESTQUEUE";

    public void sendMessage(String request) throws JMSException {
        // Getting JMS connection from the server and starting it
        ConnectionFactory connectionFactory =
            new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false,
            Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue(subject);

        MessageProducer producer = session.createProducer(destination);
       //example
        //TextMessage message = session.createTextMessage("こんにちは");
        TextMessage message = session.createTextMessage(request);
        producer.send(message);
        System.out.println("Sent message '" + message.getText() + "'");

        connection.close();
}
}