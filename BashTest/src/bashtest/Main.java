/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bashtest;

import java.io.BufferedWriter;
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

/*
        Runtime rtime = Runtime.getRuntime();
        Process child = rtime.exec("/bin/bash");

        BufferedWriter outCommand = new BufferedWriter(new
        OutputStreamWriter(child.getOutputStream()));
        outCommand.write("/home/krzysztof/NetBeansProjects/BashTest/MyShellScript.sh");
        outCommand.flush();

        System.out.print("WTF");
InputStream str =  child.getInputStream();

       

child.waitFor();
 System.out.print(str.read());

    int retCode = child.exitValue();

    System.out.print(retCode);

*/
        String cmd = "/home/krzysztof/NetBeansProjects/BashTest/MyShellScript.sh"; // this is the command to execute in the Unix shell
// create a process for the shell
ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
pb.redirectErrorStream(true); // use this to capture messages sent to stderr
Process shell = pb.start();
InputStream shellIn = shell.getInputStream(); // this captures the output from the command
int shellExitStatus = shell.waitFor(); // wait for the shell to finish and get the return code
// at this point you can process the output issued by the command
// for instance, this reads the output and writes it to System.out:
int c;
while ((c = shellIn.read()) != -1) {System.out.write(c);}
// close the stream
try {shellIn.close();} catch (IOException ignoreMe) {}
    }

}
