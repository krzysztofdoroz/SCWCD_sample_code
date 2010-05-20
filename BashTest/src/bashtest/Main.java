/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bashtest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author krzysztof
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {


        String cmd = "qsub -W stagein=Hello.class@localhost:/home/students/krdoroz/lab3/Hello.class hello.sh"; // this is the command to execute in the Unix shell
        // create a process for the shell
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
        pb.redirectErrorStream(true); // use this to capture messages sent to stderr
        Process shell = pb.start();
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

        System.out.println(reader.readLine());


    }
}




