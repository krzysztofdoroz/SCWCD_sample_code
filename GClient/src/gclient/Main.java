package gclient;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.jms.JMSException;
import javax.jms.Destination;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import task.Task;



public class Main {

    /**
     * Main line.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) throws NamingException {
        Context context = null;
        ConnectionFactory factory = null;
        Connection connection = null;
        String factoryName = "ConnectionFactory";
        String destName = null;
        Destination dest = null;
        int count = 1;
        Session session = null;
        MessageProducer sender = null;
        String text = "Message ";

         Properties props = new Properties();
    props.put( Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory" );
    props.put( Context.PROVIDER_URL, "rmi://localhost:1299" );
    
    //create initial context
    context = new InitialContext( props );

        count = 1;
        destName = "ClientRequests";

        try {
           
            // look up the ConnectionFactory
            factory = (ConnectionFactory) context.lookup(factoryName);

            // look up the Destination
            dest = (Destination) context.lookup(destName);

            // create the connection
            connection = factory.createConnection();

            // create the session
            session = connection.createSession(
                false, Session.AUTO_ACKNOWLEDGE);

            // create the sender
            sender = session.createProducer(dest);

            // start the connection, to enable message sends
            connection.start();

            for (int i = 0; i < count; ++i) {
                ObjectMessage message = session.createObjectMessage();

                Task task = new Task();
                task.setA(1);
                task.setB(0);
                task.setC(0);
                task.setL(0);
                task.setR(1);

                message.setObject(task);
                sender.send(message);
                System.out.println("Sent message ");
            }
        } catch (JMSException exception) {
            exception.printStackTrace();
        } catch (NamingException exception) {
            exception.printStackTrace();
        } finally {
            // close the context
            if (context != null) {
                try {
                    context.close();
                } catch (NamingException exception) {
                    exception.printStackTrace();
                }
            }

            // close the connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

}