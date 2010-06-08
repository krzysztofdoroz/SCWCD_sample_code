package gclient;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.jms.JMSException;
import javax.jms.Destination;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import result.Result;
import task.Task;



public class Main {

    public static final int COUNT = 1;


    public static void main(String[] args) throws NamingException {

        sendRequests();

        getResuls();


    }

    static void getResuls() throws NamingException{

         Context context = null;
        ConnectionFactory factory = null;
        Connection connection = null;
        String factoryName = "ConnectionFactory";
        String destName = null;
        Destination dest = null;
        Session session = null;
        MessageConsumer receiver = null;
        double totalResult = 0.0;


        destName = "Results";


         Properties props = new Properties();
    props.put( Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory" );
    props.put( Context.PROVIDER_URL, "rmi://localhost:1299" );

    //create initial context
    context = new InitialContext( props );


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

            // create the receiver
            receiver = session.createConsumer(dest);

            // start the connection, to enable message receipt
            connection.start();


            
        for(int i = 0; i < COUNT; i++){

        
                Message message = receiver.receive();
                if (message instanceof ObjectMessage) {
                    ObjectMessage result = (ObjectMessage) message;

                    System.out.print(result.getObject().getClass());
                    Result t = (Result)result.getObject();

                    totalResult += t.getResult();


                    //computeAndSend(t);

                    System.out.println("Received result message" );
                } else if (message != null) {
                    System.out.println("Received non object message");
                }
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

        System.out.println("TOTAL RESULT: " + totalResult);




    }


    static void sendRequests() throws NamingException{
          Context context = null;
        ConnectionFactory factory = null;
        Connection connection = null;
        String factoryName = "ConnectionFactory";
        String destName = null;
        Destination dest = null;
        
        Session session = null;
        MessageProducer sender = null;
        String text = "Message ";

         Properties props = new Properties();
    props.put( Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory" );
    props.put( Context.PROVIDER_URL, "rmi://localhost:1299" );

    //create initial context
    context = new InitialContext( props );

       
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

            for (int i = 0; i < COUNT; ++i) {
                ObjectMessage message = session.createObjectMessage();

                task.Task task = new task.Task();
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