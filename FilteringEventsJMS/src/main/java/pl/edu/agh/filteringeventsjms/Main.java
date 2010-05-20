package pl.edu.agh.filteringeventsjms;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;








/**
 *
 * @author krzysztof
 */



public class Main {

    public static void main(String ... arg) throws NamingException{
  Properties props = new Properties();
props.put( Context.INITIAL_CONTEXT_FACTORY,
           "org.exolab.jms.jndi.InitialContextFactory" );
props.put( Context.PROVIDER_URL, "rmi://localhost:1299" );
Context context = new InitialContext( props );
QueueConnectionFactory factory = (QueueConnectionFactory)
     context.lookup("JmsQueueConnectionFactory");
Queue queue = (Queue) context.lookup("addressbook-queue");
QueueConnection connection = factory.createQueueConnection();
connection.start();
QueueSession session = connection.
        createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
QueueSender sender = session.createSender(queue);
ObjectMessage message = session.createObjectMessage();
message.setObject( userInfo );
sender.send(message );
connection.close();

System.out.print("saasas");
    }

}
