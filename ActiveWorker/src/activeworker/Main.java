/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact jima@intalio.com.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2004-2005 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id: Receiver.java,v 1.2 2005/11/18 03:28:01 tanderson Exp $
 */
import java.util.Properties;
import java.util.Random;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Destination;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Session;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import task.Task;


/**
 * Synchronous <code>MessageConsumer</code> example.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.2 $ $Date: 2005/11/18 03:28:01 $
 */
public class Main {

    /**
     * Main line.
     *
     * @param args command line arguments
     */

    public static final int PRECISSION = 1000;


    public static void main(String[] args) throws NamingException {
        Context context = null;
        ConnectionFactory factory = null;
        Connection connection = null;
        String factoryName = "ConnectionFactory";
        String destName = null;
        Destination dest = null;
        Session session = null;
        MessageConsumer receiver = null;

        

        destName = "ClientRequests";


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

           
                Message message = receiver.receive();
                if (message instanceof ObjectMessage) {
                    ObjectMessage task = (ObjectMessage) message;

                    System.out.print(task.getObject().getClass());
                    Task t = (Task)task.getObject();
                    computeAndSend(t);

                    System.out.println("Received message" );
                } else if (message != null) {
                    System.out.println("Received non object message");
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

    static double calculateFx(double a, double b, double c, double x){

        return a*x*x + b*x + c;
    }

    static void computeAndSend(Task task){

        double x;
        double y;
        int counter = 0;

        Random rand = new Random();

        for(int i = 0; i < PRECISSION; i++){
            int a = rand.nextInt() % 1000;
            int b = rand.nextInt() % 1000;

            if (b == 0)
                b = 1;

            x = a/b;
            if (x < 0)
                x = -x;

            a = rand.nextInt() % 1000;
            b = rand.nextInt() % 1000;

            if (b == 0)
                b = 1;
            
            y = a/b;
            if (y < 0)
                y = -y;

            if ( y < calculateFx(task.getA(), task.getB(), task.getC(), x))
                counter++;

            
        }
        
System.out.println((double)counter/PRECISSION);


    }


}