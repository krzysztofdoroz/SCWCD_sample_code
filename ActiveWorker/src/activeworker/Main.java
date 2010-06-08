package activeworker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import result.Result;
import task.Task;



public class Main {

    public static final int PRECISSION = 1000;


    public static void main(String[] args) throws NamingException {

        getRequest();
        try {
            System.out.println("sending job to PBS...");
            sentToPBS();
        } catch (FileNotFoundException ex) {
           System.out.println("File not foud exception");
        } catch (IOException ex) {
            System.out.println("IO exception");
        } catch (InterruptedException ex) {
            System.out.println("Interrupted exception");
        }


    }


    static void getRequest() throws NamingException{
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

                    BufferedWriter out;
                try {
                    out = new BufferedWriter(new FileWriter(new File("data.in")));

                    out.write(new Double(t.getA()).toString());
                    out.write("\n");
                    out.write(new Double(t.getB()).toString());
                    out.write("\n");
                    out.write(new Double(t.getC()).toString());
                    out.write("\n");
                    out.write(new Double(t.getL()).toString());
                    out.write("\n");
                    out.write(new Double(t.getR()).toString());
                    out.write("\n");
                    //TODO: refactor!
                    out.write(new Integer(10000).toString());
                    out.write("\n");

                    out.close();

                } catch (IOException ex) {
                    System.out.println("Exception occured while writing to data.in");
                }


                    //computeAndSend(t);

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

        try {
            //wyslij zadanie do PBS-a
            sentToPBS();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found exception");
        } catch (IOException ex) {
            System.out.println("IOException exception");
        } catch (InterruptedException ex) {
            System.out.println("Interrupted exception");
        }

    }

    static void sentToPBS() throws FileNotFoundException, IOException, InterruptedException, NamingException{

          String cmd = "qsub -W stagein=MonteCarlo.class@localhost:/home/students/krdoroz/lab3/MonteCarlo.class,data.in@localhost:/home/students/krdoroz/lab3/ActiveWorker/data.in hello.sh"; // this is the command to execute in the Unix shell
        // create a process for the shell
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
        pb.redirectErrorStream(true); // use this to capture messages sent to stderr
        Process shell = null;
        try {
            shell = pb.start();
        } catch (IOException ex) {
            System.out.println("shell exception");
        }
        InputStream shellIn = shell.getInputStream(); // this captures the output from the command
        int shellExitStatus = shell.waitFor(); // wait for the shell to finish and get the return code
        // at this point you can process the output issued by the command
        // for instance, this reads the output and writes it to System.out:
        int c;

        byte[] nazwa = new byte[32];

        int dl =  shellIn.read(nazwa);

        String pr = new String(nazwa,0,dl);

        System.out.println( pr);

        String numerProcesu = pr.split("\\.")[0];
        //for(String s: pr.split("\\."))
        //    System.out.println(s);


        System.out.println(numerProcesu);


        Integer numer = Integer.parseInt(numerProcesu);

        System.out.println(numer);


        // close the stream
        try {
            shellIn.close();
        } catch (IOException ignoreMe) {
            ignoreMe.printStackTrace();
        }

        //czekamy na plik z wynikiem:

        File result = new File("task.o" + numer);

        while ( !result.exists()){
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }

        }

        BufferedReader reader = new BufferedReader(new FileReader(result));

        String line = "";
         line = reader.readLine();
         line = reader.readLine();
         String data[] = line.split(" ");

         reader.close();

         System.out.println(data[1]);
         Double res = Double.parseDouble(data[1]);
           

        Result integrationResult = new Result();
        integrationResult.setResult(res);

        sentToResultQueue(integrationResult);

    }
    
    static void sentToResultQueue(Result result) throws NamingException{
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


        destName = "Results";

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

           
                ObjectMessage message = session.createObjectMessage();

                

                message.setObject(result);
                sender.send(message);
                System.out.println("Sent result to a queue.. ");
            
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