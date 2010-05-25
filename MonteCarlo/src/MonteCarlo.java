

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author krzysztof
 */
public class MonteCarlo {

    private static double A, B, C, L, R;
    private static  int numberOfSteps;

    public static void init(){
        try {
            BufferedReader in = new BufferedReader(new FileReader(new File("data.in")));
            A = Double.parseDouble(in.readLine());
            B = Double.parseDouble(in.readLine());
            C = Double.parseDouble(in.readLine());
            L = Double.parseDouble(in.readLine());
            R = Double.parseDouble(in.readLine());

            numberOfSteps = Integer.parseInt(in.readLine());

        } catch (IOException ex) {
            System.out.println("file data.in doesnt exist");
        }

    }

     static void compute(){

        double x;
        double y;
        int counter = 0;

        Random rand = new Random();

        for(int i = 0; i < numberOfSteps; i++){
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

            if ( y < calculateFx(A, B, C, x))
                counter++;


        }

System.out.println("result: " +  (double)counter/numberOfSteps);


    }



    static double calculateFx(double a, double b, double c, double x){

        return a*x*x + b*x + c;
    }


    public static void main(String[] args) {
        init();
        System.out.println(A + " " + B + " " + C + " " + L + " " + R + " " + numberOfSteps);
        
        compute();
    }

}
