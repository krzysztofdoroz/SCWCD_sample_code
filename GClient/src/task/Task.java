/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package task;

import java.io.Serializable;

/**
 *
 * @author krzysztof
 */
public class Task implements Serializable{

    //ax*x + bx + c
    private double a;
    private double b;
    private double c;
    private double l;
    private double r;

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getL() {
        return l;
    }

    public double getR() {
        return r;
    }

    public void setL(double l) {
        this.l = l;
    }

    public void setR(double r) {
        this.r = r;
    }




}
